package com.github.yuttyann.kdstatus;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuttyann.kdstatus.command.KDStatusCommand;
import com.github.yuttyann.kdstatus.file.Files;
import com.github.yuttyann.kdstatus.file.yaml.YamlConfig;
import com.github.yuttyann.kdstatus.listener.PlayerListener;
import com.github.yuttyann.kdstatus.utils.Utils;

public class KDStatus extends JavaPlugin {

	private static final Map<UUID, GameProfile> PROFILES = new HashMap<>();

	private KDStatusCommand kdStatusCommand;
	{
		new PluginInstance("KDStatus", this);
	}

	@Override
	public void onEnable() {
		Files.reload();
		loadPlayers();
		Ranking.update();

		this.kdStatusCommand = new KDStatusCommand(this);

		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
	}

	@Override
	public void onDisable() {
		PROFILES.clear();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equals(kdStatusCommand.getCommandName())) {
			return kdStatusCommand.onCommand(sender, command, label, args);
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equals(kdStatusCommand.getCommandName())) {
			return kdStatusCommand.onTabComplete(sender, command, label, args);
		}
		return super.onTabComplete(sender, command, label, args);
	}

	public static KDStatus getInstance() {
		return PluginInstance.get("KDStatus");
	}

	public Map<UUID, GameProfile> getProfiles() {
		return PROFILES;
	}

	public UUID getUniqueId(String name) {
		for (Entry<UUID, GameProfile> profile : PROFILES.entrySet()) {
			if (profile.getValue().getName().equals(name)) {
				return profile.getValue().getUniqueId();
			}
		}
		return null;
	}

	public String getName(UUID uuid) {
		GameProfile profile = PROFILES.get(uuid);
		return profile == null ? null : profile.getName();
	}

	public GameProfile getProfile(UUID uuid) {
		return PROFILES.get(uuid);
	}

	public void loadPlayers() {
		PROFILES.clear();
		String fileName = null;
		try {
			Utils.sendMessage("&dプレイヤーデータの読み込みを開始します。");
			File players = new File(getDataFolder(), "players");
			if (players.exists() && players.isDirectory()) {
				File[] files = players.listFiles();
				if (files.length > 0) {
					GameProfile profile = null;
					for (File file : files) {
						String path = file.getPath();
						if (!path.endsWith(".yml")) {
							continue;
						}
						fileName = file.getName();
						YamlConfig yaml = YamlConfig.load(this, file, false);
						try {
							profile = new GameProfile(yaml.getUUID("UUID"), yaml.getString("Name"), yaml);
						} catch (NullPointerException e) {
							yaml = null;
							profile = null;
							file.delete();
						} finally {
							if (yaml != null && profile != null) {
								PROFILES.put(profile.getUniqueId(), profile);
								Utils.sendMessage("&a成功: " + profile.getUniqueId().toString() + ".yml");
							} else {
								Utils.sendMessage("&c失敗: " + profile.getUniqueId().toString() + ".yml");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Utils.sendMessage("&cプレイヤーデータのロードに失敗しました。");
			Utils.sendMessage("&cデータファイル: " + fileName);
			Utils.sendMessage("&cエラーコード: " + e.toString());
			getServer().getPluginManager().disablePlugin(this);
			return;
		} finally {
			Utils.sendMessage("&dプレイヤーデータの読み込みが完了しました。");
		}
	}
}