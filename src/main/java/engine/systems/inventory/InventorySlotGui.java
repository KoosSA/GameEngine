package engine.systems.inventory;

import com.koossa.filesystem.Files;
import com.spinyowl.legui.component.Button;
import com.spinyowl.legui.component.ImageView;
import com.spinyowl.legui.component.optional.align.HorizontalAlign;
import com.spinyowl.legui.component.optional.align.VerticalAlign;
import com.spinyowl.legui.image.loader.ImageLoader;
import com.spinyowl.legui.style.Style.DisplayType;
import com.spinyowl.legui.style.Style.PositionType;
import com.spinyowl.legui.style.color.ColorConstants;
import com.spinyowl.legui.style.shadow.Shadow;

public class InventorySlotGui extends Button {

	private static final long serialVersionUID = -3215896522232151896L;
	private ImageView image;

	public InventorySlotGui() {
		getStyle().setShadow(new Shadow(2, 2, 1, 1, ColorConstants.black()));
		getStyle().setDisplay(DisplayType.FLEX);
		getStyle().setPosition(PositionType.RELATIVE);
		getStyle().getFlexStyle().setFlexGrow(1);
		getStyle().setMaximumSize(50, 50);
		getStyle().setMinimumSize(50, 50);
		getStyle().setMargin(5);
		getStyle().setHorizontalAlign(HorizontalAlign.RIGHT);
		getStyle().setVerticalAlign(VerticalAlign.BOTTOM);
		image = new ImageView();
		image.getStyle().setDisplay(DisplayType.FLEX);
		image.getStyle().setPosition(PositionType.RELATIVE);
		image.getStyle().getFlexStyle().setFlexGrow(1);
		image.getStyle().setMarginBottom(15f);
		image.getStyle().setMarginTop(5f);
		image.getStyle().setMarginLeft(10f);
		image.getStyle().setMarginRight(10f);
		image.setEnabled(false);
		image.setFocusable(false);
		getTextState().setText("");

	}

	public void updateSlot(InventorySlotData data) {
		if (data.getAmountOnStack() > 0) {
			image.setImage(ImageLoader.loadImage(Files.getFolderPath("GuiIcons") + "/" + data.getItem().getItemTexture()));
			add(image);
			getTextState().setText("x " + data.getAmountOnStack());
		} else {
			remove(image);
			getTextState().setText("");
		}
	}





}
