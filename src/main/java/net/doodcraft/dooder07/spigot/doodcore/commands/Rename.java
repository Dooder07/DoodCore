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
