package cocona20xx.retribution.mixins;

import cocona20xx.retribution.impl.ParsedItemFlagData;
import cocona20xx.retribution.internal.BakedQuadLightingModifierAccessor;
import cocona20xx.retribution.internal.UnbakedModelAccessor;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(JsonUnbakedModel.class)
public abstract class JsonUnbakedModelMixin implements UnbakedModel, UnbakedModelAccessor {

	@Unique private ParsedItemFlagData data = new ParsedItemFlagData();
	@Unique private JsonUnbakedModel actual;

	@Inject(method = "createQuad", at = @At("RETURN"), cancellable = true)
	private static void bakeQuadInjector(ModelElement element, ModelElementFace elementFace, Sprite sprite, Direction side, ModelBakeSettings settings, Identifier id, CallbackInfoReturnable<BakedQuad> cir){
		for (int i = 0; i < 4; i++) {
			if(JsonUnbakedModelMixin.matchFaceWithDataValue(elementFace, i, this.data))
		}
		BakedQuad initialReturn = cir.getReturnValue();
		BakedQuadLightingModifierAccessor wrapped = ((BakedQuadLightingModifierAccessor)initialReturn);
		wrapped.storeActual(initialReturn);
		wrapped.setModifier(150);
		cir.setReturnValue(wrapped.getActual());
	}

	@Override
	public void setData(ParsedItemFlagData data) {
		this.data = data;
	}

	@Override
	public void storeActual(JsonUnbakedModel model) {
		actual = model;
	}

	@Override
	public JsonUnbakedModel getActual() {
		return actual;
	}

	private static boolean matchFaceWithDataValue(ModelElementFace face, int id, ParsedItemFlagData data){
		String keyStr = data.getId(id);
		if(Objects.isNull(keyStr)) return false;
		else {
			return Objects.equals(face.textureId, keyStr);
		}
	}
}
