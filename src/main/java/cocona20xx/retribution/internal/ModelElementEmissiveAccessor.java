package cocona20xx.retribution.internal;

import net.minecraft.client.render.model.json.ModelElement;

public interface ModelElementEmissiveAccessor {
	boolean isEmissive();
	void setEmissive(boolean set);

	ModelElement getActual();

	void storeActual(ModelElement actual);
}
