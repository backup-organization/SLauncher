package ru.spark.slauncher.ui.swing.extended;

import ru.spark.util.Reflect;

import javax.swing.*;
import java.awt.*;

public class VPanel extends ExtendedPanel {
	private static final long serialVersionUID = -7956156442842177101L;

	private VPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	public VPanel() {
		this(true);
	}

	@Override
	public BoxLayout getLayout() {
		return (BoxLayout) super.getLayout();
	}

	@Override
	public void setLayout(LayoutManager mgr) {
		if (!(mgr instanceof BoxLayout))
			return;

		int axis = Reflect.cast(mgr, BoxLayout.class).getAxis();

		if (axis == BoxLayout.PAGE_AXIS || axis == BoxLayout.Y_AXIS)
			super.setLayout(mgr);
		else
			throw new IllegalArgumentException("Illegal BoxLayout axis!");
	}

}
