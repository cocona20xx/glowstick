package cocona20xx.retribution.impl;

import java.util.Objects;

public class ParsedItemFlagData {
	private String layer0 = null;
	private String layer1 = null;
	private String layer2 = null;
	private String layer3 = null;
	private String layer4 = null;

	public boolean hasFlag(int id){
		return switch (id){
			case 0 -> !Objects.isNull(layer0);
			case 1 -> !Objects.isNull(layer1);
			case 2 -> !Objects.isNull(layer2);
			case 3 -> !Objects.isNull(layer3);
			case 4 -> !Objects.isNull(layer4);
			default -> false;
		};
	}
	public boolean hasAny(){
		return hasFlag(0) || hasFlag(1) || hasFlag(2) || hasFlag(3) ||hasFlag(4);
	}
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
}
