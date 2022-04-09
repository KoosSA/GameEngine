package engine.systems.inventory;

import java.util.ArrayList;
import java.util.List;

import com.spinyowl.legui.component.Frame;
import com.spinyowl.legui.component.optional.align.HorizontalAlign;
import com.spinyowl.legui.style.border.SimpleLineBorder;
import com.spinyowl.legui.style.color.ColorConstants;
import com.spinyowl.legui.style.flex.FlexStyle.AlignItems;
import com.spinyowl.legui.style.flex.FlexStyle.FlexDirection;
import com.spinyowl.legui.style.flex.FlexStyle.FlexWrap;

import engine.gui.components.MyLabel;
import engine.gui.components.MyPanel;
import engine.gui.components.VBox;
import engine.settings.WindowSettings;

public class InventoryGui extends VBox {

	private static final long serialVersionUID = -4940655724025841214L;
	private Inventory inventory;
	private MyPanel north, center, south;
	private List<InventorySlotGui> slotGuis;
	private boolean visible = false;
	private Frame frame;


	public InventoryGui(Inventory inventory, float width, float height, Frame frame) {
		this.frame = frame;
		this.inventory = inventory;
		slotGuis = new ArrayList<InventorySlotGui>();
		setSize(width, height);
		setPosition(WindowSettings.width / 2 - width / 2, WindowSettings.height / 2 - height / 2);
		setEnabled(false);
		getStyle().setBorderRadius(5);
		createGui();
	}

	private void createGui() {
		createMainPanel();
		updateSlots();
	}

	public void addSlot() {
		InventorySlotGui slotGui = new InventorySlotGui();
		slotGuis.add(slotGui);
		center.add(slotGui);
	}

	private void updateSlots() {
		for (int i = 0; i < inventory.getSlotData().size(); i++) {
			InventorySlotData data = inventory.getSlotData().get(i);
			if (data == null) break;
			slotGuis.get(i).updateSlot(data);
		}
	}

	private void createMainPanel() {
		north = new MyPanel();
		center = new MyPanel();
		south = new MyPanel();
		north.getStyle().getBackground().setColor(ColorConstants.black());
		center.getStyle().getBackground().setColor(ColorConstants.gray());
		center.getStyle().getFlexStyle().setFlexGrow(10);
		center.getStyle().getFlexStyle().setFlexFlow(FlexDirection.ROW, FlexWrap.WRAP);
		center.getStyle().setPadding(5);
		south.getStyle().getBackground().setColor(ColorConstants.lightBlack());
		add(north);
		add(center);
		add(south);
		getStyle().setBorder(new SimpleLineBorder(ColorConstants.black(), 5));
		MyLabel title = new MyLabel("Inventory");
		north.add(title);
		north.getStyle().getFlexStyle().setAlignItems(AlignItems.CENTER);
		title.getStyle().setHorizontalAlign(HorizontalAlign.CENTER);
		title.getStyle().setFontSize(15f);
		title.getStyle().setTextColor(ColorConstants.white());
		title.getStyle().setHighlightColor(ColorConstants.lightGray());
	}

	public void toggle() {
		if (visible) {
			hide();
			visible = false;
		} else {
			show();
			visible = true;
		}
	}

	private void show() {
		updateSlots();
		frame.getContainer().add(this);
	}

	private void hide() {
		frame.getContainer().remove(this);
	}

}
