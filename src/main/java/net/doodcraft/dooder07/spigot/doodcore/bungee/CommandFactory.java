package net.doodcraft.dooder07.spigot.doodcore.bungee;

import net.doodcraft.dooder07.spigot.doodcore.DoodLog;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;

import java.util.ArrayList;

public class CommandFactory {
    public static ArrayList<String> serverCommands = new ArrayList<>();

    public static void setupAbstractCommands() {
        for (String command : Settings.servers) {
            addAbstractCommand("server", command);
            serverCommands.add(command);
        }
    }

    public static void addAbstractCommand(String type, String label) {
        if (type.equals("server")) {
            ServerCommand command = new ServerCommand(label);
            command.register();
            DoodLog.log("Core", "&eRegistered new BungeeCord server command: &b" + label);
        }
    }
}
