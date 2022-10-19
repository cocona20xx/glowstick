/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package cocona20xx.glowstick.mixins;

import cocona20xx.glowstick.GlowstickModClient;
import cocona20xx.glowstick.BakedQuadAccessor;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemRenderer.class)
public class ItemRenderMixin {
	@ModifyArg(method = "renderBakedItemQuads", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;bakedQuad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;FFFII)V"), index = 5)
	public int modifyLighting(MatrixStack.Entry matrixEntry, BakedQuad quad, float red, float green, float blue, int light, int overlay){
		BakedQuadAccessor wrap = (BakedQuadAccessor)quad;
		short sky = (short)(light >> 16);
		short block = (short)light;
		block = (short) (block << 4);
		if((wrap.getModifier() != 0 && block < 240)) {
			short mod = (short) (block + wrap.getModifier());
			mod = GlowstickModClient.shortMin(mod, (short) 240);
			return (sky << 16) | (mod & 0xFFFF);
		}
		else return light;
	}
}


