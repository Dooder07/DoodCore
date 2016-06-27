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
