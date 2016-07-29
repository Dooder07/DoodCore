package net.doodcraft.dooder07.bungeecord.doodcore.features;

import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.User;
import net.doodcraft.dooder07.bungeecord.doodcore.BungeeLog;
import net.doodcraft.dooder07.bungeecord.doodcore.DoodCorePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GlobalSayCommand extends Command {

    public GlobalSayCommand() {
        super("gs");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s).append(" ");
        }

        if (commandSender instanceof ProxiedPlayer) {
            User user = BungeePerms.getInstance().getPermissionsManager().getUser(commandSender.getName());

            if (user.hasPerm("core.command.bungee.say")) {
                String name = user.getGroupByLadder("default").getPrefix() + commandSender.getName();
                DoodCorePlugin.plugin.getProxy().broadcast(new TextComponent(BungeeLog.addColor("&8[&7" + name + "&8] &f" + builder.toString())));
            } else {
                commandSender.sendMessage(new TextComponent(BungeeLog.addColor("&cNo permission.")));
            }
        } else {
            String name = "CONSOLE";
            DoodCorePlugin.plugin.getProxy().broadcast(new TextComponent(BungeeLog.addColor("&8[&7" + name + "&8] &f" + builder.toString())));
        }
    }
}
