package net.doodcraft.dooder07.spigot.doodcore.features;

import net.doodcraft.dooder07.spigot.doodcore.DoodLog;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

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
public class MobFarming implements Listener {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();
        Location loc = entity.getLocation();
        String location = loc.getX() + ", " + loc.getY() + ", " + loc.getZ();

        if (entity.getType().equals(EntityType.VILLAGER)) {
            if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CURED)) {
                event.setCancelled(true);
                DoodLog.debug("Cancelled cured villager event at " + location);
            }
            if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BREEDING)) {
                event.setCancelled(true);
                DoodLog.debug("Cancelled villager breeding event at " + location);
            }
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        Location loc = entity.getLocation();
        String location = loc.getX() + ", " + loc.getY() + ", " + loc.getZ();

        if (entity.getType().equals(EntityType.IRON_GOLEM)) {
            List<ItemStack> drops = event.getDrops();
            drops.clear();
            DoodLog.debug("Cleared drops of iron golem at " + location);
        }

        if (entity.getType().equals(EntityType.PIG_ZOMBIE)) {
            List<ItemStack> drops = event.getDrops();
            drops.clear();
            DoodLog.debug("Cleared drops of pig zombie at " + location);
        }
    }
}
