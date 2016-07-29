package net.doodcraft.dooder07.spigot.doodcore;

import mkremins.fanciful.FancyMessage;
import net.doodcraft.dooder07.spigot.doodcore.compat.Compatibility;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

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
