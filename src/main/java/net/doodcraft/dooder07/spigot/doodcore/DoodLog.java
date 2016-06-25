package net.doodcraft.dooder07.spigot.doodcore;

import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The MIT License (MIT)
 * -
 * Copyright (c) 2016 Conor O'Shields
 * -
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * -
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * -
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
            dev.sendMessage(StringParser.addColor("&3C&9O&8R&0E:&7 " + message));
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
