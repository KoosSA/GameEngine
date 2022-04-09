package engine.systems.inventory;

import java.util.ArrayList;
import java.util.List;

import com.koossa.savelib.SaveSystem;
import com.spinyowl.legui.component.Frame;

import engine.io.GameInput;
import engine.io.IInputHandler;
import engine.io.KeyBindings;
import engine.systems.general.Item;

public class Inventory implements IInputHandler {

	private InventorySettings settings;
	private InventoryGui gui;
	private List<InventorySlotData> slotData;

	public Inventory(Frame frame) {
		registerInputHandler();
		settings = SaveSystem.load(InventorySettings.class, true, "Systems/Inventory", "InventorySettings.json");
		slotData = new ArrayList<InventorySlotData>();
		gui = new InventoryGui(this, 600, 400, frame);

		for (int i = 0; i < getSettings().getMaxNumberOfSlots(); i++) {
			slotData.add(new InventorySlotData());
			gui.addSlot();
		}
	}

	public InventorySettings getSettings() {
		return settings;
	}

	public int addItem(Item item, int amount) {
		List<Integer> indices = getSlotWithSpaceIndex(item);
		if (indices.size() <= 0) return amount;
		int pointer = 0;
		while (amount > 0 && indices.size() > 0) {
			amount = slotData.get(indices.get(pointer)).setData(item, amount);
			pointer++;
		}
		return amount;
	}

	public List<InventorySlotData> getSlotData() {
		return slotData;
	}




	private List<Integer> getSlotIndex(Item item) {
		List<Integer> tempIntList = new ArrayList<Integer>();
		for (int i = 0; i<slotData.size(); i++)
			if ((slotData.get(i).getItem() == item || slotData.get(i).getItem() == null)) tempIntList.add(i);
		return tempIntList;
	}

	private List<Integer> getSlotWithSpaceIndex(Item item) {
		List<Integer> tempIntList = new ArrayList<Integer>();
		for (int i = 0; i<slotData.size(); i++)
			if ((slotData.get(i).getItem() == item || slotData.get(i).getItem() == null) && hasSpace(item, i)) tempIntList.add(i);
		return tempIntList;
	}

	private boolean hasSpace(Item item, int index) {
		return slotData.get(index).getAmountOnStack() < item.getMaxOnStack();
	}

	@Override
	public void handleInput(float delta, GameInput input) {
		if (input.isKeyJustPressed(KeyBindings.INVENTORY)) gui.toggle();
	}

}
