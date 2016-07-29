package net.doodcraft.dooder07.bungeecord.doodcore.features;

import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.PermissionsManager;
import net.alpenblock.bungeeperms.User;
import net.doodcraft.dooder07.bungeecord.doodcore.BungeeLog;
import net.doodcraft.dooder07.bungeecord.doodcore.DoodCorePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

public class GlobalListCommand extends Command {
    public GlobalListCommand() {
        super("list");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

        if (BungeePerms.getInstance().getPermissionsManager() != null) {

            List<String> members = new ArrayList<>();
            List<String> donators = new ArrayList<>();
            List<String> moderators = new ArrayList<>();
            List<String> admins = new ArrayList<>();

            for (ProxiedPlayer p : DoodCorePlugin.plugin.getProxy().getPlayers()) {

                PermissionsManager pm = BungeePerms.getInstance().getPermissionsManager();
                User user = pm.getUser(p.getName());

                if (user != null) {
                    if (user.getGroupByLadder("default").getName().equalsIgnoreCase("member")) {
                        members.add("&2" + p.getName());
                    }
                    if (user.getGroupByLadder("default").getName().equalsIgnoreCase("vip")) {
                        members.add("&2" + p.getName());
                    }
                    if (user.getGroupByLadder("default").getName().equalsIgnoreCase("donate1")) {
                        donators.add("&4" + p.getName());
                    }
                    if (user.getGroupByLadder("default").getName().equalsIgnoreCase("donate2")) {
                        donators.add("&6" + p.getName());
                    }
                    if (user.getGroupByLadder("default").getName().equalsIgnoreCase("donate3")) {
                        donators.add("&9" + p.getName());
                    }
                    if (user.getGroupByLadder("default").getName().equalsIgnoreCase("trainee")) {
                        moderators.add("&3" + p.getName());
                    }
                    if (user.getGroupByLadder("default").getName().equalsIgnoreCase("moderator")) {
                        moderators.add("&b" + p.getName());
                    }
                    if (user.getGroupByLadder("default").getName().equalsIgnoreCase("headmoderator")) {
                        moderators.add("&e" + p.getName());
                    }
                    if (user.getGroupByLadder("default").getName().equalsIgnoreCase("admin")) {
                        admins.add("&d" + p.getName());
                    }
                    if (user.getGroupByLadder("default").getName().equalsIgnoreCase("headadmin")) {
                        admins.add("&5" + p.getName());
                    }
                    if (user.getGroupByLadder("default").getName().equalsIgnoreCase("owner")) {
                        admins.add("&5&l" + p.getName());
                    }
                }
            }

            // do stuff with the collections
            commandSender.sendMessage(new TextComponent(BungeeLog.addColor("&8&l&m============================================")));

            if (admins.size() >= 1) {
                String adminsString = "&7Admins: " + String.join("&8,&r ", admins);
                commandSender.sendMessage(new TextComponent(BungeeLog.addColor(adminsString)));
            }

            if (moderators.size() >= 1) {
                String moderatorsString = "&7Moderators: " + String.join("&8,&r ", moderators);
                commandSender.sendMessage(new TextComponent(BungeeLog.addColor(moderatorsString)));
            }

            if (donators.size() >= 1) {
                String donatorsString = "&7Donators: " + String.join("&8,&r ", donators);
                commandSender.sendMessage(new TextComponent(BungeeLog.addColor(donatorsString)));
            }

            if (members.size() >= 1) {
                String membersString = "&7Members: " + String.join("&8,&r ", members);
                commandSender.sendMessage(new TextComponent(BungeeLog.addColor(membersString)));
            }

            commandSender.sendMessage(new TextComponent(BungeeLog.addColor("&8&l&m============================================")));
        }
    }
}
