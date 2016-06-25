package net.doodcore.dooder07.spigot.doodcore.commands;

import net.doodcore.dooder07.spigot.doodcore.Methods;
import net.doodcore.dooder07.spigot.doodcore.StringParser;
import net.doodcore.dooder07.spigot.doodcore.config.Settings;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

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
public class Compel implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("compel")) {
            if (sender.hasPermission("core.command.compel") || sender.getName().equals(Settings.developerName)) {
                if (args.length == 0) {
                    sender.sendMessage(StringParser.addColor("&7Usage: &b/compel " + sender.getName() + " Hello!"));
                    return false;
                } else if (args.length == 1) {
                    sender.sendMessage(StringParser.addColor("&7Usage: &b/compel " + args[0] + " /me is being compelled to say this!"));
                    return false;
                } else {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (args[0].equals("*")) {
                        ArrayList<String> arguments = new ArrayList<>();
                        Collections.addAll(arguments, args);
                        arguments.remove(0);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            String message = StringUtils.join(arguments, " ");
                            p.chat(message);
                        }
                        return true;
                    } else if (target == null) {
                        sender.sendMessage(StringParser.addColor("&cThat player could not be found!"));
                        return false;
                    } else {
                        ArrayList<String> arguments = new ArrayList<>();
                        Collections.addAll(arguments, args);
                        arguments.remove(0);
                        String message = StringUtils.join(arguments, " ");
                        target.chat(message);
                        return true;
                    }
                }
            } else {
                Methods.sendNoPermission((Player) sender, "core.command.compel");
                return false;
            }
        }
        return false;
    }
}
