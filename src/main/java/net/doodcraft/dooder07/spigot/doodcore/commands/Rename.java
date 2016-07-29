package net.doodcraft.dooder07.spigot.doodcore.commands;

import net.doodcraft.dooder07.spigot.doodcore.Methods;
import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Rename implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage(StringParser.addColor("&cConsole cannot rename items."));
        }

        if (player != null) {
            if (command.getName().equalsIgnoreCase("rename")) {
                if (player.hasPermission("core.command.rename") || player.getName().equals(Settings.developerName)) {

                    if (args.length == 0) {
                        player.sendMessage(StringParser.addColor("&7Usage: &b/rename Fancy new item name!"));
                    } else {
                        ItemStack item = player.getInventory().getItemInMainHand();

                        ItemMeta meta = item.getItemMeta();
                        String name = StringUtils.join(args, " ");

                        meta.setDisplayName(StringParser.addColor("&r" + name));
                        item.setItemMeta(meta);

                        player.sendMessage(StringParser.addColor("&7Item renamed: &r" + name));
                    }
                } else {
                    Methods.sendNoPermission(player, "core.command.rename");
                }
            }
        }

        return false;
    }
}
