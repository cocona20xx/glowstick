package cocona20xx.retribution;

import cocona20xx.retribution.impl.EmissiveDataReloader;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.reloader.ResourceReloaderKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetributionModClient implements ClientModInitializer {
	public static final String MOD_ID = "retribution";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitializeClient(ModContainer mod) {
		LOGGER.info("Retribution is loading...");
		ResourceLoader.get(ResourceType.CLIENT_RESOURCES).registerReloader(new EmissiveDataReloader());
		ResourceLoader.get(ResourceType.CLIENT_RESOURCES).addReloaderOrdering(EmissiveDataReloader.RELOADER_ID, ResourceReloaderKeys.BEFORE_VANILLA);
	}
}
