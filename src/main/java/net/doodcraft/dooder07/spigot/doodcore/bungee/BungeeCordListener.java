package net.doodcraft.dooder07.spigot.doodcore.bungee;

import net.doodcraft.dooder07.spigot.doodcore.DoodLog;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class BungeeCordListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] msg) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(msg));

        try {
            String subChannel = in.readUTF();
            if (subChannel.equals("Core")) {
                short len = in.readShort();
                byte[] msgBytes = new byte[len];
                in.readFully(msgBytes);

                DataInputStream msgIn = new DataInputStream(new ByteArrayInputStream(msgBytes));
                String inputChannel = msgIn.readUTF();

                if (inputChannel.equals("name")) {
                    // do stuff : msgIn.readUTF(); will be each line sent from the origin message
                }
            }
        } catch (Exception ex) {
            DoodLog.printError("Core", "&cError receiving global Bungee message!", ex);
        }
    }
}
