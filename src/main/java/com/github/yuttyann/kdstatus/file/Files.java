package com.github.yuttyann.kdstatus.file;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.github.yuttyann.kdstatus.KDStatus;
import com.github.yuttyann.kdstatus.file.yaml.YamlConfig;

public final class Files {

	private static final Map<String, YamlConfig> FILES = new HashMap<>();

	public static void reload() {
		FILES.put("config", loadFile("config.yml", true));
	}

	public static Map<String, YamlConfig> getFiles() {
		return Collections.unmodifiableMap(FILES);
	}

	public static YamlConfig getConfig() {
		return FILES.get("config");
	}

	private static YamlConfig loadFile(String filePath, boolean isCopyFile) {
		return YamlConfig.load(KDStatus.getInstance(), filePath, isCopyFile);
	}
}