/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package cocona20xx.glowstick.mixins;

import cocona20xx.glowstick.GlowstickModClient;
import cocona20xx.glowstick.EmissiveDataReloader;
import cocona20xx.glowstick.BakedQuadAccessor;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JsonUnbakedModel.class)
public class JsonUnbakedModelMixin{

	@Inject(method = "createQuad", at = @At("RETURN"), cancellable = true)
	private static void createQuadInjector(ModelElement element, ModelElementFace elementFace, Sprite sprite, Direction side, ModelBakeSettings settings, Identifier id, CallbackInfoReturnable<BakedQuad> cir){
		BakedQuad initReturn = cir.getReturnValue();
		if(EmissiveDataReloader.checkValidity(EmissiveDataReloader.sanitizeValidId(sprite.getId()), EmissiveDataReloader.sanitizeValidId(id))){
			BakedQuadAccessor bakedQuadAccessor = (BakedQuadAccessor)initReturn;
			bakedQuadAccessor.storeActual(initReturn);
			bakedQuadAccessor.setModifier(GlowstickModClient.EMISSIVE_MODIFIER);
			cir.setReturnValue(bakedQuadAccessor.getActual());
		}
	}
}
