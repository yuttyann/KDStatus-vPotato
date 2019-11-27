package com.github.yuttyann.kdstatus.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.yuttyann.kdstatus.utils.StreamUtils;
import com.github.yuttyann.kdstatus.utils.Utils;

public abstract class CommandUsage {

	private List<CommandData> usages;

	public final void setUsage(CommandData... args) {
		usages = new ArrayList<CommandData>(args.length);
		StreamUtils.forEach(args, usages::add);
	}

	public final void addUsage(CommandData... args) {
		if (usages != null) {
			StreamUtils.forEach(args, usages::add);
		}
	}

	protected final void sendUsage(BaseCommand baseCommand, CommandSender sender, Command command) {
		if (usages == null || usages.isEmpty()) {
			return;
		}
		List<CommandData> list = new ArrayList<>(usages.size());
		StreamUtils.fForEach(usages, c -> c.hasPermission(sender), list::add);
		if (list.isEmpty()) {
			Utils.sendMessage(sender, "&cパーミッションが無いため、実行できません。");
			return;
		}
		String commandName = command.getName();
		if (baseCommand.isAliases() && command.getAliases().size() > 0) {
			commandName = command.getAliases().get(0).toLowerCase();
		}
		sender.sendMessage("§d========== " + baseCommand.getName() + " Commands ==========");
		String prefix = "§b/" + commandName + " ";
		StreamUtils.fForEach(list, c -> c.hasMessage(), c -> sender.sendMessage(text(c, prefix)));
	}

	private String text(CommandData commandData, String prefix) {
		return commandData.isPrefix() ? prefix + commandData.getMessage() : commandData.getMessage();
	}
}