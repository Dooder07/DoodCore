package net.doodcraft.dooder07.bungeecord.doodcore;

import net.alpenblock.bungeeperms.BungeePerms;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeLog implements Listener {
    public static void log(String plugin, String message) {
        System.out.println(addColor("&8[&b" + plugin + "&8] &7" + message));
    }

    public static void log(String message) {
        System.out.println(addColor("&7" + message));
    }

    public static String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String stripColor(String message) {
        message = addColor(message);
        return ChatColor.stripColor(message);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String name = player.getName();

        if (BungeePerms.getInstance().getPermissionsManager() != null) {
            name = BungeePerms.getInstance().getPermissionsManager().getUser(name).getGroupByLadder("default").getPrefix() + name;
            String message = event.getMessage();
            String firstString = message.substring(0,1);

            if (firstString.equals("/")) {
                log("&7" + stripColor(name) + " &7issued server command " + event.getMessage());
            } else {
                log(name + "&8: &7" + event.getMessage());
            }
        }
    }
}
