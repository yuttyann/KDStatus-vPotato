package com.github.yuttyann.kdstatus.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.plugin.Plugin;

import com.google.common.base.Charsets;

public final class FileUtils {

	public static InputStream getResource(Plugin plugin, String filePath) {
		if (plugin == null || StringUtils.isEmpty(filePath)) {
			return null;
		}
		try {
			URL url = plugin.getClass().getClassLoader().getResource(filePath);
			if (url == null) {
				return null;
			}
			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			return connection.getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

	public static void copyFileFromPlugin(Plugin plugin, File targetFile, String sourceFilePath) {
		if (targetFile == null || StringUtils.isEmpty(sourceFilePath)) {
			return;
		}
		File parent = targetFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		try (
				InputStream is = getResource(plugin, sourceFilePath); InputStreamReader isr = new InputStreamReader(is, Charsets.UTF_8);
				OutputStream os = new FileOutputStream(targetFile); OutputStreamWriter osw = new OutputStreamWriter(os, Charsets.UTF_8);
				BufferedReader reader = new BufferedReader(isr); BufferedWriter writer = new BufferedWriter(osw)
			) {
			boolean isFirst = true;
			String line;
			while ((line = reader.readLine()) != null) {
				if (isFirst && !(isFirst = false)) {
					line = removeBom(line);
				}
				writer.write(line);
				writer.newLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isEmpty(File file) {
		String[] array = file.list();
		return array == null || array.length == 0;
	}

	private static String removeBom(String source) {
		if (source.startsWith("\uFEFF")) {
			return source.substring(1);
		}
		return source;
	}
}