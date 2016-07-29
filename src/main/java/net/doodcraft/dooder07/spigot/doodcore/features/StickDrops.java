package net.doodcraft.dooder07.spigot.doodcore.features;

import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class StickDrops implements Listener {

    static Random random = new Random();

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (Settings.stickDrops) {

            Player player = event.getPlayer();
            Block block = event.getBlock();

            if (block.getType() != null) {
                Material mat = block.getType();

                if (player != null) {
                    if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                        if (mat.equals(Material.LEAVES) || mat.equals(Material.LEAVES_2)) {
                            int chance = random.nextInt(20) + 1;

                            if (chance == 1) {
                                player.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.STICK, 1));
                            }

                            if (chance == 2) {
                                player.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.STICK, 2));
                            }
                        }
                    }
                }
            }
        }
    }
}
