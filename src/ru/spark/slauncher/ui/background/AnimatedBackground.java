package ru.spark.slauncher.ui.background;

import java.awt.*;

public abstract class AnimatedBackground extends Background {
	private static final long serialVersionUID = -7203733710324519015L;

	public AnimatedBackground(BackgroundHolder holder, Color coverColor) {
		super(holder, coverColor);
	}

	public abstract void startBackground();

	public abstract void stopBackground();

	public abstract void suspendBackground();
}
