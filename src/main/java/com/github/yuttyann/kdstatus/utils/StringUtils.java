package com.github.yuttyann.kdstatus.utils;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.text.StrBuilder;
import org.bukkit.ChatColor;

public final class StringUtils {

	public static String[] split(String source, String delimiter) {
		if (isEmpty(source) || isEmpty(delimiter)) {
			return new String[] { source };
		}
		int start = 0;
		int end = source.indexOf(delimiter, start);
		List<String> result = new LinkedList<>();
		while (end != -1) {
			result.add(source.substring(start, end));
			start = end + delimiter.length();
			end = source.indexOf(delimiter, start);
		}
		result.add(source.substring(start));
		return result.toArray(new String[result.size()]);
	}

	public static String getColors(String source) {
		if (isEmpty(source)) {
			return source;
		}
		char[] chars = source.toCharArray();
		StringBuilder builder = new StringBuilder(chars.length);
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != '§' || (i + 1) >= chars.length) {
				continue;
			}
			if (ChatColor.getByChar(Character.toLowerCase(chars[i + 1])) != null) {
				builder.append(chars[i++]).append(chars[i]);
			}
		}
		return builder.toString();
	}

	public static String replace(String source, String search, String replace) {
		return replace(source, search, () -> replace);
	}

	public static String replace(String source, String search, ReplaceValue replace) {
		if (isEmpty(source) || isEmpty(search)) {
			return source;
		}
		int start = 0;
		int end = source.indexOf(search, start);
		if (end == -1) {
			return source;
		}
		int searchLength = search.length();
		int replaceLength = source.length() - replace.length();
		replaceLength = replaceLength < 0 ? 0 : replaceLength;
		StrBuilder builder = new StrBuilder(source.length() + replaceLength);
		while (end != -1) {
			builder.append(source.substring(start, end)).append(replace.asString());
			start = end + searchLength;
			end = source.indexOf(search, start);
		}
		builder.append(source.substring(start));
		return builder.toString();
	}

	public interface ReplaceValue {

		Object value();

		default String asString() {
			Object obj = value();
			return obj == null ? "" : obj.toString();
		}

		default int length() {
			return asString().length();
		}
	}

	public static String removeStart(String source, String prefix) {
		if (isEmpty(source) || isEmpty(prefix)) {
			return source;
		}
		return source.startsWith(prefix) ? source.substring(prefix.length(), source.length()) : source;
	}

	public static boolean isNotEmpty(String source) {
		return !isEmpty(source);
	}

	public static boolean isEmpty(String source) {
		return source == null || source.length() == 0;
	}
}