package net.doodcraft.dooder07.spigot.doodcore;

import mkremins.fanciful.FancyMessage;
import net.doodcraft.dooder07.spigot.doodcore.compat.Compatibility;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

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
public class Methods {
    public static void sendNoPermission(Player player, String node) {
        FancyMessage message = new FancyMessage(StringParser.addColor(Settings.pluginPrefix + " &cNo permission")).tooltip(StringParser.addColor("&b" + node));
        sendFancyMessage(player, message);
    }

    public static PacketPlayOutChat createPacketPlayOutChat(String s){return new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(s));}

    public static void sendJsonMessage(Player p, String s){( (CraftPlayer)p ).getHandle().playerConnection.sendPacket(createPacketPlayOutChat(s));}

    public static void sendFancyMessage(Player player, FancyMessage message) {
        sendJsonMessage(player, message.toJSONString());
    }

    public static void banPlayer(Player player, String reason) {
        String ip = String.valueOf(player.getAddress().getAddress());
        ip = ip.replaceAll("/", "");

        if (Compatibility.isHooked("Litebans")) {
            if (reason.equalsIgnoreCase("passwordbruteforce")) {
                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "ipban " + ip + " " + Settings.pluginPrefix + " &cYou are using an illegal mod/client!");
                return;
            }
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "ipban " + ip + " " + Settings.pluginPrefix + " &eGeneral breach of the rules!");
        } else {
            DoodLog.log("DoodCore", "&cLitebans is not hooked. Autoban was denied.");
        }
    }
}
