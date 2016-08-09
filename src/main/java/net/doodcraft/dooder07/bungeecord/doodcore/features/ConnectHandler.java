package net.doodcraft.dooder07.bungeecord.doodcore.features;

import net.alpenblock.bungeeperms.BungeePerms;
import net.doodcraft.dooder07.bungeecord.doodcore.BungeeLog;
import net.doodcraft.dooder07.bungeecord.doodcore.DoodCorePlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// todo: Handle the player's target server quickly and without disturbing the player's QoS.
public class ConnectHandler implements Listener {

    public static ArrayList<ProxiedPlayer> connected = new ArrayList<>();

    public static Configuration getConfig(String name) {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(DoodCorePlugin.plugin.getDataFolder() + File.separator + "players" + File.separator, name + ".yml"));
        } catch (IOException e) {
            try {
                BungeeLog.log("DoodCore", "Creating player config for " + name + "..");

                Configuration config = new Configuration();
                saveConfig(config, name);

                return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(DoodCorePlugin.plugin.getDataFolder() + File.separator + "players" + File.separator, name + ".yml"));
            } catch (IOException e1) {
                BungeeLog.log("DoodCore", "There was an error saving a file - check read/write perms..?");
                e1.printStackTrace();
                return null;
            }
        }
    }

    public static void saveConfig(Configuration config, String name) throws IOException {

        try {

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(DoodCorePlugin.plugin.getDataFolder() + File.separator + "players" + File.separator, name + ".yml"));

        } catch (IOException e) {

            if (new File(DoodCorePlugin.plugin.getDataFolder() + File.separator + "players" + File.separator).mkdirs()) {
                BungeeLog.log("DoodCore", "Creating 'players' directory..");
            }

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(DoodCorePlugin.plugin.getDataFolder() + File.separator + "players" + File.separator, name + ".yml"));
        }
    }

    @EventHandler
    public void onConnected(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (BungeePerms.getInstance().getPermissionsManager() != null) {

            if (!connected.contains(event.getPlayer())) {
                String formattedName = BungeePerms.getInstance().getPermissionsManager().getUser(player.getName()).getGroupByLadder("default").getPrefix() + player.getName();

                BungeeLog.log(formattedName + " &ejoined the game from IP: &b" + player.getAddress());
                Configuration config = getConfig(event.getPlayer().getName());

                ProxyServer.getInstance().getPlayers().stream().filter(p -> !event.getPlayer().equals(p)).forEach(p -> {
                    p.sendMessage(new TextComponent(BungeeLog.addColor("&8[" + formattedName + "&8]&e joined the game.")));
                });

                connected.add(player);
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) throws IOException {
        if (BungeePerms.getInstance().getPermissionsManager() != null) {
            ProxiedPlayer player = event.getPlayer();
            String formattedName = BungeePerms.getInstance().getPermissionsManager().getUser(player.getName()).getGroupByLadder("default").getPrefix() + player.getName();

            String name = player.getName();
            String address = player.getAddress().toString().replaceAll(":" + player.getAddress().getPort(), "");

            BungeeLog.log(formattedName + " &equit the game.");

            if (connected.contains(player)) {
                connected.remove(player);
            }

            Configuration config = getConfig(name);

            if (config != null) {
                // Save last IP to address list
                if (!config.getList("Addresses").contains(address)) {
                    List<String> strings = config.getStringList("Addresses");
                    strings.add(address);
                    config.set("Addresses", strings);
                }

                // Save their last connected server
                config.set("LastServer", player.getServer().getInfo().getName());

                // Save config
                saveConfig(config, name);
            }

            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                p.sendMessage(new TextComponent(BungeeLog.addColor("&8[" + formattedName + "&8]&e quit the game.")));
            }
        }
    }
}
