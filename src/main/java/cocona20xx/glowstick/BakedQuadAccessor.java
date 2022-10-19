/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package cocona20xx.glowstick;

import net.minecraft.client.render.model.BakedQuad;

public interface BakedQuadAccessor {
	int getModifier();
	void setModifier(int newMod);

	void storeActual(BakedQuad toStore);

	BakedQuad getActual();
}
