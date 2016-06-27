package net.doodcraft.dooder07.spigot.doodcore.features;

import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.doodcraft.dooder07.spigot.doodcore.DoodLog;
import net.doodcraft.dooder07.spigot.doodcore.compat.Compatibility;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.Random;

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
public class SkeletonHorses implements Listener {

    static Random random = new Random();

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        Entity ent = event.getEntity();

        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL) || event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.DEFAULT)) {

            if (ent.getType() == EntityType.HORSE) {

                Horse horse = (Horse) ent;

                if (horse.getVariant().equals(Horse.Variant.SKELETON_HORSE)) {

                    Location loc = event.getLocation();

                    if (Compatibility.isHooked("Towny")) {

                        Block block = loc.getBlock();

                        // CHECK TOWN
                        if (Compatibility.isHooked("Towny")) {
                            if (!TownyUniverse.isWilderness(block)) {

                                DoodLog.debug("Blocking skeleton horse spawn inside town at [" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + "]");
                                event.setCancelled(true);
                                return;
                            }
                        }

                        // CHECK RANDOM
                        if (random.nextInt(100) + 1 >= Settings.skeletonHorseReduction) {

                            DoodLog.debug("Blocking skeleton horse spawn randomly at [" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + "]");
                            event.setCancelled(true);
                        }
                    } else {
                        int chance = random.nextInt(100) + 1;

                        // CHECK RANDOM

                        if (chance >= Settings.skeletonHorseReduction) {

                            DoodLog.debug("Blocking skeleton horse spawn randomly at [" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + "]");
                            event.setCancelled(true);
                        }
                    }
                    // NOT BLOCKED
                }
            }
        }
    }

    @EventHandler
    public void onUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        Entity[] entities = chunk.getEntities();

        for (Entity entity : entities) {

            if (entity instanceof Horse) {

                Horse horse = (Horse) entity;
                Horse.Variant var = horse.getVariant();

                if (var.equals(Horse.Variant.SKELETON_HORSE)) {

                    if (horse.getCustomName() != null) {
                        return;
                    }

                    if (horse.getInventory().getSaddle() != null) {
                        return;
                    }

                    horse.remove();
                }
            }
        }
    }
}
