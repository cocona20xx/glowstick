package cocona20xx.retribution.mixins;

import cocona20xx.retribution.RetributionModClient;
import cocona20xx.retribution.impl.EmissiveDataReloader;
import cocona20xx.retribution.impl.ParsedItemFlagData;
import cocona20xx.retribution.internal.ModelElementEmissiveAccessor;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin {
	@Inject(method = "create", at = @At(value = "INVOKE_ASSIGN",
			target = "Lnet/minecraft/client/render/model/json/ItemModelGenerator;addLayerElements(ILjava/lang/String;Lnet/minecraft/client/texture/Sprite;)Ljava/util/List;", shift = At.Shift.BY, by = 1),
			locals = LocalCapture.CAPTURE_FAILHARD)
	public void createInjector(Function<SpriteIdentifier, Sprite> textureGetter, JsonUnbakedModel blockModel, CallbackInfoReturnable<JsonUnbakedModel> cir, Map<String, Either<SpriteIdentifier, String>> map, List<ModelElement> list, int i, String string, SpriteIdentifier spriteIdentifier, Sprite sprite){
		int size = list.size();
		Identifier id = spriteIdentifier.getTextureId();
		String simple = EmissiveDataReloader.simplifyFromId(id);
		if(EmissiveDataReloader.hasDataForSimple(simple)){
			RetributionModClient.LOGGER.info(simple);
			int flagInt = EmissiveDataReloader.flagStringToInt(string);
			ParsedItemFlagData data = EmissiveDataReloader.getData(simple);
			if(data.hasId(flagInt)){
				for (int j = 0; j < size; j++) {
					ModelElement current = list.get(j);
					ModelElementEmissiveAccessor elementAccessor = (ModelElementEmissiveAccessor)current;
					elementAccessor.setEmissive(true);
					elementAccessor.storeActual(current);
					list.set(j, elementAccessor.getActual());
				}
			}
		}
	}
}

