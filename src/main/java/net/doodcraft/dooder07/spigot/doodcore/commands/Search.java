package net.doodcraft.dooder07.spigot.doodcore.commands;

import net.doodcraft.dooder07.spigot.doodcore.DoodLog;
import net.doodcraft.dooder07.spigot.doodcore.Methods;
import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import net.doodcraft.dooder07.spigot.doodcore.lib.Pagination;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
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

// TODO: Needs some severe optimization!
public class Search implements CommandExecutor {

    HashMap<String, Pagination.PaginationBook> lastSearch = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("search")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return false;
            }

            Player player = (Player) sender;

            if (player.hasPermission("core.command.search")) {
                if (args.length == 0){
                    player.sendMessage(StringParser.addColor("/search chunkradius string\nExample: /search 5 diamond"));
                    return true;
                } else if (args.length == 1) {
                    // SEARCHING THROUGH LAST SEARCH
                    if (lastSearch.containsKey(player.getName())) {
                        if (!lastSearch.get(player.getName()).isEmpty()) {
                            try {
                                Pagination.PaginationBook book = (Pagination.PaginationBook) lastSearch.get(player.getName())
                                        .decodeHtmlEntities(true)
                                        .setEntryFormat("&8:: %entry%n")
                                        .setPageFormat(StringParser.addColor("&8&l&m============================================%n%entries&8&l&m===============================&8[ &b<page>&8/&b%pagemax &8]&l&m========").replaceAll("<page>", String.valueOf(Integer.valueOf(args[0]))));

                                book.displayPage(player, Integer.valueOf(args[0]));
                            } catch (Exception ex) {
                                player.sendMessage(StringParser.addColor("&cThat number is outside the page range!"));
                            }
                        } else {
                            player.sendMessage(StringParser.addColor("&aNo results!"));
                        }
                    } else {
                        player.sendMessage(StringParser.addColor("&cYou haven't searched for anything yet!"));
                    }
                } else if (args.length == 2) {
                    try {
                        if (Integer.valueOf(args[0]) >= 1 && Integer.valueOf(args[0]) <= 20) {
                            player.sendMessage(StringParser.addColor("&a&oScanning..."));

                            // Get the chunks to check
                            Location loc = player.getLocation();
                            World world = loc.getWorld();
                            Chunk chunk = loc.getChunk();

                            int radius = (Integer.valueOf(args[0]) - 1);

                            int chunkX = chunk.getX();
                            int chunkZ = chunk.getZ();

                            ArrayList<Chunk> chunks = new ArrayList<>();
                            for (int x = chunkX - radius; x <= chunkX + radius; x++) {
                                for (int z = chunkZ - radius; z <= chunkZ + radius; z++) {
                                    Chunk c = world.getChunkAt(x, z);
                                    if (!chunks.contains(c)) {
                                        chunks.add(c);
                                    }
                                }
                            }

                            // Find and add results
                            String string = String.valueOf(args[1]);

                            ArrayList<Entity> foundEntities = new ArrayList<>();
                            HashMap<Block, ItemStack> foundChests = new HashMap<>();

                            ArrayList<String> lines = new ArrayList<>();
                            Pagination.PaginationBook book = new Pagination.PaginationBook();

                            for (Chunk c : chunks) {
                                for (Entity e : c.getEntities()) {
                                    if (e.getClass().getSimpleName().replaceAll("Craft", "").toLowerCase().contains(string.toLowerCase()) || e.getName().toLowerCase().contains(string.toLowerCase())) {
                                        foundEntities.add(e);
                                    } else {
                                        if (e.getCustomName() != null) {
                                            if (e.getCustomName().toLowerCase().contains(string.toLowerCase())) {
                                                foundEntities.add(e);
                                            }
                                        }
                                    }
                                }

                                for (int x = 0; x <= 16; x++) {
                                    for (int y = 0; y <= 256; y++) {
                                        for (int z = 0; z <= 16; z++) {
                                            Block b = c.getBlock(x, y, z);

                                            if (b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST)) {
                                                Chest chest = (Chest) b.getState();
                                                Inventory inv = chest.getBlockInventory();

                                                for (ItemStack i : inv.getContents()) {
                                                    if (i != null) {
                                                        if (i.getItemMeta().getDisplayName() != null) {
                                                            if (i.getItemMeta().getDisplayName().toLowerCase().contains(string.toLowerCase())) {
                                                                foundChests.put(b, i);
                                                            } else {
                                                                if (i.getType().toString().toLowerCase().contains(string.toLowerCase())) {
                                                                    foundChests.put(b, i);
                                                                }
                                                            }
                                                        } else {
                                                            if (i.getType().toString().toLowerCase().contains(string.toLowerCase())) {
                                                                foundChests.put(b, i);
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            if (b.getType().equals(Material.FURNACE) || b.getType().equals(Material.BURNING_FURNACE)) {
                                                Furnace furnace = (Furnace) b.getState();
                                                Inventory inv = furnace.getInventory();

                                                for (ItemStack i : inv.getContents()) {
                                                    if (i != null) {
                                                        if (i.getItemMeta().getDisplayName() != null) {
                                                            if (i.getItemMeta().getDisplayName().toLowerCase().contains(string.toLowerCase())) {
                                                                foundChests.put(b, i);
                                                            } else {
                                                                if (i.getType().toString().toLowerCase().contains(string.toLowerCase())) {
                                                                    foundChests.put(b, i);
                                                                }
                                                            }
                                                        } else {
                                                            if (i.getType().toString().toLowerCase().contains(string.toLowerCase())) {
                                                                foundChests.put(b, i);
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            if (b.getType().equals(Material.DISPENSER)) {
                                                Dispenser dispenser = (Dispenser) b.getState();
                                                Inventory inv = dispenser.getInventory();

                                                for (ItemStack i : inv.getContents()) {
                                                    if (i != null) {
                                                        if (i.getItemMeta().getDisplayName() != null) {
                                                            if (i.getItemMeta().getDisplayName().toLowerCase().contains(string.toLowerCase())) {
                                                                foundChests.put(b, i);
                                                            } else {
                                                                if (i.getType().toString().toLowerCase().contains(string.toLowerCase())) {
                                                                    foundChests.put(b, i);
                                                                }
                                                            }
                                                        } else {
                                                            if (i.getType().toString().toLowerCase().contains(string.toLowerCase())) {
                                                                foundChests.put(b, i);
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            if (b.getType().equals(Material.DROPPER)) {
                                                Dropper dropper = (Dropper) b.getState();
                                                Inventory inv = dropper.getInventory();

                                                for (ItemStack i : inv.getContents()) {
                                                    if (i != null) {
                                                        if (i.getItemMeta().getDisplayName() != null) {
                                                            if (i.getItemMeta().getDisplayName().toLowerCase().contains(string.toLowerCase())) {
                                                                foundChests.put(b, i);
                                                            } else {
                                                                if (i.getType().toString().toLowerCase().contains(string.toLowerCase())) {
                                                                    foundChests.put(b, i);
                                                                }
                                                            }
                                                        } else {
                                                            if (i.getType().toString().toLowerCase().contains(string.toLowerCase())) {
                                                                foundChests.put(b, i);
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            if (b.getType().equals(Material.HOPPER)) {
                                                Hopper hopper = (Hopper) b.getState();
                                                Inventory inv = hopper.getInventory();

                                                for (ItemStack i : inv.getContents()) {
                                                    if (i != null) {
                                                        if (i.getItemMeta().getDisplayName() != null) {
                                                            if (i.getItemMeta().getDisplayName().toLowerCase().contains(string.toLowerCase())) {
                                                                foundChests.put(b, i);
                                                            } else {
                                                                if (i.getType().toString().toLowerCase().contains(string.toLowerCase())) {
                                                                    foundChests.put(b, i);
                                                                }
                                                            }
                                                        } else {
                                                            if (i.getType().toString().toLowerCase().contains(string.toLowerCase())) {
                                                                foundChests.put(b, i);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            for (Entity e : foundEntities) {
                                String location = (Math.round(e.getLocation().getX() * 10d) / 10d) + ", " + (Math.round(e.getLocation().getY() * 10d) / 10d) + ", " + (Math.round(e.getLocation().getZ() * 10d) / 10d);

                                if (e.getCustomName() != null) {
                                    String line = StringParser.addColor("&e" + e.getClass().getSimpleName().replaceAll("Craft", "") + " &8[&r" + e.getCustomName() + "&8] [&r" + location + "&8]");
                                    lines.add(line);
                                } else {
                                    String line = StringParser.addColor("&e" + e.getClass().getSimpleName().replaceAll("Craft", "") + " &8[&r" + e.getName() + "&8] [&r" + location + "&8]");
                                    lines.add(line);
                                }
                            }

                            Iterator iterator = foundChests.entrySet().iterator();
                            while (iterator.hasNext()) {

                                HashMap.Entry pair = (HashMap.Entry) iterator.next();

                                String location = ((Block) pair.getKey()).getLocation().getX() + ", " + ((Block) pair.getKey()).getLocation().getY() + ", " + ((Block) pair.getKey()).getLocation().getZ();

                                String line = StringParser.addColor("&eINSIDE " + ((Block) pair.getKey()).getType().toString() + " &8[&r" + ((ItemStack) pair.getValue()).getType().toString() + "&8] [&r" + location + "&8]");
                                lines.add(line);

                                iterator.remove();
                            }

                            if (lines.size() >= 1) {
                                book = (Pagination.PaginationBook) Pagination.paginate(lines, 10)
                                        .decodeHtmlEntities(true)
                                        .setEntryFormat("&8:: %entry%n")
                                        .setPageFormat(StringParser.addColor("&8&l&m================&8[&b Search Results &8]&l&m===============%n%entries&8&l&m===============================&8[ &b%page&8/&b%pagemax &8]&l&m========"));

                                if (!book.isEmpty()) {
                                    if (!lastSearch.containsKey(player.getName())) {
                                        lastSearch.put(player.getName(), book);
                                    } else {
                                        lastSearch.remove(player.getName());
                                        lastSearch.put(player.getName(), book);
                                    }

                                    book.displayPage(player, 1);
                                    return true;
                                } else {
                                    player.sendMessage(StringParser.addColor("&aNo results!"));
                                    return true;
                                }
                            } else {
                                player.sendMessage(StringParser.addColor("&aNo results!"));
                                return true;
                            }
                        } else {
                            player.sendMessage(StringParser.addColor("&cYou supplied an unsafe radius. Use &8[&e1-20&8]&c."));
                            return false;
                        }
                    } catch (Exception ex) {
                        player.sendMessage(StringParser.addColor("&cThere was an error with your request - try again?"));
                        DoodLog.printError("DreadGuard", "Error executing /search!", ex);
                        return false;
                    }
                } else {
                    // too many args!
                    player.sendMessage(StringParser.addColor("/search chunkradius string\nExample: /search 5 diamond"));
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
