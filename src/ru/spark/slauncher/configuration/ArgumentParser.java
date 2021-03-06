package ru.spark.slauncher.configuration;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import ru.spark.slauncher.ui.alert.Alert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ArgumentParser {
	private static Map<String, String> m = createLinkMap();
	private static OptionParser parser = createParser();

	public static OptionParser getParser() {
		return parser;
	}

	public static OptionSet parseArgs(String[] args) throws IOException {
		OptionSet set = null;
		try {
			set = parser.parse(args);
		} catch (OptionException e) {
			e.printStackTrace();
			parser.printHelpOn(System.out);

			Alert.showError(e, false);
		}

		return set;
	}

	public static Map<String, Object> parse(OptionSet set) {
		Map<String, Object> r = new HashMap<String, Object>();
		if (set == null)
			return r;

		for (Entry<String, String> a : m.entrySet()) {
			String key = a.getKey();
			Object value = null;
			if (key.startsWith("-")) {
				key = key.substring(1);
				value = true;
			}
			if (!set.has(key))
				continue;

			if (value == null)
				value = set.valueOf(key);
			r.put(a.getValue(), value);
		}

		return r;
	}

	private static Map<String, String> createLinkMap() {
		Map<String, String> r = new HashMap<String, String>();

		r.put("directory", "minecraft.gamedir");
		r.put("java-directory", "minecraft.javadir");
		r.put("version", "login.version");
		r.put("username", "login.account");
		r.put("javaargs", "minecraft.javaargs");
		r.put("margs", "minecraft.args");
		r.put("window", "minecraft.size");
		r.put("background", "gui.background");
		r.put("fullscreen", "minecraft.fullscreen");

		return r;
	}

	private static OptionParser createParser() {
		OptionParser parser = new OptionParser();

		parser.accepts("help", "Shows this help");
		parser.accepts("nogui", "Starts minimal version");
		parser.accepts("directory", "Specifies Minecraft directory")
		.withRequiredArg();
		parser.accepts("java-directory", "Specifies Java directory")
		.withRequiredArg();
		parser.accepts("version", "Specifies version to run").withRequiredArg();
		parser.accepts("username", "Specifies username").withRequiredArg();
		parser.accepts("javaargs", "Specifies JVM arguments").withRequiredArg();
		parser.accepts("margs", "Specifies Minecraft arguments")
		.withRequiredArg();
		parser.accepts("window",
				"Specifies window size in format: width;height")
				.withRequiredArg();
		parser.accepts("settings", "Specifies path to settings file")
		.withRequiredArg();
		parser.accepts("background",
				"Specifies background image. URL links, JPEG and PNG formats are supported.")
				.withRequiredArg();
		parser.accepts("fullscreen",
				"Specifies whether fullscreen mode enabled or not")
				.withRequiredArg();

		return parser;
	}
}
