package net.doodcraft.dooder07.spigot.doodcore.features;

import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import net.doodcraft.dooder07.spigot.doodcore.config.Configuration;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.Player;
import org.bukkit.entity.SplashPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
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
public class PvPManager implements Listener {

    ArrayList<String> teleportProtected = new ArrayList<>();
    ArrayList<String> pvpLogged = new ArrayList<>();

    HashMap<String, Long> inCombat = new HashMap<>();
    HashMap<String, Integer> combatTasks = new HashMap<>();

    public static HashMap<String, Long> timeReduction = new HashMap<>();

    // Todo: fix the noob timer and optimize everything else..

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String[] message = event.getMessage().split(" ");

        for (String command : Settings.combatBlockedCommands) {
            if (message[0].toLowerCase().contains(command.toLowerCase())) {
                if (inCombat.containsKey(event.getPlayer().getName())) {
                    event.getPlayer().sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cYou can't use this until combat is over!"));
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.isCancelled()) {
            return;
        }

        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            Player aggressor = null;

            // todo: add enderpearls and fix lingering potion

            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    aggressor = (Player) arrow.getShooter();
                }
            }

            if (event.getDamager() instanceof SplashPotion) {
                SplashPotion potion = (SplashPotion) event.getDamager();
                if (potion.getShooter() instanceof Player) {
                    aggressor = (Player) potion.getShooter();
                }
            }

            if (event.getDamager() instanceof LingeringPotion) {
                LingeringPotion potion = (LingeringPotion) event.getDamager();
                if (potion.getShooter() instanceof Player) {
                    aggressor = (Player) potion.getShooter();
                }
            }

            if (event.getDamager() instanceof Player) {
                aggressor = (Player) event.getDamager();
            }

            if (aggressor != null) {

                if (aggressor.equals(victim)) {
                    return;
                }

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

                    aggressor.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cYou cannot PvP for " + aggressorRemaining + " &cmore minutes!"));
                    aggressor.updateInventory();
                    event.setCancelled(true);

                    return;
                }

                if (aggressor.getGameMode().equals(GameMode.CREATIVE)) {
                    aggressor.sendMessage(StringParser.addColor("&7Your attack seems to have no effect.."));
                    event.setCancelled(true);
                    return;
                }

                if (teleportProtected.contains(aggressor.getName()) || teleportProtected.contains(victim.getName())) {
                    aggressor.sendMessage(StringParser.addColor("&7Your attack seems to have no effect.."));
                    event.setCancelled(true);
                    return;
                }

                runCombatTask(victim);
                runCombatTask(aggressor);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        PlayerTeleportEvent.TeleportCause cause = event.getCause();

        if (cause.equals(PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT)) {
            return;
        }

        if (cause.equals(PlayerTeleportEvent.TeleportCause.SPECTATE)) {
            return;
        }

        if (cause.equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            return;
        }

        if (player.getGameMode().equals(GameMode.SURVIVAL)) {
            if (!teleportProtected.contains(player.getName())) {
                teleportProtected.add(player.getName());

                Bukkit.getScheduler().runTaskLater(DoodCorePlugin.plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (teleportProtected.contains(player.getName())) {
                            teleportProtected.remove(player.getName());
                        }
                    }
                }, 300L);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        addNoob(player);
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

        if (pvpLogged.contains(event.getPlayer().getName())) {
            if (!event.getPlayer().isDead()) {

                Inventory inv = event.getPlayer().getInventory();

                for (ItemStack i : inv.getContents()) {
                    if (i != null) {
                        event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), i);
                    }
                }

                event.getPlayer().getInventory().clear();
                event.getPlayer().setHealth(0.0);

                Bukkit.broadcastMessage(StringParser.addColor(event.getPlayer().getDisplayName() + " &elogged out during PvP and paid the price! :o"));

                if (pvpLogged.contains(event.getPlayer().getName())) {
                    pvpLogged.remove(event.getPlayer().getName());
                }

                if (inCombat.containsKey(event.getPlayer().getName())) {
                    inCombat.remove(event.getPlayer().getName());
                }

                if (combatTasks.containsKey(event.getPlayer().getName())) {
                    Bukkit.getScheduler().cancelTask(combatTasks.get(event.getPlayer().getName()));
                    combatTasks.remove(event.getPlayer().getName());
                }
            }
        } else {
            if (inCombat.containsKey(event.getPlayer().getName())) {
                inCombat.remove(event.getPlayer().getName());
            }

            if (combatTasks.containsKey(event.getPlayer().getName())) {
                Bukkit.getScheduler().cancelTask(combatTasks.get(event.getPlayer().getName()));
                combatTasks.remove(event.getPlayer().getName());
            }
        }
    }

    public void runCombatTask(Player player) {
        long started = System.currentTimeMillis();

        if (inCombat.containsKey(player.getName())) {
            inCombat.remove(player.getName());
            inCombat.put(player.getName(), started);
            return;
        }

        if (!pvpLogged.contains(player.getName())) {

            player.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cYou have entered mortal kombat!!\n&8:: &eTeleportation commands are disabled!\n&8:: &eLogging out before it's over &nwill&e result in DEATH!"));

            pvpLogged.add(player.getName());
            inCombat.put(player.getName(), started);

            combatTasks.put(player.getName(), Bukkit.getScheduler().scheduleSyncRepeatingTask(DoodCorePlugin.plugin, new Runnable() {
                @Override
                public void run() {
                    if (pvpLogged.contains(player.getName())) {
                        if ((System.currentTimeMillis() - inCombat.get(player.getName())) > 29000) {

                            player.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aYou are no longer in combat!"));

                            pvpLogged.remove(player.getName());

                            inCombat.remove(player.getName());

                            Bukkit.getScheduler().cancelTask(combatTasks.get(player.getName()));
                            combatTasks.remove(player.getName());
                        }
                    }
                }
            }, 600L, 20L));
        }
    }

    public static void addNoob(Player player) {
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
            addNoob(player);
        }
    }
}
