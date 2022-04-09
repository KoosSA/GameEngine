package engine.gui.components;

import com.spinyowl.legui.component.Label;
import com.spinyowl.legui.style.Style.DisplayType;
import com.spinyowl.legui.style.Style.PositionType;
import com.spinyowl.legui.style.flex.FlexStyle.AlignSelf;

public class MyLabel extends Label {

	private static final long serialVersionUID = 5373440155233627478L;

	public MyLabel(String text) {
		super(text);
		getStyle().setDisplay(DisplayType.FLEX);
		getStyle().setPosition(PositionType.RELATIVE);
		getStyle().getFlexStyle().setFlexGrow(1);
		getStyle().getFlexStyle().setFlexShrink(1);
		getStyle().getFlexStyle().setAlignSelf(AlignSelf.STRETCH);
	}

}
