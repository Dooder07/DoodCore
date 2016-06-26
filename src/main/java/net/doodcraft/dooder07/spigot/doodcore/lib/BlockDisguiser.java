package net.doodcraft.dooder07.spigot.doodcore.lib;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.reflect.StructureModifier;
import com.google.common.collect.HashBasedTable;
import org.apache.commons.lang.SerializationUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.io.*;

/**
 * Simple class that can be used to alter the apperance of a number of blocks.
 * @author Kristian
 */
public class BlockDisguiser {
    private HashBasedTable<ChunkCoordinate, BlockCoordinate, Integer> translations = HashBasedTable.create();

    // The current listener
    private PacketAdapter listener;

    /**
     * Construct a new block changer.
     * @param parent - the owner plugin.
     */
    public BlockDisguiser(Plugin parent) {
        registerListener(parent);
    }

    @SuppressWarnings("unchecked")
    public void loadState(InputStream stream) {
        translations = (HashBasedTable<ChunkCoordinate, BlockCoordinate, Integer>)
                SerializationUtils.deserialize(stream);
    }

    public void loadState(File source) throws IOException {
        InputStream io = null;

        try {
            io = new BufferedInputStream(new FileInputStream(source));
            loadState(io);
        } finally {
            if (io != null) {
                io.close();
            }
        }
    }

    public void saveState(OutputStream stream) {
        SerializationUtils.serialize(translations, stream);
    }

    public void saveState(File destination) throws IOException {
        OutputStream io = null;

        try {
            io = new BufferedOutputStream(new FileOutputStream(destination));
            saveState(io);
        } finally {
            if (io != null) {
                io.close();
            }
        }
    }

    /**
     * Create a new translated block that have a different block ID on the server and visually for a client.
     * @param loc - the location of the block.
     * @param serverBlockID - the block ID on the server side.
     * @param clientBlockID - the block ID on the client side.
     */
    public void setTranslatedBlock(Location loc, int serverBlockID, int clientBlockID) {
        // Make this block appear as the client block
        translations.put(ChunkCoordinate.fromBlock(loc), new BlockCoordinate(loc), clientBlockID);

        // Set the server side block
        loc.getBlock().setTypeId(serverBlockID);
    }

    private void registerListener(Plugin plugin) {
        final ChunkPacketProcessor.ChunkletProcessor processor = getChunkletProcessor();

        ProtocolLibrary.getProtocolManager().addPacketListener(
                listener = new PacketAdapter(plugin, ConnectionSide.SERVER_SIDE,
                        Packets.Server.BLOCK_CHANGE, Packets.Server.MULTI_BLOCK_CHANGE,
                        Packets.Server.MAP_CHUNK, Packets.Server.MAP_CHUNK_BULK) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        PacketContainer packet = event.getPacket();
                        World world = event.getPlayer().getWorld();

                        switch (event.getPacketID()) {
                            case Packets.Server.BLOCK_CHANGE:
                                translateBlockChange(packet, world);
                                break;
                            case Packets.Server.MULTI_BLOCK_CHANGE:
                                translateMultiBlockChange(packet, world);
                                break;
                            case Packets.Server.MAP_CHUNK:
                                ChunkPacketProcessor.fromMapPacket(packet, world).process(processor);
                                break;
                            case Packets.Server.MAP_CHUNK_BULK:
                                for (ChunkPacketProcessor chunk : ChunkPacketProcessor.fromMapBulkPacket(packet, world)) {
                                    chunk.process(processor);
                                }
                                break;
                        }
                    }
                });
    }

    public void close() {
        if (listener != null) {
            ProtocolLibrary.getProtocolManager().removePacketListener(listener);
            listener = null;
        }
    }

    private ChunkPacketProcessor.ChunkletProcessor getChunkletProcessor() {
        return new ChunkPacketProcessor.ChunkletProcessor() {
            @Override
            public void processChunklet(Location origin, byte[] data, int blockIndex, int dataIndex) {
                ChunkCoordinate coord = ChunkCoordinate.fromBlock(origin);
                World world = origin.getWorld();
                int originX = origin.getBlockX();
                int originY = origin.getBlockY();
                int originZ = origin.getBlockZ();

                for (BlockCoordinate position : translations.row(coord).keySet()) {
                    int posX = position.getX();
                    int posY = position.getY();
                    int posZ = position.getZ();

                    // Make sure we're inside the chunklet
                    if (posY >= originY && posY - originY < 16) {
                        int offset = blockIndex + (posX - originX) + (posZ - originZ) * 16 + (posY - originY) * 256;

                        data[offset] = (byte) translateBlockID(world, posX, posY, posZ, data[offset] & 0xFF);
                    }
                }
            }
        };
    }

    private void translateBlockChange(PacketContainer packet, World world) throws FieldAccessException {
        StructureModifier<Integer> ints = packet.getIntegers();
        int x = ints.read(0);
        int y = ints.read(1);
        int z = ints.read(2);
        int blockID = ints.read(3);

        System.out.println("Block change: " + x + ", " + y + ", " + z);

        // Convert using the tables
        ints.write(3, translateBlockID(world, x, y, z, blockID));
    }

    private void translateMultiBlockChange(PacketContainer packet, World world) throws FieldAccessException {
        StructureModifier<byte[]> byteArrays = packet.getByteArrays();
        StructureModifier<Integer> ints = packet.getIntegers();

        int baseX = ints.read(0) << 4;
        int baseZ = ints.read(1) << 4;
        BlockChangeArray data = new BlockChangeArray(byteArrays.read(0));

        for (int i = 0; i < data.getSize(); i++) {
            BlockChangeArray.BlockChange change = data.getBlockChange(i);

            change.setBlockID(translateBlockID(
                    world,
                    baseX + change.getRelativeX(),
                    change.getAbsoluteY(),
                    baseZ + change.getRelativeZ(),
                    change.getBlockID()
            ));
        }
        byteArrays.write(0, data.toByteArray());
    }

    private int translateBlockID(World world, int x, int y, int z, int blockID) {
        Integer translate = translations.get(
                ChunkCoordinate.fromBlock(world, x, z), new BlockCoordinate(x, y, z));

        // Use the existing block ID if not found
        return translate != null ? translate : blockID;
    }
}