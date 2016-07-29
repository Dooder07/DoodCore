package net.doodcraft.dooder07.bungeecord.doodcore;

import net.md_5.bungee.api.ChatColor;

public class BungeeLog {
    public static void log(String plugin, String message) {
        System.out.println(addColor("&8[&b" + plugin + "&8] &7" + message));
    }

    public static String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String stripColor(String message) {
        message = addColor(message);
        return ChatColor.stripColor(message);
    }
}
