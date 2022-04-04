package engine.gui.components;

import com.spinyowl.legui.component.Panel;
import com.spinyowl.legui.style.Style.DisplayType;
import com.spinyowl.legui.style.Style.PositionType;
import com.spinyowl.legui.style.flex.FlexStyle.AlignItems;
import com.spinyowl.legui.style.flex.FlexStyle.FlexDirection;

public class HBox extends Panel {

	private static final long serialVersionUID = -8188808846945631908L;

	public HBox() {
		this.getStyle().setDisplay(DisplayType.FLEX);
		this.getStyle().setPosition(PositionType.RELATIVE);
		this.getStyle().getFlexStyle().setFlexDirection(FlexDirection.ROW);
		this.getStyle().getFlexStyle().setAlignItems(AlignItems.CENTER);
		this.getStyle().getFlexStyle().setFlexGrow(1);
		this.getStyle().getFlexStyle().setFlexShrink(1);

	}

}
