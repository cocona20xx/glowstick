package cocona20xx.retribution.impl;

import java.util.Objects;

public class ParsedItemFlagData {
	private String layer0 = null;
	private String layer1 = null;
	private String layer2 = null;
	private String layer3 = null;
	private String layer4 = null;

	public void setFlag(int id, String val){
		switch (id){
			case 0 -> layer0 = val;
			case 1 -> layer1 = val;
			case 2 -> layer2 = val;
			case 3 -> layer3 = val;
			case 4 -> layer4 = val;
		}
	}

	public String getId(int id){
		return switch (id){
			case 0 -> layer0;
			case 1 -> layer1;
			case 2 -> layer2;
			case 3 -> layer3;
			case 4 -> layer4;
			default -> null;
		};
	}
	public boolean hasAny(){
		return Objects.nonNull(layer0) || Objects.nonNull(layer1) || Objects.nonNull(layer2) || Objects.nonNull(layer3) || Objects.nonNull(layer4);
	}
	public boolean hasId(int id){
		return switch (id){
			case 0 -> Objects.nonNull(layer0);
			case 1 -> Objects.nonNull(layer1);
			case 2 -> Objects.nonNull(layer2);
			case 3 -> Objects.nonNull(layer3);
			case 4 -> Objects.nonNull(layer4);
			default -> false;
		};
	}
}
