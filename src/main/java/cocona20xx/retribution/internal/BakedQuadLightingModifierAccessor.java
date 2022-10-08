package cocona20xx.retribution.internal;

import net.minecraft.client.render.model.BakedQuad;

public interface BakedQuadLightingModifierAccessor {
	int getModifier();
	void setModifier(int newMod);

	void storeActual(BakedQuad toStore);

	BakedQuad getActual();
}
