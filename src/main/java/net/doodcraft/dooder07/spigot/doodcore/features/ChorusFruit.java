package net.doodcraft.dooder07.spigot.doodcore.features;

import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class ChorusFruit implements Listener {

    @EventHandler
    public void onTeleport(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if (Settings.checkChorusFruitTeleport) {
            if (event.getItem().getType().equals(Material.CHORUS_FRUIT)) {

                if (TownDistance.checkTownDistance(player, Settings.chorusDistance, "&cChorus Fruit teleportation is disabled here!")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
