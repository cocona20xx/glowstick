package cocona20xx.retribution.mixins;

import cocona20xx.retribution.RetributionModClient;
import cocona20xx.retribution.impl.EmissiveFlagJsonParser;
import cocona20xx.retribution.impl.ParsedItemFlagData;
import cocona20xx.retribution.internal.UnbakedModelAccessor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.BufferedReader;
import java.util.regex.Pattern;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	@Unique private static final Gson GSON = new Gson();
	@Unique private final ResourceManager manager = MinecraftClient.getInstance().getResourceManager();

	private static final String[] PARENT_BLACKLIST = {"template_banner", "template_bed", "template_shulker_box", "template_skull", "template_spawn_egg", "trident_in_hand", "spyglass_in_hand", "block"};

	@Inject(method = "loadModelFromJson", at = @At(value = "RETURN"), cancellable = true)
	public void loadModelInjector(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
		JsonUnbakedModel toReturn = cir.getReturnValue();
		if (id.toString().contains(":item/")) {
			try {
				for(Identifier id2 : manager.findResources(idResolver(id), path -> path.toString().endsWith(".json")).keySet()){
					ParsedItemFlagData data = new ParsedItemFlagData();
					for(Resource r : manager.getAllResources(id2)){
						BufferedReader reader = r.openBufferedReader();
						JsonObject object = GSON.fromJson(reader, JsonObject.class);
						if(object.has("parent") && !idBlacklist(id2)){
							if (object.has("emissive_flags")) {
								try {
									data = EmissiveFlagJsonParser.parse(object);
								} catch (Exception e) {
									RetributionModClient.LOGGER.info(e.toString());
								}
								if (data.hasAny()) {
									UnbakedModelAccessor accessor = (UnbakedModelAccessor) toReturn;
									accessor.storeActual(toReturn);
									accessor.setData(data);
									toReturn = accessor.getActual();
								}
							}
						}

					}
				}
			} catch (Exception e) {
				cir.setReturnValue(toReturn);
				throw new RuntimeException(e);
			}
			cir.setReturnValue(toReturn);
		}
	}
	private String idResolver(Identifier id){
		String fileName = id.getPath().replaceFirst(Pattern.quote("item/"), "");
		return "models/item/".concat(fileName);
	}
	private boolean idBlacklist(Identifier toCheck){
		for(String s : ModelLoaderMixin.PARENT_BLACKLIST){
			if(toCheck.getPath().contains(s)) return true;
			break;
		}
		return false;
	}
}
