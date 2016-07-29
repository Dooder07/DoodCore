package net.doodcraft.dooder07.bungeecord.doodcore.features;

import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.User;
import net.doodcraft.dooder07.bungeecord.doodcore.BungeeLog;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GlobalAdminACommand extends Command {
    public GlobalAdminACommand() {
        super("a");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ProxiedPlayer) {
            User user = BungeePerms.getInstance().getPermissionsManager().getUser(commandSender.getName());

            if (user.hasPerm("core.command.bungee.admin")) {

                String name = user.getGroupByLadder("default").getPrefix() + commandSender.getName();

                StringBuilder builder = new StringBuilder();
                for (String s : args) {
                    builder.append(s).append(" &c");
                }

                for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                    if (BungeePerms.getInstance().getPermissionsManager().getUser(p.getName()).hasPerm("core.command.bungee.admin")) {
                        p.sendMessage(new TextComponent(BungeeLog.addColor("&8[&4A&8]&r " + name + "&8:&c " + builder.toString())));
                    }
                }
            } else {
                commandSender.sendMessage(new TextComponent(BungeeLog.addColor("&cNo permission.")));
            }
        } else {
            StringBuilder builder = new StringBuilder();
            for (String s : args) {
                builder.append(s).append(" &c");
            }

            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                if (BungeePerms.getInstance().getPermissionsManager().getUser(p.getName()).hasPerm("core.command.bungee.admin")) {
                    p.sendMessage(new TextComponent(BungeeLog.addColor("&8[&4A&8]&r &7CONSOLE&8:&c " + builder.toString())));
                }
            }
        }
    }
}
