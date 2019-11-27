package com.github.yuttyann.kdstatus.file.yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import com.github.yuttyann.kdstatus.utils.FileUtils;
import com.github.yuttyann.kdstatus.utils.StringUtils;

public class YamlConfig {

	private static final Pattern UUID_PATTERN = Pattern.compile(
			"[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"
	);

	private final Plugin plugin;
	private final File file;

	private UTF8Config yaml;
	private boolean isCopyFile;

	protected YamlConfig(Plugin plugin, File file, boolean isCopyFile) {
		this.plugin = plugin;
		this.file = file;
		this.isCopyFile = isCopyFile;
		reload();
	}

	public static YamlConfig load(Plugin plugin, File file) {
		return load(plugin, file, true);
	}

	public static YamlConfig load(Plugin plugin, File file, boolean isCopyFile) {
		return new YamlConfig(plugin, file, isCopyFile);
	}

	public static YamlConfig load(Plugin plugin, String filePath) {
		return load(plugin, filePath, true);
	}

	public static YamlConfig load(Plugin plugin, String filePath, boolean isCopyFile) {
		return load(plugin, new File(plugin.getDataFolder(), filePath), isCopyFile);
	}

	public final YamlConfig setCopyFile(boolean isCopyFile) {
		this.isCopyFile = isCopyFile;
		return this;
	}

	public final Plugin getPlugin() {
		return plugin;
	}

	public final File getDataFolder() {
		return plugin.getDataFolder();
	}

	public final File getFile() {
		return file;
	}

	public final String getFileName() {
		return file.getName();
	}

	public final String getPath() {
		return file.getPath();
	}

	public final String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	public final String getFolderPath() {
		String path = StringUtils.removeStart(getPath(), getDataFolder().getPath());
		return path.startsWith("\\") ? path.substring(1, path.length()) : path;
	}

	public final boolean exists() {
		return file.exists();
	}

	public final long length() {
		return file.length();
	}

