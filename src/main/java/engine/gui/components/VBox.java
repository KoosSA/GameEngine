package engine.gui.components;

import com.spinyowl.legui.component.Panel;
import com.spinyowl.legui.style.Style.DisplayType;
import com.spinyowl.legui.style.Style.PositionType;
import com.spinyowl.legui.style.flex.FlexStyle.FlexDirection;

public class VBox extends Panel {

	private static final long serialVersionUID = -8188808846945631908L;

	public VBox() {
		getStyle().setDisplay(DisplayType.FLEX);
		getStyle().setPosition(PositionType.RELATIVE);
		getStyle().getFlexStyle().setFlexDirection(FlexDirection.COLUMN);
		getStyle().getFlexStyle().setFlexGrow(1);
		getStyle().getFlexStyle().setFlexShrink(1);
	}

}
