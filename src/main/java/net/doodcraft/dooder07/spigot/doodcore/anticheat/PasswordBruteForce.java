package net.doodcraft.dooder07.spigot.doodcore.anticheat;

import net.doodcraft.dooder07.spigot.doodcore.Methods;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PasswordBruteForce implements Listener {

    public static HashMap<UUID, Integer> checkPoints = new HashMap<>();

    public static List<String> passwords = Settings.bruteForcePasswords;

    public static void addAllPlayers(){
        Bukkit.getOnlinePlayers().stream().filter(p -> !checkPoints.containsKey(p.getUniqueId())).forEach(p -> checkPoints.put(p.getUniqueId(), 0));
    }

    public static void removeAllPlayers(){
        Bukkit.getOnlinePlayers().stream().filter(p -> checkPoints.containsKey(p.getUniqueId())).forEach(p -> checkPoints.remove(p.getUniqueId()));
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();

        int points = checkPoints.get(event.getPlayer().getUniqueId());

        String[] args = message.split(" ");

        if (args[0].equalsIgnoreCase("/login")) {
            for (String p : passwords) {
                if (message.equalsIgnoreCase("/login " + event.getPlayer().getName()) || message.equalsIgnoreCase("/login " + p)) {
                    int newPoints = points + 1;
                    if (newPoints >= 3) {
                        Methods.banPlayer(event.getPlayer(), "passwordbruteforce");
                    } else {
                        checkPoints.put(event.getPlayer().getUniqueId(), newPoints);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!checkPoints.containsKey(player.getUniqueId())) {
            checkPoints.put(player.getUniqueId(), 0);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (checkPoints.containsKey(player.getUniqueId())) {
            checkPoints.remove(player.getUniqueId());
        }
    }
}
