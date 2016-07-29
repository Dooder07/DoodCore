package net.doodcraft.dooder07.spigot.doodcore.commands;

import mkremins.fanciful.FancyMessage;
import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.spigot.doodcore.DoodLog;
import net.doodcraft.dooder07.spigot.doodcore.Methods;
import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Core implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("core")) {

            if (args.length == 0) {

                FancyMessage msg = new FancyMessage(StringParser.addColor("&6&l&oCore&7 v" + DoodCorePlugin.plugin.getDescription().getVersion()));
                msg.tooltip(StringParser.addColor("&8Author: &6Dooder07\n&dFor DreadCraft &5<3"));

                if (sender instanceof Player) {
                    Methods.sendFancyMessage((Player) sender, msg);
                } else {
                    DoodLog.log("DoodCore", StringParser.addColor("&6&l&oCore&7 v" + DoodCorePlugin.plugin.getDescription().getVersion() + "\n&8Author: &6Dooder07\n&8Website: &bhttp://doodcraft.net/"));
                }

                if (sender.hasPermission("core.command.reload") || sender.getName().equals(Settings.developerName)) {
                    sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &7Valid arguments: &breload"));
                }

                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("core.command.reload") || sender.getName().equals(Settings.developerName)) {
                    try {
                        if (sender instanceof Player) {
                            sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aReloading Core..."));
                        }

                        DoodLog.log("DoodCore", "Reloading Core..");
                        DoodCorePlugin.reload();

                        if (sender instanceof Player) {
                            sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aReload successful!"));
                        }

                        DoodLog.log("DoodCore", "&aReload successful!");
                        return true;

                    } catch (Exception ex) {

                        if (sender instanceof Player) {
                            sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cThere was an error during the reload. Check the console log."));
                        }

                        DoodLog.printError("DoodCore", "&cKore couldn't reload!", ex);
                        return false;
                    }
                } else {
                    Methods.sendNoPermission((Player) sender, "core.command.reload");
                    return false;
                }
            }

            sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cInvalid arguments!"));

            if (sender.hasPermission("core.command.reload") || sender.getName().equals(Settings.developerName)) {
                sender.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &7Valid arguments: &breload"));
            }
            return true;
        }
        return false;
    }
}
