package com.github.yuttyann.kdstatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.command.CommandSender;

import com.github.yuttyann.kdstatus.utils.Utils;

public class Ranking {

	private static final KDStatus INSTANCE = KDStatus.getInstance();

	private static final Map<UUID, Double> PVPRANKING = new HashMap<>();
	private static final Map<UUID, Double> PVERANKING = new HashMap<>();

	public static void update() {
		PVPRANKING.clear();
		PVERANKING.clear();
		for (GameProfile profile : INSTANCE.getProfiles().values()) {
			PVPRANKING.put(profile.getUniqueId(), profile.getStatus().getPvPKDR());
			PVERANKING.put(profile.getUniqueId(), profile.getStatus().getPvEKDR());
		}
	}

	public static void updatePlayer(UUID uuid) {
		GameProfile profile = INSTANCE.getProfile(uuid);
		PVPRANKING.put(profile.getUniqueId(), profile.getStatus().getPvPKDR());
		PVERANKING.put(profile.getUniqueId(), profile.getStatus().getPvEKDR());
	}

	public static int getRank(UUID uuid, boolean isPVP) {
		List<Entry<UUID, Double>> entries = new ArrayList<>(isPVP ? PVPRANKING.entrySet() : PVERANKING.entrySet());
		entries.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));
		int rank = 0;
		for (Entry<UUID, Double> e : entries) {
			if (e.getKey().equals(uuid)) {
				return rank + 1;
			}
			rank++;
		}
		return rank;
	}

	public static void showRanking(CommandSender sender, int top, boolean isPVP) throws Exception {
		Utils.sendMessage(sender, "&7ランキングを計算しています...");
		List<Entry<UUID, Double>> entries = new ArrayList<>(isPVP ? PVPRANKING.entrySet() : PVERANKING.entrySet());
		entries.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));
		int rank = 0;
		try {
			Utils.sendMessage(sender, "&e=====[キルレート ランキング]=====");
			Utils.sendBrankMessage(sender);
			Utils.sendMessage(sender, isPVP ? "&e[&4PVP§e]" : "&e[&2PVE&e]");
			for (Entry<UUID, Double> s : entries) {
				if (rank == top) {
					return;
				}
				String name = INSTANCE.getName(s.getKey());
				String space = ++rank < 10 ? "   " : rank > 9 ? "  " : rank > 99 ? " " : " ";
				Utils.sendMessage(sender, "&a" + rank + "位" + space + "&b>> &a" + name + " &d" + s.getValue());
			}
		} finally {
			Utils.sendBrankMessage(sender);
			Utils.sendMessage(sender, "&e=================================");
		}
	}
}