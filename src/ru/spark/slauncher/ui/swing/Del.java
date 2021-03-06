package ru.spark.slauncher.ui.swing;

import ru.spark.slauncher.ui.swing.extended.ExtendedPanel;

import java.awt.*;

public class Del extends ExtendedPanel {
	private static final int TOP = -1;
	public static final int CENTER = 0;
	private static final int BOTTOM = 1;

	private static final long serialVersionUID = -2252007533187803762L;

	private int size;
	private int aligment;
	private Color color;

	public Del(int size, int aligment, Color color) {
		this.size = size;
		this.aligment = aligment;
		this.color = color;
	}

	public Del(int size, int aligment, int width, int height, Color color) {
		this.size = size;
		this.aligment = aligment;
		this.color = color;

		this.setPreferredSize(new Dimension(width, height));
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(color);
		switch (aligment) {
		case TOP:
			g.fillRect(0, 0, getWidth(), size);
			break;
		case CENTER:
			g.fillRect(0, getHeight() / 2 - size, getWidth(), size);
			break;
		case BOTTOM:
			g.fillRect(0, getHeight() - size, getWidth(), size);
		}
	}

}
