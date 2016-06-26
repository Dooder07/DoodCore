package net.doodcraft.dooder07.spigot.doodcore.commands;

import mkremins.fanciful.FancyMessage;
import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.spigot.doodcore.DoodLog;
import net.doodcraft.dooder07.spigot.doodcore.Methods;
import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
public class Search implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("search")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return false;
            }

            Player player = (Player) sender;

            if (player.hasPermission("core.command.search")) {
                if (args.length != 2) {
                    player.sendMessage(StringParser.addColor("/search chunkradius string\nExample: /search 5 diamond"));
                    return false;
                } else {
                    Bukkit.getScheduler().runTaskLater(DoodCorePlugin.plugin, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                long start = System.currentTimeMillis();

                                player.sendMessage(StringParser.addColor("&a&oScanning..."));

                                String string = String.valueOf(args[1].replaceAll("_", " "));

                                ArrayList<Entity> mobs = new ArrayList<>();

                                HashMap<Block, ItemStack> chests = new HashMap<>();

                                ArrayList<Chunk> chunks = new ArrayList<>();

                                Chunk chunk = player.getLocation().getChunk();

                                int radius = Integer.valueOf(args[0]) + 1;
                                int chunkX = chunk.getX();
                                int chunkZ = chunk.getZ();

                                for (int x = radius; x >= 0; x--) {
                                    for (int z = radius; z >= 0; z--) {

                                        if (!chunks.contains(player.getWorld().getChunkAt(chunkX + x, chunkZ + z))) {
                                            chunks.add(player.getWorld().getChunkAt(chunkX + x, chunkZ + z));
                                        }
                                        if (!chunks.contains(player.getWorld().getChunkAt(chunkX - x, chunkZ - z))) {
                                            chunks.add(player.getWorld().getChunkAt(chunkX - x, chunkZ - z));
                                        }
                                    }
                                }

                                for (Chunk c : chunks) {
                                    for (Entity e : c.getEntities()) {

                                        if (e.getClass().getSimpleName().replaceAll("Craft", "").toLowerCase().contains(string.toLowerCase()) || e.getName().toLowerCase().contains(string.toLowerCase())) {
                                            mobs.add(e);
                                        } else {
                                            if (e.getCustomName() != null) {
                                                if (e.getCustomName().toLowerCase().contains(string.toLowerCase())) {
                                                    mobs.add(e);
                                                }
                                            }
                                        }
                                    }

                                    for (int x = 0; x <= 16; x++) {
                                        for (int y = 0; y <= 256; y++) {
                                            for (int z = 0; z <= 16; z++) {
                                                Block b = chunk.getBlock(x, y, z);
                                                if (b.getType().equals(Material.CHEST)) {
                                                    Chest chest = (Chest) b.getState();
                                                    Inventory inv = chest.getBlockInventory();

                                                    for (ItemStack i : inv.getContents()) {
                                                        if (i != null) {
                                                            if (i.getItemMeta().getDisplayName() != null) {
                                                                if (i.getItemMeta().getDisplayName().toLowerCase().contains(string.toLowerCase())) {
                                                                    chests.put(b, i);
                                                                } else {
                                                                    if (i.getType().toString().toLowerCase().contains(string.toLowerCase())) {
                                                                        chests.put(b, i);
                                                                    }
                                                                }
                                                            } else {
                                                                if (i.getType().toString().toLowerCase().contains(string.toLowerCase())) {
                                                                    chests.put(b, i);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                for (Entity e : mobs) {
                                    String loc = e.getLocation().getX() + " " + e.getLocation().getY() + " " + e.getLocation().getZ();

                                    if (e.getCustomName() != null) {
                                        FancyMessage msg = new FancyMessage(StringParser.addColor("&8:: &e" + e.getClass().getSimpleName().replaceAll("Craft", "") + " &8[&r" + e.getCustomName() + "&8]"));
                                        msg.command("/minecraft:tp " + loc).tooltip(loc);
                                        Methods.sendFancyMessage(player, msg);
                                    } else {
                                        FancyMessage msg = new FancyMessage(StringParser.addColor("&8:: &e" + e.getClass().getSimpleName().replaceAll("Craft", "") + " &8[&r" + e.getName() + "&8]"));
                                        msg.command("/minecraft:tp " + loc).tooltip(loc);
                                        Methods.sendFancyMessage(player, msg);
                                    }
                                }

                                Iterator iterator = chests.entrySet().iterator();
                                while (iterator.hasNext()) {

                                    HashMap.Entry pair = (HashMap.Entry) iterator.next();

                                    String loc = ((Block) pair.getKey()).getLocation().getX() + " " + ((Block) pair.getKey()).getLocation().getY() + " " + ((Block) pair.getKey()).getLocation().getZ();

                                    FancyMessage msg = new FancyMessage(StringParser.addColor("&8:: &eINSIDE CHEST &8[&r" + ((ItemStack) pair.getValue()).getType().toString() + "&8]"));
                                    msg.command("/minecraft:tp " + loc).tooltip(loc);
                                    Methods.sendFancyMessage(player, msg);

                                    iterator.remove();
                                }

                                player.sendMessage(StringParser.addColor("&aDone! &e(" + (System.currentTimeMillis() - start) + "ms)"));
                            } catch (Exception ex) {
                                player.sendMessage(StringParser.addColor("&cThere was an error with your request - try again."));
                                player.sendMessage(StringParser.addColor("/search radius string\nExample: /search 5 diamond"));

                                DoodLog.printError("DreadGuard", "Error executing /search!", ex);
                            }
                        }
                    }, 1L);
                    return true;
                }
            } else {
                Methods.sendNoPermission(player, "core.command.search");
                return false;
            }
        }
        return false;
    }
}
