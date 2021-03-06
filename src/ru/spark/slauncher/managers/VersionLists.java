package ru.spark.slauncher.managers;

import net.minecraft.launcher.updater.ExtraVersionList;
import net.minecraft.launcher.updater.LocalVersionList;
import net.minecraft.launcher.updater.OfficialVersionList;
import net.minecraft.launcher.updater.RemoteVersionList;
import ru.spark.slauncher.component.LauncherComponent;
import ru.spark.util.MinecraftUtil;

import java.io.IOException;

public class VersionLists extends LauncherComponent {
	private final LocalVersionList localList;

	private final RemoteVersionList[] remoteLists;

	public VersionLists(ComponentManager manager) throws Exception {
		super(manager);

		this.localList = new LocalVersionList(
				MinecraftUtil.getWorkingDirectory());

		OfficialVersionList officialList = new OfficialVersionList();
		ExtraVersionList extraList = new ExtraVersionList();

		this.remoteLists = new RemoteVersionList[] { officialList, extraList };
	}

	public LocalVersionList getLocal() {
		return localList;
	}

	public void updateLocal() throws IOException {
		this.localList.setBaseDirectory(MinecraftUtil.getWorkingDirectory());
	}

	public RemoteVersionList[] getRemoteLists() {
		return remoteLists;
	}
}
