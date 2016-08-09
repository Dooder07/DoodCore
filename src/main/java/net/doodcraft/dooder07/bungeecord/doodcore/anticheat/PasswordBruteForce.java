package net.doodcraft.dooder07.bungeecord.doodcore.anticheat;

import net.doodcraft.dooder07.bungeecord.doodcore.config.Settings;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class PasswordBruteForce implements Listener {

    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String name = player.getName();

        String message = event.getMessage();

        int points = checkPoints.get(((ProxiedPlayer) event.getSender()).getUniqueId());

        String[] args = message.split(" ");

        if (args[0].equalsIgnoreCase("/login")) {

            for (String p : Settings.bruteForcePasswords) {

                if (message.equalsIgnoreCase("/login " + ((ProxiedPlayer) event.getSender()).getName()) || message.equalsIgnoreCase("/login " + p)) {

                    int newPoints = points + 1;

                    if (newPoints >= 3) {

                        String disclaimer = "\n\n&8[&eThis ban cannot be appealed&8]";

                        Random rand = new Random();
                        int msg = rand.nextInt(3) + 1;

                        try {
                            if (msg == 1) {
                                AntiCheatMethods.ipBan(((ProxiedPlayer) event.getSender()), "Sucks to suck!" + disclaimer, "PasswordBruteForce");
                                return;
                            } else if (msg == 2) {
                                AntiCheatMethods.ipBan(((ProxiedPlayer) event.getSender()), "You're the WURST!" + disclaimer, "PasswordBruteForce");
                                return;
                            } else if (msg == 3) {
                                AntiCheatMethods.ipBan(((ProxiedPlayer) event.getSender()), "Nice try!" + disclaimer, "PasswordBruteForce");
                                return;
                            } else {
                                AntiCheatMethods.ipBan(((ProxiedPlayer) event.getSender()), "Get a life.." + disclaimer, "PasswordBruteForce");
                                return;
                            }
                        } catch (Exception ex) {

                        }
                    } else {

                        checkPoints.put(((ProxiedPlayer) event.getSender()).getUniqueId(), newPoints);
                    }
                }
            }
        }
    }

    public static HashMap<UUID, Integer> checkPoints = new HashMap<>();

    public static void addAllPlayers() {
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            checkPoints.put(p.getUniqueId(), 0);
        }
    }

    public static void removeAllPlayers() {
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (checkPoints.containsKey(p.getUniqueId())) {
                checkPoints.remove(p.getUniqueId());
            }
        }
    }


    @EventHandler
    public void onJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (!checkPoints.containsKey(player.getUniqueId())) {
            checkPoints.put(player.getUniqueId(), 0);
        }
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (checkPoints.containsKey(player.getUniqueId())) {
            checkPoints.remove(player.getUniqueId());
        }
    }
}
