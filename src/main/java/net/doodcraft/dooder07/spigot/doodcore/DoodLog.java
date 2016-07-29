package net.doodcraft.dooder07.spigot.doodcore;

import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class DoodLog {

    public static void log(String plugin, String message) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        message = "&8[&b" + plugin + "&8]&r " + message;

        if (Settings.colorfulLogging) {
            message = StringParser.addColor(message);
            console.sendMessage(message);
        } else {
            message = StringParser.removeColor(StringParser.addColor(message));
            console.sendMessage(message);
        }
    }

    public static void debug(String message) {
        Player dev = Bukkit.getPlayer(Settings.developerName);

        if (dev != null && Settings.messageDeveloper) {
            dev.sendMessage(StringParser.addColor("&bC&3O&9R&8E: &7" + message));
        }

        if (Settings.debugMode) {
            if (Settings.colorfulLogging) {
                message = StringParser.addColor(message);
                log("Core|Debug", message);
            } else {
                message = StringParser.removeColor(message);
                log("Core|Debug", message);
            }
        }
    }

    public static void printError(String plugin, String warning, Throwable ex) {
        log(plugin, " ");
        log(plugin, "&c--Send this to Dooder07 right away!!--");
        log(plugin, " ");
        log(plugin, "&eERROR: &c" + warning);
        log(plugin, " ");
        log(plugin, "                    &c======= Copy and Paste =======");
        log(plugin, " ");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);

        for (String l : sw.toString().replace("\r", "").split("\n")) {
            log(plugin, l);
            pw.close();

            // Ex-in-ception!
            try {
                sw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        log(plugin, " ");
        log(plugin, "                    &c======= Copy and Paste =======");
        log(plugin, " ");
    }
}
