package net.doodcraft.dooder07.spigot.doodcore.lib;

import net.minecraft.server.v1_10_R1.PacketPlayOutMapChunk;
import org.bukkit.craftbukkit.v1_10_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketMapChunk {
    private final net.minecraft.server.v1_10_R1.Chunk chunk;

    public PacketMapChunk(final org.bukkit.Chunk chunk) {
        this.chunk = ((CraftChunk)chunk).getHandle();
    }

    public final void send(final Player player) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunk(chunk, 20));
    }
}
