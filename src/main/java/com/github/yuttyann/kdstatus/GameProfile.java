package com.github.yuttyann.kdstatus;

import java.util.UUID;

import com.github.yuttyann.kdstatus.file.yaml.YamlConfig;

public final class GameProfile {

	private final YamlConfig yaml;
	private final Status status;
	private final String name;
	private final UUID uuid;

	public GameProfile(UUID uuid, String name, YamlConfig yaml) {
		this.uuid = uuid;
		this.name = name;
		this.yaml = yaml;
		this.status = new Status(this);
	}

	public YamlConfig getYaml() {
		return yaml;
	}

	public Status getStatus() {
		return status;
	}

	public String getName() {
		return name;
	}

	public UUID getUniqueId() {
		return uuid;
	}
}