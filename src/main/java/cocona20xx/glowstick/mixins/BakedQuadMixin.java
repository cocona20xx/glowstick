/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package cocona20xx.glowstick.mixins;

import cocona20xx.glowstick.BakedQuadAccessor;
import net.minecraft.client.render.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BakedQuad.class)
public class BakedQuadMixin implements BakedQuadAccessor {
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
