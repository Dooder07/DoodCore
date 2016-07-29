package net.doodcraft.dooder07.spigot.doodcore.features;

import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.spigot.doodcore.lib.ChunkWrap;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;

// Todo: This isn't working. It's a good idea, let's get it working asap.
public class ChunkFix implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        final int visibleDistance = Bukkit.getServer().getViewDistance() * 16;

        Location from = event.getFrom();
        Location to = event.getTo();

        // Fix lighting/rendering issues
        if (!to.getWorld().equals(from.getWorld())) {
            Bukkit.getScheduler().runTask(DoodCorePlugin.plugin, new Runnable() {
                @Override
                public void run() {
                    fixChunkLighting(to.getChunk());
                }
            });
        } else {
            if (to.distance(from) >= 48) {
                Bukkit.getScheduler().runTask(DoodCorePlugin.plugin, new Runnable() {
                    @Override
                    public void run() {
                        fixChunkLighting(to.getChunk());
                    }
                });
            }
        }

        // Fix invisible players bug
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DoodCorePlugin.plugin, new Runnable() {
            @Override
            public void run() {
                final List<Player> nearby = getPlayersWithin(player, visibleDistance);

                // Hide every player
                updateEntities(player, nearby, false);

                // Then show them again
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DoodCorePlugin.plugin, new Runnable() {
                    @Override
                    public void run() {
                        updateEntities(player, nearby, true);
                    }
                }, 1);
            }
        }, 5L);
    }

    private void updateEntities(Player tpedPlayer, List<Player> players, boolean visible) {
        for (Player player : players) {
            if (visible) {
                tpedPlayer.showPlayer(player);
                player.showPlayer(tpedPlayer);
            } else {
                tpedPlayer.hidePlayer(player);
                player.hidePlayer(tpedPlayer);
            }
        }
    }

    private List<Player> getPlayersWithin(Player player, int distance) {
        List<Player> res = new ArrayList<Player>();
        int d2 = distance * distance;
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p != player && p.getWorld() == player.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= d2) {
                res.add(p);
            }
        }
        return res;
    }

    public void fixChunkLighting(Chunk origin) {
        int radius = 1;

        int originX = origin.getX();
        int originZ = origin.getZ();

        World world = origin.getWorld();

        // Apply the fix
        for (int deltaX = -radius; deltaX <= radius; deltaX++) {
            for (int deltaZ = -radius; deltaZ <= radius; deltaZ++) {
                int x = originX + deltaX;
                int z = originZ + deltaZ;
                new ChunkWrap(world, x, z).recalcLightLevel();
            }
        }
    }
}
