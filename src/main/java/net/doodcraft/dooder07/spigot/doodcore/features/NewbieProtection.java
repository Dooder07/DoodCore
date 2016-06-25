package net.doodcraft.dooder07.spigot.doodcore.features;

import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import net.doodcraft.dooder07.spigot.doodcore.config.Configuration;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.HashMap;

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
public class NewbieProtection implements Listener {

    public static HashMap<String, Long> timeReduction = new HashMap<>();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        Entity damager = event.getDamager();
        Player aggressor = null;

        Arrow arrow = null;
        Egg egg = null;
        Snowball snowball = null;

        if (event.getDamager() instanceof Arrow) {
            arrow = (Arrow) event.getDamager();

            if (arrow.getShooter() instanceof Player) {
                aggressor = (Player) arrow.getShooter();
            }
        } else if (event.getDamager() instanceof Egg) {
            egg = (Egg) event.getDamager();

            if (egg.getShooter() instanceof Player) {
                aggressor = (Player) egg.getShooter();
            }
        } else if (event.getDamager() instanceof Snowball) {
            snowball = (Snowball) event.getDamager();

            if (snowball.getShooter() instanceof Player) {
                aggressor = (Player) snowball.getShooter();
            }
        } else {
            if (damager instanceof Player) {
                aggressor = (Player) event.getDamager();
            }
        }

        if (aggressor != null && event.getEntity() instanceof Player) {

            Player victim = (Player) event.getEntity();

            String victimName = victim.getName().toLowerCase();
            String aggressorName = aggressor.getName().toLowerCase();

            Configuration victimData = new Configuration(DoodCorePlugin.plugin.getDataFolder() + File.separator + "pvp" + File.separator + victimName + ".yml");
            Configuration aggressorData = new Configuration(DoodCorePlugin.plugin.getDataFolder() + File.separator + "pvp" + File.separator + aggressorName + ".yml");

            int victimRemaining = victimData.getInteger(victimName + ".timeLeft");
            int aggressorRemaining = aggressorData.getInteger(aggressorName + ".timeLeft");

            if (victimRemaining >= 1) {

                long currentTime = System.currentTimeMillis();
                long startTime = timeReduction.get(victimName);
                long newTime = currentTime - startTime;
                newTime = (newTime / 1000) / 60;

                if (newTime >= 1) {
                    timeReduction.put(victimName, System.currentTimeMillis());
                    victimData.decrement(victimName + ".timeLeft", (int) newTime);
                    victimData.save();
                }

                if (arrow != null) {
                    arrow.remove();
                }

                if (snowball != null) {
                    snowball.remove();
                }

                if (egg != null) {
                    egg.remove();
                }

                aggressor.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cThey're protected for " + victimRemaining + " &cmore minutes!"));
                aggressor.updateInventory();
                event.setCancelled(true);

                return;
            }

            if (aggressorRemaining >= 1) {

                long currentTime = System.currentTimeMillis();
                long startTime = timeReduction.get(aggressorName);
                long newTime = currentTime - startTime;
                newTime = (newTime / 1000) / 60;

                if (newTime >= 1) {
                    timeReduction.put(aggressorName, System.currentTimeMillis());
                    aggressorData.decrement(aggressorName + ".timeLeft", (int) newTime);
                    aggressorData.save();
                }

                if (arrow != null) {
                    arrow.remove();
                }

                if (snowball != null) {
                    snowball.remove();
                }

                if (egg != null) {
                    egg.remove();
                }

                aggressor.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cYou cannot PvP for " + aggressorRemaining + " &cmore minutes!"));
                aggressor.updateInventory();
                event.setCancelled(true);

                return;
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String name = event.getPlayer().getName().toLowerCase();
        Configuration data = new Configuration(DoodCorePlugin.plugin.getDataFolder() + File.separator + "pvp" + File.separator + name + ".yml");

        if (timeReduction.containsKey(name)) {
            long currentTime = System.currentTimeMillis();
            long startTime = timeReduction.get(name);
            long newTime = currentTime - startTime;
            newTime = (newTime / 1000) / 60;

            if (newTime >= 1) {
                timeReduction.put(name, System.currentTimeMillis());
                data.decrement(name + ".timeLeft", (int) newTime);
                data.save();
            }

            timeReduction.remove(name);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        add(player);
    }

    public static void add(Player player) {
        String name = player.getName().toLowerCase();

        Configuration data = new Configuration(DoodCorePlugin.plugin.getDataFolder() + File.separator + "pvp" + File.separator + name + ".yml");

        if (data.get(name) == null || data.get(name + ".timeLeft") == null) {

            if (player.getStatistic(Statistic.DAMAGE_TAKEN) >= 1) {
                data.add(name + ".timeLeft", 0);
                data.save();

                long currentTime = System.currentTimeMillis();
                timeReduction.put(name, currentTime);
            } else {
                data.add(name + ".timeLeft", Settings.pvpNewbTime);
                data.save();

                long currentTime = System.currentTimeMillis();
                timeReduction.put(name, currentTime);
            }

        } else {
            long currentTime = System.currentTimeMillis();
            timeReduction.put(name, currentTime);
        }
    }

    public static void addAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            add(player);
        }
    }
}
