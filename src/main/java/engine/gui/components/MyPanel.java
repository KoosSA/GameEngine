package engine.gui.components;

import com.spinyowl.legui.component.Panel;
import com.spinyowl.legui.style.Style.DisplayType;
import com.spinyowl.legui.style.Style.PositionType;
import com.spinyowl.legui.style.flex.FlexStyle.AlignSelf;

public class MyPanel extends Panel {

	private static final long serialVersionUID = -8348830407142475011L;

	public MyPanel() {
		getStyle().setDisplay(DisplayType.FLEX);
		getStyle().setPosition(PositionType.RELATIVE);
		getStyle().getFlexStyle().setFlexGrow(1);
		getStyle().getFlexStyle().setFlexShrink(1);
		getStyle().getFlexStyle().setAlignSelf(AlignSelf.STRETCH);
	}

}
