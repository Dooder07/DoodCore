package net.doodcraft.dooder07.spigot.doodcore.features;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.utils.CombatUtil;
import net.doodcraft.dooder07.spigot.doodcore.DoodLog;
import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import net.doodcraft.dooder07.spigot.doodcore.compat.Compatibility;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

// Todo: Extract Towny related code and make this work with other plugin's regions.
public class TownDistance implements Listener {

    @EventHandler
    public void onPreProcess(PlayerCommandPreprocessEvent event) {

        for (String block : Settings.townDistanceChecklist) {

            String[] parts = block.split(" ");

            int length = parts.length;
            int count = 0;

            for (String part : parts) {
                if (event.getMessage().toLowerCase().contains(part.toLowerCase())) {
                    count++;
                }
            }

            if (count == length) {

                if (checkTownDistance(event.getPlayer(), Settings.townDistance, "&cThat command is disabled here.")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public static boolean checkTownDistance(Player player, Integer radius, String denied) {
        if (player.hasPermission("doodcore.admin.bypass")) {
            player.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &aOverriding town distance check."));
            return false;
        } else {
            player.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &7Checking for town claims within a " + radius + " block radius.."));

            if (!startDistanceCheck(player, radius)) {
                player.sendMessage(StringParser.addColor(Settings.pluginPrefix + " " + denied));
                return true;
            }
            return false;
        }
    }

    public static boolean startDistanceCheck(final Player player, final Integer radius) {

        if (Compatibility.isHooked("Towny")) {

            Location loc = player.getLocation();

            String townName = null;
            Resident resident;
            Town town = null;

            try {
                resident = TownyUniverse.getDataSource().getResident(player.getName());

                if (resident.hasTown()) {
                    town = resident.getTown();
                    townName = town.getName();
                } else {
                    townName = "Wilderness";
                }
            } catch (Exception ex) {
                DoodLog.printError("Core", "Towny Not Registered Exception", ex);
            }

            try {
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        if ((x * x) + (z * z) <= (radius * radius)) {
                            Location check = new Location(loc.getWorld(), loc.getBlockX() + x, loc.getBlockY(), loc.getBlockZ() + z);
                            Block block = check.getBlock();

                            String otherTownName = TownyUniverse.getTownName(check);

                            Town otherTown = null;
                            try {
                                try {
                                    otherTown = TownyUniverse.getDataSource().getTown(otherTownName);
                                } catch (NullPointerException ex) {
                                    otherTownName = "Wilderness";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (!TownyUniverse.isWilderness(block) && !townName.equalsIgnoreCase(otherTownName)) {
                                return false;
                            }

                            if (otherTown != null && !CombatUtil.isAlly(otherTown, town)) {
                                return false;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                DoodLog.printError("Core", "Exception", ex);
            }
        } else {
            DoodLog.log("DoodCore", "&cTowny is not hooked. TownDistance checking is disabled.");
            return true;
        }
        return true;
    }
}
