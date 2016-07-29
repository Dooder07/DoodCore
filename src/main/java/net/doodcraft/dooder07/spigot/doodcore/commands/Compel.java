package net.doodcraft.dooder07.spigot.doodcore.commands;

import net.doodcraft.dooder07.spigot.doodcore.Methods;
import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

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
