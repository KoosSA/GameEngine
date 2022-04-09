package engine.systems.general;

import com.koossa.savelib.ISavable;

public class Item implements ISavable {

	private boolean usable = true;

	private String useText = "Use";
	private String description = "This is an usable item.";
	private String itemTexture = "3.png";

	private int maxOnStack = 10;

	public boolean isUsable() {
		return usable;
	}

	public String getUseText() {
		return useText;
	}

	public String getDescription() {
		return description;
	}

	public int getMaxOnStack() {
		return maxOnStack;
	}

	public String getItemTexture() {
		return itemTexture;
	}

}
