package net.doodcraft.dooder07.bungeecord.doodcore.features;

import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.User;
import net.doodcraft.dooder07.bungeecord.doodcore.BungeeLog;
import net.doodcraft.dooder07.bungeecord.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.bungeecord.doodcore.config.BungeeConfig;
import net.doodcraft.dooder07.bungeecord.doodcore.config.Settings;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.io.File;
import java.io.IOException;

public class MaintenanceModeCommand extends Command {

    public MaintenanceModeCommand() {
        super("mm");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            User user = BungeePerms.getInstance().getPermissionsManager().getUser(commandSender.getName());

            if (user.hasPerm("core.command.bungee.maintenancemode")) {
                try {
                    if (runToggle()) {
                        commandSender.sendMessage(new TextComponent(BungeeLog.addColor("&eMaintenance Mode enabled!")));
                    } else {
                        commandSender.sendMessage(new TextComponent(BungeeLog.addColor("&eMaintenance Mode disabled!")));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                commandSender.sendMessage(new TextComponent(BungeeLog.addColor("&cNo permission.")));
            }
        } else {
            try {
                if (runToggle()) {

                    BungeeLog.log("Maintenance Mode enabled!");

                    ProxyServer.getInstance().getPlayers().stream().filter(p -> !BungeePerms.getInstance().getPermissionsChecker().hasPerm(p.getName(), "core.command.bungee.maintmode.bypass")).forEach(p -> {
                        p.disconnect(new TextComponent(BungeeLog.addColor("&cDreadcraft is currently under maintenance!\n&eCheck back later. :)")));
                    });
                } else {
                    BungeeLog.log("Maintenance Mode disabled!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean runToggle() throws IOException {
        Configuration config = Settings.getConfig();

        if (config.getBoolean("MaintenanceMode") || config.getString("MaintenanceMode") == null) {
            config.set("MaintenanceMode", false);
            BungeeConfig.saveConfig(config, new File(DoodCorePlugin.plugin.getDataFolder() + File.separator + "config.yml"));
            return false;
        } else {
            config.set("MaintenanceMode", true);
            BungeeConfig.saveConfig(config, new File(DoodCorePlugin.plugin.getDataFolder() + File.separator + "config.yml"));
            return true;
        }
    }
}
