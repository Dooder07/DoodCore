package net.doodcraft.dooder07.spigot.doodcore.bungee;

import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.spigot.doodcore.DoodLog;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class ServerCommand extends AbstractCommand {
    public ServerCommand(String command) {
        super(command);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(DoodCorePlugin.plugin, "BungeeCord");
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        if (sender instanceof Player) {
            Player player = (Player) sender;
            for (String server : CommandFactory.serverCommands) {
                if (label.equalsIgnoreCase(server)) {
                    try {
                        out.writeUTF("Connect");
                        out.writeUTF(server);
                    } catch (Exception ex) {
                        DoodLog.printError("Core", "&cError sending player to target server!", ex);
                    }
                    player.sendPluginMessage(DoodCorePlugin.plugin, "BungeeCord", b.toByteArray());
                    return true;
                }
            }
        } else {
            DoodLog.log("Core", "&cConsole can't switch servers!");
        }
        return false;
    }
}
