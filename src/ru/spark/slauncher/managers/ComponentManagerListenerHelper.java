package ru.spark.slauncher.managers;

import ru.spark.slauncher.component.LauncherComponent;
import ru.spark.slauncher.ui.block.Blockable;
import ru.spark.slauncher.ui.block.Blocker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentManagerListenerHelper extends LauncherComponent implements
		Blockable, VersionManagerListener {
	private final List<ComponentManagerListener> listeners;

	public ComponentManagerListenerHelper(ComponentManager manager)
			throws Exception {
		super(manager);

		this.listeners = Collections
				.synchronizedList(new ArrayList<ComponentManagerListener>());

		manager.getComponent(VersionManager.class).addListener(this);
	}

	public void addListener(ComponentManagerListener listener) {
		if (listener == null)
			throw new NullPointerException();

		this.listeners.add(listener);
	}

	@Override
	public void onVersionsRefreshing(VersionManager manager) {
		Blocker.block(this, manager);
	}

	@Override
	public void onVersionsRefreshingFailed(VersionManager manager) {
		Blocker.unblock(this, manager);
	}

	@Override
	public void onVersionsRefreshed(VersionManager manager) {
		Blocker.unblock(this, manager);
	}

	//

	@Override
	public void block(Object reason) {
		for (ComponentManagerListener listener : listeners)
			listener.onComponentsRefreshing(manager);
	}

	@Override
	public void unblock(Object reason) {
		for (ComponentManagerListener listener : listeners)
			listener.onComponentsRefreshed(manager);
	}

}
