/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package cocona20xx.glowstick;

import net.minecraft.resource.ResourceType;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.reloader.ResourceReloaderKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlowstickModClient implements ClientModInitializer {
	public static final String MOD_ID = "glowstick";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final int EMISSIVE_MODIFIER = 200;
	@Override
	public void onInitializeClient(ModContainer mod) {
		ResourceLoader.get(ResourceType.CLIENT_RESOURCES).registerReloader(new EmissiveDataReloader());
		ResourceLoader.get(ResourceType.CLIENT_RESOURCES).addReloaderOrdering(EmissiveDataReloader.RELOADER_ID, ResourceReloaderKeys.BEFORE_VANILLA);
	}

	public static short shortMin(short a, short b){
		return (a <= b) ? a : b;
	}
}
