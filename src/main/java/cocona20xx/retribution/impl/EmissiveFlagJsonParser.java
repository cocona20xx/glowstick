package cocona20xx.retribution.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class EmissiveFlagJsonParser {
	private static final Gson GSON = new Gson();
	private static final String[] flags = {"layer0", "layer1", "layer2", "layer3", "layer4"};
	public static ParsedItemFlagData parse(JsonObject object) throws JsonParseException {
		ParsedItemFlagData data = new ParsedItemFlagData();
		if (object.has("emissive_flags")) {
			JsonObject flagObject = object.getAsJsonObject("emissive_flags");
			JsonObject textureObject = object.getAsJsonObject("textures");
			for (int i = 0; i < 4; i++) {
				if (flagObject.has(flags[i])) {
					JsonPrimitive flagPrim = flagObject.getAsJsonPrimitive(flags[i]);
					if (flagPrim.isBoolean()) {
						if (flagPrim.getAsBoolean()) {
							if (!textureObject.has(flags[i]))
								throw new JsonParseException("Flag set for layer but layer is not present");
							String texVal = textureObject.get(flags[i]).getAsString();
							data.setFlag(i, texVal);
						} //value is simply ignored if false. might change this to an array of strings containing the layer name later?
					} else throw new JsonParseException("Flag set to non-boolean value");
				}
			}
		}
		return data;
	}

}
