package net.doodcraft.dooder07.spigot.doodcore.features;

import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import net.doodcraft.dooder07.spigot.doodcore.compat.Compatibility;
import net.doodcraft.dooder07.spigot.doodcore.compat.Vault;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashMap;
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
public class PlayerWelcome implements Listener {

    public static List<String> greetable = new ArrayList<>();

    public static HashMap<String, String> rewarded = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {

            if (!greetable.contains(player.getName())) {

                Bukkit.getScheduler().runTaskLater(DoodCorePlugin.plugin, () -> {

                    if (Compatibility.isHooked("Vault")) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (!Vault.permission.getPrimaryGroup(p).equalsIgnoreCase("guest")) {
                                p.sendMessage(StringParser.addColor("&dWelcome " + player.getDisplayName() + " &dwithin 60 seconds for a reward!"));
                            }
                        }
                    }

                    greetable.add(player.getName());

                    startRemoveTask(player);
                }, 20L);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();

        String message = e.getMessage().toLowerCase();

        Settings.validWelcomeList.stream().filter(greeting -> message.toLowerCase().contains(greeting.toLowerCase())).forEach(greeting -> {

            for (Player p : Bukkit.getOnlinePlayers()) {

                String name = p.getName();

                if (message.toLowerCase().contains(name.toLowerCase())) {

                    if (greetable.contains(name)) {

                        if (rewarded.get(player.getName()) != null && rewarded.get(player.getName()).equals(name)) {

                            player.sendMessage(StringParser.addColor(Settings.pluginPrefix + "&cYou've already greeted " + p.getDisplayName() + "&c!"));

                        } else {

                            player.sendMessage(StringParser.addColor(Settings.pluginPrefix + "&aThank you for welcoming " + p.getDisplayName() + "&a to " + Settings.serverName + "!"));

                            giveReward(player, p);

                            rewarded.put(player.getName(), p.getName());
                        }
                    }
                }
            }
        });
    }

    public void startRemoveTask(Player player) {

        Bukkit.getScheduler().runTaskLater(DoodCorePlugin.plugin, () -> {

            if (greetable.contains(player.getName())) {
                greetable.remove(player.getName());
            }
        }, 1200L);
    }

    public void giveReward(Player greeter, Player greeted) {

        if (Compatibility.isHooked("Vault")) {
            greeter.sendMessage(StringParser.addColor(Settings.pluginPrefix + "&aBoth of you earned a bonus &6$10&a!"));

            Economy econ = Vault.economy;

            econ.depositPlayer(greeter, 10);
            econ.depositPlayer(greeted, 10);
        } else {
            greeter.sendMessage(StringParser.addColor(Settings.pluginPrefix + "&cThere was an error! Let an admin know!"));
        }
    }
}
