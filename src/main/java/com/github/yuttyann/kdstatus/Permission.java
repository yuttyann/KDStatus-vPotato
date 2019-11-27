package com.github.yuttyann.kdstatus;

import org.bukkit.permissions.Permissible;

import com.github.yuttyann.kdstatus.utils.StringUtils;

public enum Permission {
	KDSTATUS_COMMAND_RELOAD("kdstatus.command.reload"),
	KDSTATUS_COMMAND_RANKING("kdstatus.command.ranking"),
	KDSTATUS_COMMAND_STATUS("kdstatus.command.status"),
	KDSTATUS_COMMAND_SET("kdstatus.command.set");

	private final String node;

	private Permission(String node) {
		this.node = node;
	}

	public String getNode() {
		return node;
	}

	@Override
	public String toString() {
		return node;
	}

	public boolean has(Permissible permissible) {
		return has(permissible, node);
	}

	public static boolean has(Permissible permissible, String permission) {
		return StringUtils.isNotEmpty(permission) && permissible.hasPermission(permission);
	}
}