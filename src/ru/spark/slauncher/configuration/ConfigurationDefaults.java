package ru.spark.slauncher.configuration;

import net.minecraft.launcher.versions.ReleaseType;
import ru.spark.slauncher.configuration.Configuration.ActionOnLaunch;
import ru.spark.slauncher.configuration.Configuration.ConnectionQuality;
import ru.spark.slauncher.configuration.Configuration.ConsoleType;
import ru.spark.slauncher.ui.login.AutoLogin;
import ru.spark.util.Direction;
import ru.spark.util.IntegerArray;
import ru.spark.util.MinecraftUtil;
import ru.spark.util.OS;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class ConfigurationDefaults {
	private static final int version = 3;
	private final Map<String, Object> d; // defaults

	ConfigurationDefaults() {
		d = new HashMap<String, Object>();

		d.put("settings.version", version);

		d.put("login.auto", false);
		d.put("login.auto.timeout", AutoLogin.DEFAULT_TIMEOUT);

		d.put("minecraft.gamedir", MinecraftUtil.getDefaultWorkingDirectory()
				.getAbsolutePath());
		d.put("minecraft.size", new IntegerArray(925, 530));
		d.put("minecraft.fullscreen", false);

		for (ReleaseType type : ReleaseType.getDefinable())
			d.put("minecraft.versions." + type, true);

		d.put("minecraft.memory", OS.Arch.PREFERRED_MEMORY);

		d.put("minecraft.onlaunch", ActionOnLaunch.getDefault());

		d.put("gui.size", new IntegerArray(925, 530));
		d.put("gui.console", ConsoleType.getDefault());
		d.put("gui.console.width", 720);
		d.put("gui.console.height", 500);
		d.put("gui.console.x", 30);
		d.put("gui.console.y", 30);

		d.put("gui.direction.loginform", Direction.CENTER);

		d.put("connection", ConnectionQuality.getDefault());
	}

	public static int getVersion() {
		return version;
	}

	public Map<String, Object> getMap() {
		return Collections.unmodifiableMap(d);
	}

	public Object get(String key) {
		return d.get(key);
	}
}
