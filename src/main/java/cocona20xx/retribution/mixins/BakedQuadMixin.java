package cocona20xx.retribution.mixins;

import cocona20xx.retribution.internal.BakedQuadLightingModifierAccessor;
import net.minecraft.client.render.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BakedQuad.class)
public class BakedQuadMixin implements BakedQuadLightingModifierAccessor {
	@Unique private int modifierValue = 0;
	@Unique private BakedQuad actual;

	@Override
	public int getModifier() {
		return modifierValue;
	}

	@Override
	public void setModifier(int newMod) {
		modifierValue = newMod;
	}

	@Override
	public void storeActual(BakedQuad toStore) {
		this.actual = toStore;
	}

	@Override
	public BakedQuad getActual() {
		return actual;
	}
}
