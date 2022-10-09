package cocona20xx.retribution.mixins;

import cocona20xx.retribution.RetributionModClient;
import cocona20xx.retribution.impl.ParsedItemFlagData;
import cocona20xx.retribution.internal.BakedQuadLightingModifierAccessor;
import cocona20xx.retribution.internal.ModelElementEmissiveAccessor;
import cocona20xx.retribution.internal.UnbakedModelAccessor;
import net.minecraft.client.render.model.*;
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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(JsonUnbakedModel.class)
public abstract class JsonUnbakedModelMixin implements UnbakedModel, UnbakedModelAccessor {

	@Unique private ParsedItemFlagData data = new ParsedItemFlagData();
	@Unique private JsonUnbakedModel actual;
	@Unique
	private static final int EMISSIVE_MODIFIER =  150;

	@Inject(method = "createQuad", at = @At("RETURN"), cancellable = true)
	private static void createQuadInjector(ModelElement element, ModelElementFace elementFace, Sprite sprite, Direction side, ModelBakeSettings settings, Identifier id, CallbackInfoReturnable<BakedQuad> cir){
		ModelElementEmissiveAccessor accessor = (ModelElementEmissiveAccessor)element;
		if(accessor.isEmissive()) {
			BakedQuad initialReturn = cir.getReturnValue();
			BakedQuadLightingModifierAccessor wrapped = ((BakedQuadLightingModifierAccessor) initialReturn);
			wrapped.storeActual(initialReturn);
			wrapped.setModifier(EMISSIVE_MODIFIER);
			cir.setReturnValue(wrapped.getActual());
		}
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

	@Override
	public ParsedItemFlagData getData() {
		return this.data;
	}
}
