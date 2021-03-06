package ru.spark.slauncher.ui.swing;

import net.minecraft.launcher.updater.LatestVersionSyncInfo;
import net.minecraft.launcher.updater.VersionSyncInfo;
import net.minecraft.launcher.versions.ReleaseType;
import ru.spark.slauncher.ui.loc.Localizable;
import ru.spark.util.U;

import javax.swing.*;
import java.awt.*;

public class VersionCellRenderer implements ListCellRenderer<VersionSyncInfo> {
	// Pseudo elements
	public static final VersionSyncInfo LOADING = VersionSyncInfo.createEmpty(),
			EMPTY = VersionSyncInfo.createEmpty();

	private final DefaultListCellRenderer defaultRenderer;
	private final int averageColor;

	public VersionCellRenderer() {
		this.defaultRenderer = new DefaultListCellRenderer();
		this.averageColor = new Color(128, 128, 128, 255).getRGB();
	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends VersionSyncInfo> list, VersionSyncInfo value,
			int index, boolean isSelected, boolean cellHasFocus) {
		// Source:
		// http://www.java2s.com/Tutorial/Java/0240__Swing/Comboboxcellrenderer.htm
		JLabel
			mainText = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		mainText.setAlignmentY(Component.CENTER_ALIGNMENT);

		if (value == null)
			mainText.setText("(null)");

		else if (value.equals(LOADING))
			mainText.setText(Localizable.get("versions.loading"));

		else if (value.equals(EMPTY))
			mainText.setText(Localizable.get("versions.notfound.tip"));

		else {
			mainText.setText(getLabelFor(value));
			if (!value.isInstalled())
				mainText.setBackground(U.shiftColor(mainText.getBackground(), mainText.getBackground().getRGB() < averageColor ? 32 : -32));
		}
		
		return mainText;
	}
	
	public static String getLabelFor(VersionSyncInfo value) {
		LatestVersionSyncInfo asLatest = (value instanceof LatestVersionSyncInfo) ? (LatestVersionSyncInfo) value
				: null;

		ReleaseType type = value.getAvailableVersion().getReleaseType();
		String id, label;
		
		if(asLatest == null) {
			id = value.getID();
			label = "version." + type;
		} else {
			id = asLatest.getVersionID();
			label = "version.latest." + type;
		}
		
		label = Localizable.nget(label);

		switch (type) {
		case OLD_ALPHA:
			id = (id.startsWith("a")) ? id.substring(1) : id;
			break;
		case OLD_BETA:
			id = id.substring(1);
			break;
		default:
			break;
		}

		String text = label != null ? label + " " + id : id;
		
		return text;
	}

}