	public final void reload() {
		Validate.notNull(file, "File cannot be null");
		if (isCopyFile && !file.exists()) {
			FileUtils.copyFileFromPlugin(plugin, file, getFolderPath());
		}
		yaml = new UTF8Config();
		try {
			yaml.load(file);
		} catch (FileNotFoundException e) {
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			yaml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save(File file) throws IOException {
		yaml.save(file);
	}

	public void save(String file) throws IOException {
		yaml.save(file);
	}

	public void addDefault(String path, Object value) {
		yaml.addDefault(path, value);
	}

	public void addDefault(Configuration defaults) {
		yaml.addDefaults(defaults);
	}

	public void addDefault(Map<String, Object> defaults) {
		yaml.addDefaults(defaults);
	}

	public void set(String path, Object value) {
		yaml.set(path, value);
	}

	public void setDefaults(Configuration defaults) {
		yaml.setDefaults(defaults);
	}

	public ConfigurationSection createSection(String path) {
		return yaml.createSection(path);
	}

	public ConfigurationSection createSection(String path, Map<?, ?> map) {
		return yaml.createSection(path, map);
	}

	public ConfigurationSection getConfigurationSection(String path) {
		return yaml.getConfigurationSection(path);
	}

	public Configuration getDefaults() {
		return yaml.getDefaults();
	}

	public ConfigurationSection getDefaultSection() {
		return yaml.getDefaultSection();
	}

	public ConfigurationSection getParent() {
		return yaml.getParent();
	}

	public Configuration getRoot() {
		return yaml.getRoot();
	}

	public String getCurrentPath() {
		return yaml.getCurrentPath();
	}

	public String getName() {
		return yaml.getName();
	}

	public String getString(String path) {
		return yaml.getString(path);
	}

	public String getString(String path, String def) {
		return yaml.getString(path, def);
	}

	public Object get(String path) {
		return yaml.get(path);
	}

	public Object get(String path, Object def) {
		return yaml.get(path, def);
	}

	public UUID getUUID(String path) {
		Object def = getDefault(path);
		return getUUID(path, def == null ? null : UUID.fromString(def.toString()));
	}

	public UUID getUUID(String path, UUID def) {
		Object val = get(path, def);
		return val == null ? def : UUID.fromString(val.toString());
	}

	public Color getColor(String path) {
		return yaml.getColor(path);
	}

	public Color getColor(String path, Color def) {
		return yaml.getColor(path, def);
	}

	public ItemStack getItemStack(String path) {
		return yaml.getItemStack(path);
	}

	public ItemStack getItemStack(String path, ItemStack def) {
		return yaml.getItemStack(path, def);
	}

	public Vector getVector(String path) {
		return yaml.getVector(path);
	}

	public Vector getVector(String path, Vector def) {
		return yaml.getVector(path, def);
	}

	public FileConfigurationOptions options(String path) {
		return yaml.options();
	}

	public boolean contains(String path) {
		return yaml.contains(path);
	}

	public boolean getBoolean(String path) {
		return yaml.getBoolean(path);
	}

	public boolean getBoolean(String path, boolean def) {
		return yaml.getBoolean(path, def);
	}

	public boolean isString(String path) {
		return yaml.isString(path);
	}

	public boolean isUUID(String path) {
		String val = getString(path);
		return val == null ? false : UUID_PATTERN.matcher(val).matches();
	}

	public boolean isColor(String path) {
		return yaml.isColor(path);
	}

	public boolean isItemStack(String path) {
		return yaml.isItemStack(path);
	}

	public boolean isVector(String path) {
		return yaml.isVector(path);
	}

	public boolean isBoolean(String path) {
		return yaml.isBoolean(path);
	}

	public boolean isOfflinePlayer(String path) {
		return yaml.isOfflinePlayer(path);
	}

	public boolean isConfigurationSection(String path) {
		return yaml.isConfigurationSection(path);
	}

	public boolean isInt(String path) {
		return yaml.isInt(path);
	}

	public boolean isDouble(String path) {
		return yaml.isDouble(path);
	}

	public boolean isFloat(String path) {
		Object val = get(path);
		return val instanceof Float;
	}

	public boolean isLong(String path) {
		return yaml.isLong(path);
	}

	public boolean isSet(String path) {
		return yaml.isSet(path);
	}

	public boolean isList(String path) {
		return yaml.isList(path);
	}

	public int getInt(String path) {
		return yaml.getInt(path);
	}

	public int getInt(String path, int def) {
		return yaml.getInt(path, def);
	}

	public double getDouble(String path) {
		return yaml.getDouble(path);
	}

	public double getDouble(String path, double def) {
		return yaml.getDouble(path, def);
	}

	public float getFloat(String path) {
		Object def = getDefault(path);
		return getFloat(path, def instanceof Number ? NumberConversions.toFloat(def) : 0.0F);
	}

	public float getFloat(String path, float def) {
		Object val = get(path, def);
		return val instanceof Number ? NumberConversions.toFloat(val) : def;
	}

	public long getLong(String path) {
		return yaml.getLong(path);
	}

	public long getLong(String path, long def) {
		return yaml.getLong(path, def);
	}

	public Set<String> getKeys() {
		return yaml.getKeys(false);
	}

	public Set<String> getKeys(boolean deep) {
		return yaml.getKeys(deep);
	}

	public Set<String> getKeys(String path) {
		return getConfigurationSection(path).getKeys(false);
	}

	public Set<String> getKeys(String path, boolean deep) {
		return getConfigurationSection(path).getKeys(deep);
	}

	public Map<String, Object> getValues(boolean deep) {
		return yaml.getValues(deep);
	}

	public List<?> getList(String path) {
		return yaml.getList(path);
	}

	public List<?> getList(String path, List<?> def) {
		return yaml.getList(path, def);
	}

	public List<Map<?, ?>> getMapList(String path) {
		return yaml.getMapList(path);
	}

	public List<String> getStringList(String path) {
		return yaml.getStringList(path);
	}

	public List<UUID> getUUIDList(String path) {
		List<?> list = getList(path);
		if (list == null) {
			return new ArrayList<>();
		}
		List<UUID> result = new ArrayList<>(list.size());
		for (Object object : list) {
			if (object instanceof String) {
				result.add(UUID.fromString(object.toString()));
			}
		}
		return result;
	}

	public List<Boolean> getBooleanList(String path) {
		return yaml.getBooleanList(path);
	}

	public List<Character> getCharacterList(String path) {
		return yaml.getCharacterList(path);
	}

	public List<Integer> getIntegerList(String path) {
		return yaml.getIntegerList(path);
	}

	public List<Double> getDoubleList(String path) {
		return yaml.getDoubleList(path);
	}

	public List<Float> getFloatList(String path) {
		return yaml.getFloatList(path);
	}

	public List<Long> getLongList(String path) {
		return yaml.getLongList(path);
	}

	public List<Short> getShortList(String path) {
		return yaml.getShortList(path);
	}

	public List<Byte> getByteList(String path) {
		return yaml.getByteList(path);
	}

	@Override
	public String toString() {
		return yaml.toString();
	}

	private Object getDefault(String path) {
		Validate.notNull(path, "Path cannot be null");
		Configuration root = getRoot();
		Configuration defaults = root == null ? null : root.getDefaults();
		return defaults == null ? null : defaults.get(MemorySection.createPath(yaml, path));
	}
}