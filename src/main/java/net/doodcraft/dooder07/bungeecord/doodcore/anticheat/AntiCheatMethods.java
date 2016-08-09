package net.doodcraft.dooder07.bungeecord.doodcore.anticheat;

import net.doodcraft.dooder07.bungeecord.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.bungeecord.doodcore.config.Settings;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AntiCheatMethods {

    public static String getTime(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getPlayerIP(ProxiedPlayer player) {
        String ip = String.valueOf(player.getAddress().getAddress().getHostAddress());
        return ip.replaceAll("/", "");
    }

    public static void logBan(ProxiedPlayer player, String banReason) throws IOException {
        FileWriter fw = new FileWriter(DoodCorePlugin.plugin.getDataFolder() + File.separator + "anticheat-log.yml");

        String line = "[" + getTime("yyyy/MM/dd HH:mm:ss") + "] [" + player.getName() + " / " + getPlayerIP(player) + "]: " + banReason;

        for (int i = 0; i < 10; i++) {
            fw.write(line);
        }

        fw.close();
    }

    public static void ipBan(ProxiedPlayer player, String reason, String banReason) throws IOException {
        logBan(player, banReason);
        ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "ipban -s " + getPlayerIP(player) + " " + Settings.pluginPrefix + " &c" + reason);
    }

    public static void ipBan(ProxiedPlayer player, String banReason) throws IOException {
        logBan(player, banReason);
        ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "ipban -s " + getPlayerIP(player) + " " + Settings.pluginPrefix + " &cThis ban cannot be appealed.\"");
    }
}
