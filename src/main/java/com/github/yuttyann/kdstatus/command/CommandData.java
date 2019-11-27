package com.github.yuttyann.kdstatus.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.yuttyann.kdstatus.Permission;
import com.github.yuttyann.kdstatus.utils.StreamUtils;
import com.github.yuttyann.kdstatus.utils.StringUtils;

public class CommandData {

	private String message;
	private boolean isPrefix;

	private List<String> permissions = new ArrayList<>();

	public CommandData(String message, boolean isPrefix) {
		this(message, isPrefix, (String) null);
	}

	public CommandData(String message, String... permission) {
		this(message, true, permission);
	}

	public CommandData(String message, boolean isPrefix, String... permission) {
		setMessage(message);
		setPrefix(isPrefix);
		addPermission(permission);
	}

	public CommandData setMessage(String message) {
		this.message = message;
		return this;
	}

	public CommandData setPrefix(boolean isPrefix) {
		this.isPrefix = isPrefix;
		return this;
	}

	public CommandData addPermission(String... permission) {
		if (permission != null && permission.length > 0) {
			StreamUtils.forEach(permission, permissions::add);
		}
		return this;
	}

	public String getMessage() {
		return message;
	}

	public boolean isPrefix() {
		return isPrefix;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public final boolean hasMessage() {
		return StringUtils.isNotEmpty(message);
	}

	public boolean hasPermission(CommandSender sender) {
		if (permissions == null || permissions.size() == 0) {
			return true;
		}
		return StreamUtils.anyMatch(permissions, s -> Permission.has(sender, s));
	}
}