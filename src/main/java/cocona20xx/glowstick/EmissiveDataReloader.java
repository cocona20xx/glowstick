/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package cocona20xx.glowstick;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.resource.loader.api.reloader.SimpleSynchronousResourceReloader;

import java.io.BufferedReader;
import java.util.*;
import java.util.regex.Pattern;

public class EmissiveDataReloader implements SimpleSynchronousResourceReloader {
	private static final ArrayListMultimap<Identifier, Identifier> EMISSIVE_MAP = ArrayListMultimap.create();
	private static final Gson GSON = new Gson();
	private static final String[] PARENT_BLACKLIST = {"template_banner", "template_bed", "template_shulker_box", "template_skull", "template_spawn_egg", "trident_in_hand", "spyglass_in_hand", "block"};
	private static final ArrayList<String> BLACKLIST_LIST = Lists.newArrayList(PARENT_BLACKLIST);
	public static final Identifier RELOADER_ID = new Identifier(GlowstickModClient.MOD_ID, "emissive");
	public static final String ET_MEMBER_NAME = "emissive_textures";
	private static final String[] flags = {"layer0", "layer1", "layer2", "layer3", "layer4"};

	@Override
	public void reload(ResourceManager manager) {
		GlowstickModClient.LOGGER.info("Starting Emissive Load...");
		EMISSIVE_MAP.clear();
		for(Identifier modelId : manager.findResources("models/item", path -> path.toString().endsWith(".json")).keySet()){
			try {
				for(Resource r : manager.getAllResources(modelId)){
					try (BufferedReader reader = r.openBufferedReader()){
						JsonObject mainObject = GSON.fromJson(reader, JsonObject.class);
						if(mainObject.has(ET_MEMBER_NAME)){
							if(!BLACKLIST_LIST.contains(mainObject.get("parent").getAsString())){
								JsonArray etArrayJson = mainObject.getAsJsonArray(ET_MEMBER_NAME);
								int etLen = etArrayJson.size();
								for (int i = 0; i < etLen; i++) {
									JsonPrimitive current = etArrayJson.get(i).getAsJsonPrimitive();
									if(current.isString()){
										//emissive texture strings are not checked against the model as overrides can refer to other models, and this (should) hopefully let those textures also work? needs testing ofc (TODO)
										Identifier sanitizedModelId = sanitizeValidId(modelId);
										Identifier sanitizedTextureId = convertIdStringToSanitized(current.getAsString());
										putResult(sanitizedTextureId, sanitizedModelId);
									} else {
										throw new Exception("Emissive Textures array on item model " + modelId.toString() + " contains non-string value at index " + i +",skipping.");
									}
								}
							}
						}
					} catch (Exception e){
						GlowstickModClient.LOGGER.warn("Error occurred when trying to parse emissive data: " + e);
					}
				}
			} catch (Exception e){
				GlowstickModClient.LOGGER.warn("IO Error when trying to parse emissive data: "  + e);
			}
		}
	}

	@Override
	public @NotNull Identifier getQuiltId() {
		return RELOADER_ID;
	}

	private static void putResult(Identifier sanitizedTexId, Identifier sanitizedModelId){
		EMISSIVE_MAP.put(sanitizedTexId, sanitizedModelId);
	}

	public static boolean checkValidity(Identifier sanitizedTexId, Identifier sanitizedModelId){
		//you are valid! the model id, however, might not be
		boolean validity = false;
		List<Identifier> models = EMISSIVE_MAP.get(sanitizedTexId);
		for (Identifier model : models) {
			if (model.equals(sanitizedModelId)) {
				validity = true;
				break;
			}
		}
		return validity;
	}

	public static Identifier sanitizeValidId(Identifier toSanitize){
		String path = toSanitize.getPath();
		if(path.contains("#")){
			path = path.split(Pattern.quote("#"))[0];
		}
		path = removeLastIfValid(path, ".json");
		path = removeLastIfValid(path, ".png");
		path = removeFirstIfValid(path, "models/");
		path = removeFirstIfValid(path, "textures/");
		path = removeFirstIfValid(path, "item");
		return new Identifier(toSanitize.getNamespace(), path);
	}

	public static Identifier convertIdStringToSanitized(String idString){
		if(idString.contains(":")){
			//id has defined namespace
			String[] s = idString.split(Pattern.quote(":"));
			return sanitizeValidId(new Identifier(s[0], s[1]));
		} else {
			//implicit minecraft namespace
			return sanitizeValidId(new Identifier("minecraft", idString));
		}
	}

	private static String removeLastIfValid(String current, String last){
		if(current.endsWith(last)){
			int l = current.lastIndexOf(last);
			return current.substring(0, l);
		} else return current;
	}
	private static String removeFirstIfValid(String current, String first){
		if(current.startsWith(first)){
			return current.replaceFirst(Pattern.quote(first), "");
		} else return current;
	}
}
