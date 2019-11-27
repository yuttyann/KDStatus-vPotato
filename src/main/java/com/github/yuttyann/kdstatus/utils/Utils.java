package com.github.yuttyann.kdstatus.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class Utils {

	public static void sendBrankMessage(CommandSender sender) {
		sender.sendMessage(" ");
	}

	public static void sendMessage(String message) {
		sendMessage(Bukkit.getConsoleSender(), message);
	}

	public static void sendMessage(CommandSender sender, String message) {
		if (StringUtils.isNotEmpty(message)) {
			message = ChatColor.translateAlternateColorCodes('&', StringUtils.replace(message, "\\n", "|~"));
			String color = "";
			for (String line : StringUtils.split(message, "|~")) {
				sender.sendMessage(line = (color + line));
				if (line.indexOf('§') > -1) {
					color = StringUtils.getColors(line);
				}
			}
		}
	}

	public static Player getPlayer(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		return StreamUtils.fOrElse(Bukkit.getOnlinePlayers(), p -> name.equals(p.getName()), null);
	}
}