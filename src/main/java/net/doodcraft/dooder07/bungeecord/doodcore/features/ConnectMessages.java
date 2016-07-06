package net.doodcraft.dooder07.bungeecord.doodcore.features;

import net.alpenblock.bungeeperms.BungeePerms;
import net.doodcraft.dooder07.bungeecord.doodcore.BungeeLog;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

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
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String name = player.getName();

        BungeeLog.log("DoodCore", name + " joined the game.");

        if (BungeePerms.getInstance().getPermissionsManager() != null) {
            name = BungeePerms.getInstance().getPermissionsManager().getUser(name).getGroupByLadder("default").getPrefix() + name;

            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {

                p.sendMessage(new TextComponent(BungeeLog.addColor("&8[" + name + "&8]&e joined the game.")));
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String name = player.getName();

        BungeeLog.log("DoodCore", name + " quit the game.");

        if (BungeePerms.getInstance().getPermissionsManager() != null) {
            name = BungeePerms.getInstance().getPermissionsManager().getUser(name).getGroupByLadder("default").getPrefix() + name;

            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {

                p.sendMessage(new TextComponent(BungeeLog.addColor("&8[" + name + "&8]&e quit the game.")));
            }
        }
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String name = player.getName();

        if (BungeePerms.getInstance().getPermissionsManager() != null) {
            name = BungeePerms.getInstance().getPermissionsManager().getUser(name).getGroupByLadder("default").getPrefix() + name;
            BungeeLog.log("DoodCore|ChatEvent", name + "&8: &7" + event.getMessage());
        }
    }
}
