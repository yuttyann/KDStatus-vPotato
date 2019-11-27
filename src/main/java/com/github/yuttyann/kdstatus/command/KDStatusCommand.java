package com.github.yuttyann.kdstatus.command;

import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.yuttyann.kdstatus.KDStatus;
import com.github.yuttyann.kdstatus.Permission;
import com.github.yuttyann.kdstatus.Ranking;
import com.github.yuttyann.kdstatus.Status;
import com.github.yuttyann.kdstatus.file.Files;
import com.github.yuttyann.kdstatus.utils.StreamUtils;
import com.github.yuttyann.kdstatus.utils.Utils;

public class KDStatusCommand extends BaseCommand {

	public KDStatusCommand(KDStatus plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "KDStatus";
	}

	@Override
	public String getCommandName() {
		return "kdstatus";
	}

	@Override
	public CommandData[] getUsages() {
		return new CommandData[] {
			new CommandData("reload - ファイルの再読み込みをします。", Permission.KDSTATUS_COMMAND_RELOAD.getNode()),
			new CommandData("ranking <pvp | pve> [top] - ランキングを表示します。", Permission.KDSTATUS_COMMAND_RANKING.getNode()),
			new CommandData("status [player] - ステータスを表示します。", Permission.KDSTATUS_COMMAND_STATUS.getNode()),
			new CommandData("set pvpkills <player> <amount> - PVPKillsを設定します。", Permission.KDSTATUS_COMMAND_SET.getNode()),
			new CommandData("set pvpdeaths <player> <amount> - PVPDeathsを設定します。", Permission.KDSTATUS_COMMAND_SET.getNode()),
			new CommandData("set pvekills <player> <amount> - PVEKillsを設定します。", Permission.KDSTATUS_COMMAND_SET.getNode()),
			new CommandData("set pvedeaths <player> <amount> - PVEDeathsを設定します。", Permission.KDSTATUS_COMMAND_SET.getNode()),
		};
	}

	@Override
	public boolean isAliases() {
		return true;
	}

