package net.doodcraft.dooder07.spigot.doodcore.features;

import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
public class Lobby implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (Bukkit.getWorld("Lobby") != null) {
            if (player.getWorld().equals(Bukkit.getWorld("Lobby"))) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(DoodCorePlugin.plugin, new Runnable() {

                    @Override
                    public void run() {
                        if (!player.hasPermission("core.admin.bypass")) {
                            player.teleport(player.getWorld().getSpawnLocation());
                        }
                    }
                },1L);
                Bukkit.getScheduler().scheduleSyncDelayedTask(DoodCorePlugin.plugin, new Runnable() {

                    @Override
                    public void run() {
                        player.playEffect(player.getWorld().getSpawnLocation(), Effect.RECORD_PLAY, Material.RECORD_4);
                    }
                },25L);
            }
        }
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (Bukkit.getWorld("Lobby") != null) {
            if (player.getWorld().equals(Bukkit.getWorld("Lobby"))) {
                if (!player.hasPermission("core.admin.bypass")) {
                    if (event.getTo().getY() <= 57) {
                        player.teleport(player.getWorld().getSpawnLocation());
                    }
                    if (event.getTo().getY() >= 75) {
                        player.teleport(player.getWorld().getSpawnLocation());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();

        if (Bukkit.getWorld("Lobby") != null) {
            if (player.getWorld().equals(Bukkit.getWorld("Lobby"))) {
                event.setFoodLevel(20);
                event.setCancelled(true);
            }
        }
    }
}
