package net.doodcraft.dooder07.spigot.doodcore.features;

import net.doodcraft.dooder07.spigot.doodcore.DoodLog;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

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

        if (entity.getType().equals(EntityType.VILLAGER)) {
            if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CURED) || event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BREEDING)) {
                Villager villager = (Villager) entity;
                List<MerchantRecipe> trades = villager.getRecipes();
                for (MerchantRecipe trade : trades) {
                    if (trade.getResult().getType().equals(Material.EMERALD)) {
                        DoodLog.debug("Cancelled villager breeding with emerald trade.");
                        event.setCancelled(true);
                    }

                    if (trade.getResult().containsEnchantment(Enchantment.MENDING)) {
                        DoodLog.debug("Cancelled villager breeding with mending trade.");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        if (entity.getType().equals(EntityType.IRON_GOLEM)) {
            List<ItemStack> drops = event.getDrops();
            drops.clear();
            drops.add(new ItemStack(Material.JACK_O_LANTERN));
        }

        if (entity.getType().equals(EntityType.PIG_ZOMBIE)) {
            List<ItemStack> drops = event.getDrops();
            drops.clear();
            drops.add(new ItemStack(Material.ROTTEN_FLESH));
            drops.add(new ItemStack(Material.GOLD_SWORD));
        }
    }
}
