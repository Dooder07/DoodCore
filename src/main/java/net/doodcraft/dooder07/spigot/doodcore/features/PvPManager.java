package net.doodcraft.dooder07.spigot.doodcore.features;

import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

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

    // TODO: Move from expiring tasks to adding epoch times to a hashmap and checking. The current way here sucks and is temporary.

    ArrayList<String> teleportProtected = new ArrayList<>();
    ArrayList<String> pvpLogged = new ArrayList<>();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (isPvPing(event, (Player) event.getDamager())) {

                if (event.isCancelled()) {
                    return;
                }

                if (teleportProtected.contains(event.getDamager().getName()) || teleportProtected.contains(event.getEntity().getName())) {
                    event.getDamager().sendMessage(StringParser.addColor("&7Your attack seems to have no effect.."));
                    event.setCancelled(true);
                    return;
                }


                event.getDamager().sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cYou are in combat. Don't log out for 30 seconds!"));
                pvpLogged.add(event.getDamager().getName());

                Bukkit.getScheduler().runTaskLater(DoodCorePlugin.plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (pvpLogged.contains(event.getDamager().getName())) {
                            event.getDamager().sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aCombat is now over!"));
                            pvpLogged.remove(event.getDamager().getName());
                        }
                    }
                }, 600L);
            }
        }

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            if (isBeingPvPed(event, (Player) event.getEntity(), (Player) event.getDamager())) {

                if (event.isCancelled()) {
                    return;
                }

                if (teleportProtected.contains(event.getDamager().getName()) || teleportProtected.contains(event.getEntity().getName())) {
                    event.setCancelled(true);
                    return;
                }

                event.getEntity().sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cYou are in combat. Don't log out for 30 seconds!"));
                pvpLogged.add(event.getEntity().getName());

                Bukkit.getScheduler().runTaskLater(DoodCorePlugin.plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (pvpLogged.contains(event.getEntity().getName())) {
                            event.getEntity().sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aCombat is now over!"));
                            pvpLogged.remove(event.getEntity().getName());
                        }
                    }
                }, 600L);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        PlayerTeleportEvent.TeleportCause cause = event.getCause();

        if (pvpLogged.contains(player.getName())) {
            return;
        }

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
    public void onQuit(PlayerQuitEvent event) {
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
            }
        }
    }

    public static Boolean isBeingPvPed(EntityDamageByEntityEvent event, Player player, Player attacker) {
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

            if (aggressor.equals(attacker)) {
                Player victim = (Player) event.getEntity();

                if (player.equals(victim)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Boolean isPvPing(EntityDamageByEntityEvent event, Player player) {
        Entity damager = event.getDamager();
        Player aggressor = null;

        Arrow arrow;
        Egg egg;
        Snowball snowball;

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
            if (player.equals(aggressor)) {
                return true;
            }
        }

        return false;
    }
}
