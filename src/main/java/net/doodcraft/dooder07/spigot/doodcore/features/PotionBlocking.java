package net.doodcraft.dooder07.spigot.doodcore.features;

import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

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
public class PotionBlocking implements Listener {

    /**
     * Block potion dispensing for specific potions.
     */
    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        ItemStack item = event.getItem();
        Material mat = null;

        if (item.getType() != null) {
            mat = item.getType();
        }

        if (mat != null) {
            if (mat == Material.POTION) {
                PotionMeta meta = (PotionMeta) item.getItemMeta();
                PotionData data = meta.getBasePotionData();

                if (data.getType() == PotionType.INVISIBILITY) {
                    event.setCancelled(true);
                }
                if (data.getType() == PotionType.INSTANT_DAMAGE && data.isUpgraded()) {
                    event.setCancelled(true);
                }
            }
            if (mat == Material.SPLASH_POTION) {
                PotionMeta meta = (PotionMeta) item.getItemMeta();
                PotionData data = meta.getBasePotionData();

                if (data.getType() == PotionType.INVISIBILITY) {
                    event.setCancelled(true);
                }
                if (data.getType() == PotionType.INSTANT_DAMAGE && data.isUpgraded()) {
                    event.setCancelled(true);
                }
            }
            if (mat == Material.LINGERING_POTION) {
                PotionMeta meta = (PotionMeta) item.getItemMeta();
                PotionData data = meta.getBasePotionData();

                if (data.getType() == PotionType.INVISIBILITY) {
                    event.setCancelled(true);
                }
                if (data.getType() == PotionType.INSTANT_DAMAGE && data.isUpgraded()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Block potion crafting for specific potions.
     */
    @EventHandler
    public void onPrepare(PrepareItemCraftEvent event) {
        List<HumanEntity> humans = event.getViewers();
        ItemStack item = event.getInventory().getResult();
        Material mat = item.getType();

        if (mat == Material.TIPPED_ARROW) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            PotionData data = meta.getBasePotionData();

            if (data.getType() == PotionType.INVISIBILITY) {
                event.getInventory().setResult(new ItemStack(Material.AIR));

                for (HumanEntity e : humans) {
                    e.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cYou cannot craft this."));
                }
            }
        }
    }

    /**
     * Block potion use for specific potions.
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() == null) {
            return;
        }

        Material mat = item.getType();

        if (mat == Material.POTION) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            PotionData data = meta.getBasePotionData();

            if (data.getType() == PotionType.INVISIBILITY) {
                player.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cThis potion is disabled."));
                event.setCancelled(true);
            }
            if (data.getType() == PotionType.INSTANT_DAMAGE && data.isUpgraded()) {
                player.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cThis potion is disabled."));
                event.setCancelled(true);
            }
        }

        if (mat == Material.SPLASH_POTION) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            PotionData data = meta.getBasePotionData();

            if (data.getType() == PotionType.INVISIBILITY) {
                player.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cThis potion is disabled."));
                event.setCancelled(true);
            }
            if (data.getType() == PotionType.INSTANT_DAMAGE && data.isUpgraded()) {
                player.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cThis potion is disabled."));
                event.setCancelled(true);
            }
        }

        if (mat == Material.LINGERING_POTION) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            PotionData data = meta.getBasePotionData();

            if (data.getType() == PotionType.INVISIBILITY) {
                player.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cThis potion is disabled."));
                event.setCancelled(true);
            }
            if (data.getType() == PotionType.INSTANT_DAMAGE && data.isUpgraded()) {
                player.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &cThis potion is disabled."));
                event.setCancelled(true);
            }
        }
    }
}
