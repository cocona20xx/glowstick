package cocona20xx.retribution.mixins;

import cocona20xx.retribution.internal.ModelElementEmissiveAccessor;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ModelElement.class)
public class ModelElementMixin implements ModelElementEmissiveAccessor {
	@Unique private boolean emissive = false;
	@Unique private ModelElement actual;
	@Override
	public boolean isEmissive() {
		return emissive;
	}

	@Override
	public void setEmissive(boolean set) {
		this.emissive = set;
	}

	@Override
	public ModelElement getActual() {
		return actual;
	}

	@Override
	public void storeActual(ModelElement actual) {
		this.actual = actual;
	}
}
