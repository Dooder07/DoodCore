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

public class PotionBlocking implements Listener {

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