	@Override
	protected boolean runCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			if (equals(args[0], "reload")) {
				return doReload(sender);
			} if (equals(args[0], "status")) {
				return doViewStatus(sender, args);
			}
		} if (args.length == 2) {
			if (equals(args[0], "ranking")) {
				try {
					return doShowRanking(sender, args);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (equals(args[0], "status")) {
				return doViewStatus(sender, args);
			}
		} else if (args.length == 3 && equals(args[0], "ranking") && isNumber(args[2])) {
			try {
				return doShowRanking(sender, args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (args.length == 4 && equals(args[0], "set") && isNumber(args[3])) {
			return doSettings(sender, args);
		}
		return false;
	}

	private boolean doReload(CommandSender sender) {
		if (!hasPermission(sender, Permission.KDSTATUS_COMMAND_RELOAD, false)) {
			return false;
		}
		Files.reload();
		KDStatus.getInstance().loadPlayers();
		Ranking.update();
		Utils.sendMessage(sender, "&a全てのファイルの再読み込みが完了しました。");
		return true;
	}

	private boolean doShowRanking(CommandSender sender, String[] args) throws NumberFormatException, Exception {
		if (!hasPermission(sender, Permission.KDSTATUS_COMMAND_RANKING, false)) {
			return false;
		}
		int top = args.length > 2 ? Integer.parseInt(args[2]) : 10;
		boolean isPVP = equals(args[1], "pvp") ? true : equals(args[1], "pve") ? false : true;
		Ranking.showRanking(sender, top, isPVP);
		return true;
	}

	private boolean doViewStatus(CommandSender sender, String[] args) {
		if (!hasPermission(sender, Permission.KDSTATUS_COMMAND_STATUS)) {
			return false;
		}
		Player player = args.length > 1 ? Utils.getPlayer(args[1]) : (Player) sender;
		if (player == null) {
			Utils.sendMessage(sender, "&c指定されたプレイヤーが見つかりません。");
			return true;
		}
		Status status = KDStatus.getInstance().getProfile(player.getUniqueId()).getStatus();
		Utils.sendMessage(sender, "&e=====[" + status.getName() + " 個人戦績]=====");
		Utils.sendBrankMessage(sender);
		Utils.sendMessage(sender, "&e[&4PVP&e]");
		Utils.sendMessage(sender, " &aキル数    &b>> &a" + status.getPvPKills());
		Utils.sendMessage(sender, " &cデス数    &b>> &c" + status.getPVPDeaths());
		Utils.sendMessage(sender, "&dK/Dレート  &b>> &d" + status.getPvPKDR());
		Utils.sendMessage(sender, " &6ランク    &b>> &6&l" + status.getPVPRank() + "&r&6 位");
		Utils.sendBrankMessage(sender);
		Utils.sendMessage(sender, "&e---------------------------------");
		Utils.sendBrankMessage(sender);
		Utils.sendMessage(sender, "&e[&2PVE&e]");
		Utils.sendMessage(sender, " &aキル数    &b>> &a" + status.getPvEKills());
		Utils.sendMessage(sender, " &cデス数    &b>> &c" + status.getPVEDeaths());
		Utils.sendMessage(sender, "&dK/Dレート  &b>> &d" + status.getPvEKDR());
		Utils.sendMessage(sender, " &6ランク    &b>> &6&l" + status.getPVERank() + "&r&6 位");
		Utils.sendBrankMessage(sender);
		Utils.sendMessage(sender, "&e=================================");
		return true;
	}

	private boolean doSettings(CommandSender sender, String[] args) {
		if (!hasPermission(sender, Permission.KDSTATUS_COMMAND_SET, false)) {
			return false;
		}
		Player player = Utils.getPlayer(args[2]);
		if (player == null) {
			Utils.sendMessage(sender, "&c指定されたプレイヤーが見つかりません。");
			return true;
		}
		int amount = Integer.parseInt(args[3]);
		Status status = ((KDStatus) getPlugin()).getProfile(player.getUniqueId()).getStatus();
		switch (args[2].toLowerCase()) {
		case "pvpkills":
			status.setPVPKills(i -> amount);
			status.save();
			Utils.sendMessage(sender, "&aPVP-Kills の値を" + amount + "に設定しました。");
			break;
		case "pvpdeaths":
			status.setPVPDeaths(i -> amount);
			status.save();
			Utils.sendMessage(sender, "&aPVP-Deaths の値を" + amount + "に設定しました。");
			break;
		case "pvekills":
			status.setPVEKills(i -> amount);
			status.save();
			Utils.sendMessage(sender, "&aPVE-Kills の値を" + amount + "に設定しました。");
			break;
		case "pvedeaths":
			status.setPVEDeaths(i -> amount);
			status.save();
			Utils.sendMessage(sender, "&aPVE-Deaths の値を" + amount + "に設定しました。");
			break;
		default:
			Utils.sendMessage(sender, "&c指定された設定項目が見つかりません。");
			break;
		}
		return true;
	}

	private boolean isNumber(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	protected void tabComplete(CommandSender sender, Command command, String label, String[] args, List<String> empty) {
		if (args.length == 1) {
			String prefix = args[0].toLowerCase();
			String[] answers = new String[] { "reload", "ranking", "status", "set" };
			StreamUtils.fForEach(answers, s -> s.startsWith(prefix), empty::add);
		} else if (args.length == 2) {
			if (equals(args[0], "ranking")) {
				String prefix = args[1].toLowerCase();
				String[] answers = new String[] { "pvp", "pve" };
				StreamUtils.fForEach(answers, s -> s.startsWith(prefix), empty::add);
			} else if (equals(args[0], "status")) {
				Collection<? extends Player> players = Bukkit.getOnlinePlayers();
				String prefix = args[1].toLowerCase();
				String[] answers = StreamUtils.toArray(players, p -> p.getName(), new String[players.size()]);
				StreamUtils.fForEach(answers, s -> s.startsWith(prefix), empty::add);
			} else if (equals(args[0], "set")) {
				String prefix = args[1].toLowerCase();
				String[] answers = new String[] { "pvpkills", "pvpdeaths", "pvekills", "pvedeaths" };
				StreamUtils.fForEach(answers, s -> s.startsWith(prefix), empty::add);
			}
		} else if (args.length == 3) {
			if (equals(args[0], "ranking")) {
				empty.add("10");
			} else if (equals(args[0], "set")) {
				Collection<? extends Player> players = Bukkit.getOnlinePlayers();
				String prefix = args[2].toLowerCase();
				String[] answers = StreamUtils.toArray(players, p -> p.getName(), new String[players.size()]);
				StreamUtils.fForEach(answers, s -> s.startsWith(prefix), empty::add);
			}
		} else if (args.length == 4 && equals(args[0], "set")) {
			empty.add("1");
		}
	}
}