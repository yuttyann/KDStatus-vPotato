package com.github.yuttyann.kdstatus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.yuttyann.kdstatus.file.Files;
import com.github.yuttyann.kdstatus.file.yaml.YamlConfig;

public class Item {

	public static void killItems(Player killer, Player deader) {
		YamlConfig item = Files.getConfig();
		boolean drop = item.getBoolean("Drop");
		for(String name : item.getConfigurationSection("Items").getKeys(false)) {
			Material type;
			int amount;
			int damage;
			try {
				String key = "Items." + name + ".";
				type = Material.getMaterial(item.getString(key + "Material"));
				amount = item.getInt(key + "Amount");
				damage = item.getInt(key + "Damage");
			} catch(NumberFormatException e) {
				continue;
			}
			@SuppressWarnings("deprecation")
			ItemStack itemStack = new ItemStack(type, amount, (short) damage);
			if (drop) {
				killer.getWorld().dropItemNaturally(deader.getLocation(), itemStack);
			} else {
				killer.getInventory().addItem(itemStack);
			}
		}
	}
}