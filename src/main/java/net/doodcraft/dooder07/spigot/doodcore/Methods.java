package net.doodcraft.dooder07.spigot.doodcore;

import mkremins.fanciful.FancyMessage;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String getTime(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getPlayerIP(Player player) {
        String ip = String.valueOf(player.getAddress().getAddress().getHostAddress());
        return ip.replaceAll("/", "");
    }
}
