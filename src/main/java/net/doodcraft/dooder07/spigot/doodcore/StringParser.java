package net.doodcraft.dooder07.spigot.doodcore;

import org.bukkit.ChatColor;

public class StringParser {
    public static String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String removeColor(String message) {
        return ChatColor.stripColor(message);
    }
}
