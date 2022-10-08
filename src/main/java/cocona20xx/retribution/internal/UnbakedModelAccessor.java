package cocona20xx.retribution.internal;

import cocona20xx.retribution.impl.ParsedItemFlagData;
import cocona20xx.retribution.mixins.JsonUnbakedModelMixin;
import net.minecraft.client.render.model.json.JsonUnbakedModel;

public interface UnbakedModelAccessor {
	void setData(ParsedItemFlagData data);
	void storeActual(JsonUnbakedModel model);
	JsonUnbakedModel getActual();
}
