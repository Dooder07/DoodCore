package net.doodcraft.dooder07.spigot.doodcore.features;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
public class ConnectMessages implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
//        if (Compatibility.isHooked("Vault")) {
//            if (Bukkit.getServer().getName().equalsIgnoreCase("Lobby")) {
//                event.setJoinMessage(StringParser.addColor("&8[&r" + Vault.chat.getPlayerPrefix(null, event.getPlayer()) + "<name>&8] &ejoined the lobby.")
//                        .replaceAll("<name>", event.getPlayer().getName()));
//            } else {
//                event.setJoinMessage(StringParser.addColor("&8[&r" + Vault.chat.getPlayerPrefix(null, event.getPlayer()) + "<name>&8] &ejoined the game.")
//                        .replaceAll("<name>", event.getPlayer().getName()));
//            }
//        }

        // Let Bungee handle <></>he connection messages
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
//        if (Compatibility.isHooked("Vault")) {
//            event.setQuitMessage(StringParser.addColor("&8[&r" + Vault.chat.getPlayerPrefix(null, event.getPlayer()) + "<name>&8] &equit.")
//                    .replaceAll("<name>", event.getPlayer().getName()));
//        }

        // Let Bungee handle the connection messages
        event.setQuitMessage(null);
    }
}
