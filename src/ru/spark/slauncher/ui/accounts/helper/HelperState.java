package ru.spark.slauncher.ui.accounts.helper;

import ru.spark.slauncher.ui.loc.LocalizableMenuItem;

public enum HelperState {
	PREMIUM, FREE, HELP(false), NONE;

	public final LocalizableMenuItem item;
	public final boolean showInList;

	HelperState() {
		this(true);
	}

	HelperState(boolean showInList) {
		this.item = new LocalizableMenuItem("auth.helper." + toString());
		this.showInList = showInList;
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}
