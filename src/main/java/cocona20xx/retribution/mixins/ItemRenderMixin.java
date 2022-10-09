package cocona20xx.retribution.mixins;

import cocona20xx.retribution.RetributionModClient;
import cocona20xx.retribution.impl.EmissiveDataReloader;
import cocona20xx.retribution.internal.BakedQuadLightingModifierAccessor;
import net.minecraft.client.MinecraftClient;
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
		BakedQuadLightingModifierAccessor wrap = (BakedQuadLightingModifierAccessor)quad;
		String sanitySimpleId = EmissiveDataReloader.simplifyFromId(quad.getSprite().getId());
		boolean sanity = EmissiveDataReloader.hasDataForSimple(sanitySimpleId);
		if((wrap.getModifier() != 0 && light < 240) && sanity) {
			int mod = light + wrap.getModifier();
			return Math.min(mod, 240);
		}
		else return light;
	}
}

