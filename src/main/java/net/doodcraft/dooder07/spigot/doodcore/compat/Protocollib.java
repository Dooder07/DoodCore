package net.doodcraft.dooder07.spigot.doodcore.compat;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class Protocollib {
    public static ProtocolManager getManager() {
        if (Compatibility.hooked.get("ProtocolLib") != null) {
            return ProtocolLibrary.getProtocolManager();
        } else {
            return null;
        }
    }
}
