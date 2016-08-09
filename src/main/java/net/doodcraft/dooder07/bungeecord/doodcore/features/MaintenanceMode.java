package net.doodcraft.dooder07.bungeecord.doodcore.features;

import net.alpenblock.bungeeperms.BungeePerms;
import net.doodcraft.dooder07.bungeecord.doodcore.BungeeLog;
import net.doodcraft.dooder07.bungeecord.doodcore.config.Settings;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class MaintenanceMode implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPing(ProxyPingEvent event) {
        // check toggle
        Configuration config = Settings.getConfig();

        if (config.getBoolean("MaintenanceMode")) {
            ServerPing serverPing = event.getResponse();

            serverPing.setDescriptionComponent(new TextComponent(BungeeLog.addColor("&cDreadcraft is currently under maintenance!\n&eCheck back later. :)")));
            event.setResponse(serverPing);
        }
    }

    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        Configuration config = Settings.getConfig();

        String player = event.getConnection().getName();

        if (config.getBoolean("MaintenanceMode")) {

            if (BungeePerms.getInstance() != null) {
                if (!BungeePerms.getInstance().getPermissionsChecker().hasPerm(player, "core.command.bungee.maintmode.bypass")) {
                    event.setCancelReason(BungeeLog.addColor("&cDreadcraft is currently under maintenance!\n&eCheck back later. :)"));
                    event.setCancelled(true);
                }
            } else {
                event.setCancelReason(BungeeLog.addColor("&cDreadcraft is currently under maintenance!\n&eCheck back later. :)"));
                event.setCancelled(true);
            }
        }
    }
}
