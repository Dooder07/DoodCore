package net.doodcraft.dooder07.spigot.doodcore.features;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import net.doodcraft.dooder07.spigot.doodcore.compat.Compatibility;
import net.doodcraft.dooder07.spigot.doodcore.compat.Protocollib;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

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
public class TabMenu implements Listener {

    public static int refreshTask = -1;

    public static void startRefresh() {
        refreshTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(DoodCorePlugin.plugin, TabMenu::updateTabList, 1L, 80L);
    }

    public static void stopRefresh() {
        if (refreshTask != -1) {
            Bukkit.getScheduler().cancelTask(refreshTask);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void updateTabList() {
        if (Compatibility.isHooked("ProtocolLib")) {
            if (Bukkit.getOnlinePlayers().size() >= 1) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    PacketContainer packet = Protocollib.getManager().createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
                    packet.getChatComponents().write(0, WrappedChatComponent.fromText(StringParser.addColor("&7&n" + Settings.serverName))).write(1, WrappedChatComponent.fromText(StringParser.addColor("&f" + getPing(p) + "ms")));

                    try {
                        Protocollib.getManager().sendServerPacket(p, packet);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static int getPing(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        return entityPlayer.ping;
    }
}
