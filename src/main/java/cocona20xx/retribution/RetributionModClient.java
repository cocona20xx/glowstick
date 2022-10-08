package cocona20xx.retribution;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetributionModClient implements ClientModInitializer {
	public static final String MOD_ID = "retribution";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final int MODIFIER = 0;
	@Override
	public void onInitializeClient(ModContainer mod) {
		LOGGER.info("It's retributing time!!!");
	}
}
