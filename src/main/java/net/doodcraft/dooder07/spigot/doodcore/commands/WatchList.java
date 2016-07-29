package net.doodcraft.dooder07.spigot.doodcore.commands;

import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.spigot.doodcore.DoodLog;
import net.doodcraft.dooder07.spigot.doodcore.Methods;
import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import net.doodcraft.dooder07.spigot.doodcore.config.Configuration;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class WatchList implements CommandExecutor, Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName().toLowerCase();

        Configuration watchlist = new Configuration(DoodCorePlugin.plugin.getDataFolder() + File.separator + "watchlist.yml");

        if (watchlist.contains(name)) {
            String reason = watchlist.getString(name + ".Reason");

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("core.watchlist")) {
                    if (!p.equals(player)) {
                        p.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &c" + player.getName() + " is on the watchlist: &e" + reason));
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("watchlist")) {
            if (sender.hasPermission("core.command.watchlist") || sender.getName().equals(Settings.developerName)) {
                try {
                    Configuration watchlist = new Configuration(DoodCorePlugin.plugin.getDataFolder() + File.separator + "watchlist.yml");

                    if (args.length == 0) {
                        sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " /watchlist <add/remove/list/online>"));
                        return true;
                    }

                    if (args.length >= 1) {

                        // check for add
                        if ((args[0].equalsIgnoreCase("add"))) {
                            if (args.length >= 3) {
                                if (watchlist.contains(args[1].toLowerCase())) {
                                    String name = args[1].toLowerCase();

                                    String oldReason = watchlist.getString(name + ".Reason");

                                    watchlist.remove(name);
                                    watchlist.save();

                                    ArrayList<String> arguments = new ArrayList<>();
                                    Collections.addAll(arguments, args);
                                    arguments.remove(0);
                                    arguments.remove(0);

                                    String reason = StringUtils.join(arguments, " ");

                                    watchlist.add(name + ".Name", args[1]);
                                    watchlist.add(name + ".Reason", reason);
                                    watchlist.save();

                                    if (sender instanceof Player) {
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (!p.equals(sender)) {
                                                if (p.hasPermission("core.watchlist")) {
                                                    p.sendMessage(StringParser.addColor(Settings.pluginPrefix + ((Player) sender).getDisplayName() + " &aupdated &e" + args[1] + "'s &awatchlist reason!"));
                                                    p.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aOld Reason: '&c" + oldReason + "&a'"));
                                                    p.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aNew Reason: '&c" + reason + "&a'"));
                                                }
                                            }
                                        }
                                    }

                                    sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aReason updated for &e" + args[1] + "&a!"));
                                    sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aOld Reason: '&c" + oldReason + "&a'"));
                                    sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aNew Reason: '&c" + reason + "&a'"));
                                    return true;
                                } else {
                                    String name = args[1].toLowerCase();

                                    ArrayList<String> arguments = new ArrayList<>();
                                    Collections.addAll(arguments, args);
                                    arguments.remove(0);
                                    arguments.remove(0);

                                    String reason = StringUtils.join(arguments, " ");

                                    watchlist.add(name + ".Name", args[1]);
                                    watchlist.add(name + ".Reason", reason);
                                    watchlist.save();

                                    if (sender instanceof Player) {
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (!p.equals(sender)) {
                                                if (p.hasPermission("core.watchlist")) {
                                                    p.sendMessage(StringParser.addColor(Settings.pluginPrefix + " " + ((Player) sender).getDisplayName() + " &aadded &e" + args[1] + " &ato the watchlist!"));
                                                    p.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aReason: '&c" + reason + "&a'"));
                                                }
                                            }
                                        }
                                    }

                                    sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aAdded &e" + args[1] + " &ato the watchlist!"));
                                    sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aReason: '&c" + reason + "&a'"));
                                    return true;
                                }
                            } else {
                                sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &7/watchlist add <name> <reason>"));
                                return false;
                            }
                        }

                        // check for remove
                        if ((args[0].equalsIgnoreCase("remove"))) {
                            if (args.length == 2) {
                                if (watchlist.contains(args[1].toLowerCase())) {
                                    watchlist.remove(args[1].toLowerCase());
                                    watchlist.save();
                                    sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aRemoved successfully!"));
                                    return true;
                                } else {
                                    sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cThat player could not be found on the list!"));
                                    return false;
                                }
                            } else {
                                sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &7/watchlist remove <name>"));
                                return true;
                            }
                        }

                        // check for list
                        if ((args[0].equalsIgnoreCase("list"))) {

                            Set<String> names = watchlist.getKeys(false);

                            sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &a&oListing all watched players.."));

                            if (names.size() > 0) {
                                for (String name : names) {
                                    sender.sendMessage(StringParser.addColor("&8:: &e" + watchlist.get(name + ".Name") + ": &c" + watchlist.get(name + ".Reason")));
                                }
                                return true;
                            } else {
                                sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &eNobody is on the watchlist!"));
                                return false;
                            }
                        }

                        // check online players
                        if ((args[0].equalsIgnoreCase("online"))) {
                            sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &a&oListing all watched players currently online.."));

                            List<String> online = new ArrayList<>();

                            for (Player p : Bukkit.getOnlinePlayers()) {
                                String name = p.getName().toLowerCase();
                                if (watchlist.contains(name)) {
                                    online.add(StringParser.addColor("&8:: &e" + watchlist.get(name + ".Name") + ": &c" + watchlist.get(name + ".Reason")));
                                }
                            }

                            if (online.size() > 0) {
                                for (String string : online) {
                                    sender.sendMessage(string);
                                }
                                return true;
                            } else {
                                sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &eNobody online is on the watchlist!"));
                                return false;
                            }
                        }

                        sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &7/watchlist <add/remove/list/online>"));
                        return true;
                    }
                } catch (Exception ex) {
                    DoodLog.printError("DoodCore", "Error executing /watchlist", ex);
                    return false;
                }
            } else {
                Methods.sendNoPermission((Player) sender, "core.command.watchlist");
                return false;
            }
        }
        return false;
    }
}