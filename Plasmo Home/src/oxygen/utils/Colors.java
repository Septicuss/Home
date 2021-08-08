package oxygen.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;

public class Colors {

	private final static char COLOR_CHAR = ChatColor.COLOR_CHAR;

	/**
	 * 
	 * Sourced from @Elementeral on Spigot. Modified.
	 * (https://www.spigotmc.org/threads/hex-color-code-translate.449748/#post-3867804)
	 * 
	 * This method processes all color codes (both hex and '&')
	 * 
	 * @param message Target message to translate.
	 * @return A translated String.
	 */
	public static String color(String message) {
		final Pattern hexPattern = Pattern.compile("#" + "([A-Fa-f0-9]{6})");
		Matcher matcher = hexPattern.matcher(message);
		StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
		while (matcher.find()) {
			String group = matcher.group(1);
			matcher.appendReplacement(buffer,
					COLOR_CHAR + "x" + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1) + COLOR_CHAR
							+ group.charAt(2) + COLOR_CHAR + group.charAt(3) + COLOR_CHAR + group.charAt(4) + COLOR_CHAR
							+ group.charAt(5));
		}

		String processedString = ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
		return processedString;
	}

}
