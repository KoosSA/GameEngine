package engine.systems.inventory;

import engine.systems.general.Item;

public class InventorySlotData {

	private Item item = null;
	private int amountOnStack = 0;

	public int setData(Item item, int amount) {
		if (this.item == item || amountOnStack <= 0 || this.item == null) {
			this.item = item;
			return setAmount(amount);
		}
		return 0;
	}

	public boolean removeItem(int amount) {
		if (amountOnStack >= amount && item != null) {
			amountOnStack = amountOnStack - amount;
			validateSlot();
			return true;
		}
		return false;
	}

	private void validateSlot() {
		if (amountOnStack <= 0) {
			amountOnStack = 0;
			item = null;
		}
	}

	private int setAmount(int amount) {
		if (amount + this.amountOnStack > item.getMaxOnStack()) {
			this.amountOnStack = item.getMaxOnStack();
			return amount - item.getMaxOnStack();
		}
		this.amountOnStack += amount;
		return 0;
	}

	public int getAmountOnStack() {
		return amountOnStack;
	}

	public Item getItem() {
		return item;
	}
}
