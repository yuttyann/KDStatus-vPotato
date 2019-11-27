package com.github.yuttyann.kdstatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

public final class PluginInstance {

	private static final Map<String, JavaPlugin> INSTANCES = new HashMap<>();

	public PluginInstance(String key, JavaPlugin plugin) {
		INSTANCES.put(key, plugin);
	}

	public Set<Entry<String, JavaPlugin>> entrySet() {
		return INSTANCES.entrySet();
	}

	public static <T extends JavaPlugin> T get(String key) {
		return (T) INSTANCES.get(key);
	}
}