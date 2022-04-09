package engine.systems.inventory;

import com.koossa.savelib.ISavable;

public class InventorySettings implements ISavable {

	private boolean weightLimit = true;
	private boolean sizeLimit = true;

	private int maxNumberOfSlots = 24;

	private float maxWeight = 100.0f;

	public boolean isWeightLimit() {
		return weightLimit;
	}

	public boolean isSizeLimit() {
		return sizeLimit;
	}

	public int getMaxNumberOfSlots() {
		return maxNumberOfSlots;
	}

	public float getMaxWeight() {
		return maxWeight;
	}




}
