package com.github.yuttyann.kdstatus.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.yuttyann.kdstatus.Permission;
import com.github.yuttyann.kdstatus.utils.StreamUtils;
import com.github.yuttyann.kdstatus.utils.Utils;

public abstract class BaseCommand extends CommandUsage implements TabExecutor {

	private Plugin plugin;
	private boolean isIgnoreUsage;

	public BaseCommand(Plugin plugin) {
		this.plugin = plugin;
		setUsage(getUsages());
	}

	public final Plugin getPlugin() {
		return plugin;
	}

	public abstract String getName();

	public abstract String getCommandName();

	public abstract CommandData[] getUsages();

	public abstract boolean isAliases();

	public final boolean isIgnoreUsage() {
		return isIgnoreUsage;
	}

	@Override
	public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof ProxiedCommandSender) {
			CommandSender proxiedCommandSender = ((ProxiedCommandSender) sender).getCallee();
			if (proxiedCommandSender instanceof Player) {
				sender = proxiedCommandSender;
			}
		}
		try {
			if (!runCommand(sender, command, label, args) && !isIgnoreUsage) {
				sendUsage(this, sender, command);
			}
		} finally {
			isIgnoreUsage = false;
		}
		return true;
	}

	@Override
	public final List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> completeList = new ArrayList<String>();
		tabComplete(sender, command, label, args, completeList);
		return completeList;
	}

	protected abstract boolean runCommand(CommandSender sender, Command command, String label, String[] args);

	protected abstract void tabComplete(CommandSender sender, Command command, String label, String[] args, List<String> empty);

	protected final boolean hasPermission(CommandSender sender, Permission permission) {
		return hasPermission(sender, permission, true);
	}

	protected final boolean hasPermission(CommandSender sender, Permission permission, boolean isPlayer) {
		if (isPlayer && !isPlayer(sender)) {
			return false;
		}
		boolean has = permission.has(sender);
		if (!has) {
			isIgnoreUsage = true;
		}
		Utils.sendMessage(sender, has ? null : "&cパーミッションが無いため、実行できません。");
		return has;
	}

	protected final boolean isPlayer(CommandSender sender) {
		if (sender instanceof Player) {
			return true;
		}
		Utils.sendMessage("&cコマンドはゲーム内から実行してください。");
		isIgnoreUsage = true;
		return false;
	}

	protected final boolean equals(String source, String another) {
		return another == null ? false : another.equalsIgnoreCase(source);
	}

	protected final boolean equals(String source, String... anothers) {
		return StreamUtils.anyMatch(anothers, s -> equals(source, s));
	}
}