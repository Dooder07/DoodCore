package net.doodcraft.dooder07.spigot.doodcore.commands;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
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

import java.util.List;


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
