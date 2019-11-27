package com.github.yuttyann.kdstatus.listener;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.yuttyann.kdstatus.GameProfile;
import com.github.yuttyann.kdstatus.Item;
import com.github.yuttyann.kdstatus.KDStatus;
import com.github.yuttyann.kdstatus.Ranking;
import com.github.yuttyann.kdstatus.Status;
import com.github.yuttyann.kdstatus.file.yaml.YamlConfig;

public class PlayerListener implements Listener {

	private KDStatus plugin;

	public PlayerListener(KDStatus plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		GameProfile profile = plugin.getProfile(player.getUniqueId());
		if (profile == null) {
			UUID uuid = player.getUniqueId();
			YamlConfig yaml = YamlConfig.load(plugin, "players/" + uuid.toString() + ".yml", false);
			plugin.getProfiles().put(uuid, profile = new GameProfile(player.getUniqueId(), player.getName(), yaml));
			profile.getStatus().init();
		}
		Status status = profile.getStatus();
		if (status.getUUID() == null) {
			status.setUUID(player.getUniqueId());
		}
		if (!player.getName().equals(status.getName())) {
			status.setName(player.getName());
		}
		status.save();
		Ranking.updatePlayer(player.getUniqueId());
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player deader = event.getEntity();
		Player killer = deader.getKiller();
		Status deaderStatus = plugin.getProfile(deader.getUniqueId()).getStatus();
		if (killer instanceof Player && deader instanceof Player) {
			Status killerStatus = plugin.getProfile(killer.getUniqueId()).getStatus();
			killerStatus.setPVPKills(i -> i + 1);
			deaderStatus.setPVPDeaths(i -> i + 1);
			killerStatus.save();
			deaderStatus.save();
			Ranking.updatePlayer(killer.getUniqueId());
			Item.killItems(killer, deader);
		}
		Ranking.updatePlayer(deader.getUniqueId());
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity deader = event.getEntity();
		Player killer = deader.getKiller();
		if (killer == null || deader instanceof Player) {
			return;
		}
		Status status = plugin.getProfile(killer.getUniqueId()).getStatus();
		status.setPVEKills(i -> i + 1);
		status.save();
		Ranking.updatePlayer(killer.getUniqueId());
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();   // 被害者
		Entity damager = event.getDamager(); // 攻撃者
		if (entity instanceof Player && !(damager instanceof Player) && damager instanceof LivingEntity) {
			Player deader = (Player) entity;
			if ((deader.getHealth() - event.getDamage()) < 1) {
				Status deaderStatus = plugin.getProfile(deader.getUniqueId()).getStatus();
				deaderStatus.setPVEDeaths(i -> i + 1);
				deaderStatus.save();
				Ranking.updatePlayer(deader.getUniqueId());
			}
		}
	}
}