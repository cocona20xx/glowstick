package cocona20xx.retribution.impl;

import cocona20xx.retribution.RetributionModClient;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.resource.loader.api.reloader.SimpleSynchronousResourceReloader;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

public class EmissiveDataReloader implements SimpleSynchronousResourceReloader {
	private static final Object2ObjectMap<String, ParsedItemFlagData> EMISSIVE_MAP = new Object2ObjectArrayMap<>();
	private static final Gson GSON = new Gson();
	private static final String[] PARENT_BLACKLIST = {"template_banner", "template_bed", "template_shulker_box", "template_skull", "template_spawn_egg", "trident_in_hand", "spyglass_in_hand", "block"};
	private static final ArrayList<String> BLACKLIST_LIST = Lists.newArrayList(PARENT_BLACKLIST);
	public static final Identifier RELOADER_ID = new Identifier("retribution", "emissive");
	private static final String[] flags = {"layer0", "layer1", "layer2", "layer3", "layer4"};

	@Override
	public void reload(ResourceManager manager) {
		RetributionModClient.LOGGER.info("Starting Emissive Load...");
		EMISSIVE_MAP.clear();
		for(Identifier id : manager.findResources("models/item", path -> path.toString().endsWith(".json")).keySet()){
			try {
				for(Resource r : manager.getAllResources(id)){
					try (BufferedReader reader = r.openBufferedReader()){
						JsonObject mainObject = GSON.fromJson(reader, JsonObject.class);
						if(mainObject.has("emissive_flags")){
							if(!BLACKLIST_LIST.contains(mainObject.get("parent").getAsString())){
								JsonObject flagObject = mainObject.getAsJsonObject("emissive_flags");
								JsonObject textureObject = mainObject.getAsJsonObject("textures");
								for (int i = 0; i < 4; i++) {
									if (flagObject.has(flags[i])) {
										JsonPrimitive flagPrim = flagObject.getAsJsonPrimitive(flags[i]);
										if (flagPrim.isBoolean()) {
											if (flagPrim.getAsBoolean()) {
												if (!textureObject.has(flags[i])) {
													throw new JsonParseException("Flag set for layer but layer is not present");
												} else {
													String texSimple = simplifyFromIdString(textureObject.get(flags[i]).getAsString());
													ParsedItemFlagData data = new ParsedItemFlagData();
													data.setFlag(i, texSimple);
													EMISSIVE_MAP.put(texSimple, data);
												}
											}
										} else throw new JsonParseException("Flag set to non-boolean value");
									}
								}
							}
						}
					} catch (Exception e){
						RetributionModClient.LOGGER.warn("Error occurred when trying to parse emissive data: " + e);
					}
				}
			} catch (Exception e){
				RetributionModClient.LOGGER.warn("IO Error when trying to parse emissive data: "  + e);
			}
		}
	}

	@Override
	public @NotNull Identifier getQuiltId() {
		return RELOADER_ID;
	}

	private static void add(String simplifiedId, ParsedItemFlagData data){
		if(EMISSIVE_MAP.containsKey(simplifiedId)) RetributionModClient.LOGGER.warn("Emissive map key overlap detected: "+ simplifiedId);
		EMISSIVE_MAP.put(simplifiedId, data);
	}
	public static ParsedItemFlagData getData(String simplifiedId){
		return EMISSIVE_MAP.get(simplifiedId);
	}
	public static boolean hasDataForSimple(String simplifiedId){
		return EMISSIVE_MAP.containsKey(simplifiedId);
	}
	public static String simplifyFromId(Identifier id){
		String buildSimple = "";
		buildSimple = buildSimple.concat(id.getNamespace()).concat("#");
		String[] slashSeparatedPath = id.getPath().split(Pattern.quote("/"));
		int s = slashSeparatedPath.length - 1;
		return buildSimple.concat(slashSeparatedPath[s]);
	}
	private static String simplifyFromIdString(String idAsString){
		if(idAsString.contains(":")) {
			//string contains a namespace, we can parse it like an Identifier object
			String buildSimple = idAsString.split(Pattern.quote(":"))[0].concat("#");
			String[] slashSeparatedPath = idAsString.split(Pattern.quote(":"))[1].split("/");
			int s = slashSeparatedPath.length - 1;
			return buildSimple.concat(slashSeparatedPath[s]);
		} else {
			//no namespace means implicit minecraft namespace
			String buildSimple = idAsString.replaceFirst(Pattern.quote("item/"),"");
			buildSimple = "minecraft#" + buildSimple;
			return buildSimple;
		}
	}
	public static int flagStringToInt(String flag){
		for(int i = 0; i < 4; i++){
			if(Objects.equals(flag, flags[i])) return i;
		}
		return -1;
	}
}
