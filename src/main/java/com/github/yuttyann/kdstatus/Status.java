package com.github.yuttyann.kdstatus;

import java.math.BigDecimal;
import java.util.UUID;

import com.github.yuttyann.kdstatus.file.yaml.YamlConfig;

public final class Status {

	private YamlConfig yaml;

	public Status(GameProfile profile) {
		this.yaml = profile.getYaml();
	}

	public void save() {
		yaml.save();
	}

	public void init() {
		yaml.set("PVP-Kills", 0);
		yaml.set("PVP-Deaths", 0);
		yaml.set("PVE-Kills", 0);
		yaml.set("PVE-Deaths", 0);
	}

	public void setUUID(UUID uuid) {
		yaml.set("UUID", uuid.toString());
	}

	public void setName(String name) {
		yaml.set("Name", name);
	}

	public void setPVPKills(IValue<Integer> result) {
		yaml.set("PVP-Kills", result.value(getPvPKills()));
	}

	public void setPVPDeaths(IValue<Integer> result) {
		yaml.set("PVP-Deaths", result.value(getPVPDeaths()));
	}

	public void setPVEKills(IValue<Integer> result) {
		yaml.set("PVE-Kills", result.value(getPvEKills()));
	}

	public void setPVEDeaths(IValue<Integer> result) {
		yaml.set("PVE-Deaths", result.value(getPVEDeaths()));
	}

	public UUID getUUID() {
		return yaml.getUUID("UUID");
	}

	public String getName() {
		return yaml.getString("Name");
	}

	public int getPvPKills() {
		return yaml.getInt("PVP-Kills");
	}

	public int getPVPDeaths() {
		return yaml.getInt("PVP-Deaths");
	}

	public int getPvEKills() {
		return yaml.getInt("PVE-Kills");
	}

	public int getPVEDeaths() {
		return yaml.getInt("PVE-Deaths");
	}

	public int getPVPRank() {
		return Ranking.getRank(getUUID(), true);
	}

	public int getPVERank() {
		return Ranking.getRank(getUUID(), false);
	}

	public double getPvPKDR() {
		return resultKDR(getPvPKills(), getPVPDeaths());
	}

	public double getPvEKDR() {
		return resultKDR(getPvEKills(), getPVEDeaths());
	}

	private double resultKDR(int kill, int death) {
		if (kill == 0 && death >= 0) {
			return 0.0D;
		} else if (kill > 0 && death == 0) {
			return kill;
		} else {
			return new BigDecimal((double) kill / death).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
	}
}