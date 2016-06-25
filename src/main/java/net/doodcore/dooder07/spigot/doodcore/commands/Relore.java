package net.doodcore.dooder07.spigot.doodcore.commands;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.doodcore.dooder07.spigot.doodcore.Methods;
import net.doodcore.dooder07.spigot.doodcore.StringParser;
import net.doodcore.dooder07.spigot.doodcore.config.Settings;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

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
public class Relore implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage(StringParser.addColor("&cConsole cannot relore items."));
        }

        if (player != null) {
            if (command.getName().equalsIgnoreCase("relore")) {
                if (player.hasPermission("core.command.relore") || player.getName().equals(Settings.developerName)) {

                    if (args.length == 0) {
                        player.sendMessage(StringParser.addColor("&7Usage: &b/relore &fSome fancy item &olore&f!\\n&aThis is a another line!"));
                    } else {
                        ItemStack item = player.getItemInHand();
                        ItemMeta meta = item.getItemMeta();

                        String arguments = StringParser.addColor(StringUtils.join(args, " "));

                        List<String> lore = Lists.newArrayList(Splitter.on("\\n").trimResults().split(arguments));
                        meta.setLore(lore);

                        item.setItemMeta(meta);

                        player.sendMessage(StringParser.addColor("&7Item relored:"));
                        for (String line : lore) {
                            player.sendMessage(StringParser.addColor("&5&o" + line));
                        }
                    }
                } else {
                    Methods.sendNoPermission(player, "core.command.relore");
                }
            }
        }
        return false;
    }
}
