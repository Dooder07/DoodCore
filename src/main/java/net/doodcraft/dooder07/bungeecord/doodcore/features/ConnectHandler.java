package net.doodcraft.dooder07.bungeecord.doodcore.features;

import net.alpenblock.bungeeperms.BungeePerms;
import net.doodcraft.dooder07.bungeecord.doodcore.BungeeLog;
import net.doodcraft.dooder07.bungeecord.doodcore.DoodCorePlugin;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// todo: increase speed and efficiency in finding a valid server for new and returning players. The goal is total seamlessness.
public class ConnectHandler implements Listener {

    public static String DEFAULT_SERVER = "SMP";
    public static String FALLBACK_SERVER = "Lobby";

    public static Configuration getConfig(String name) {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(DoodCorePlugin.plugin.getDataFolder() + File.separator + "players" + File.separator, name + ".yml"));
        } catch (IOException e) {
            try {
                BungeeLog.log("DoodCore", "Creating player config for " + name + "..");

                Configuration config = new Configuration();
                config.set("LastServer", DEFAULT_SERVER);

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
    public void onServerConnect(ServerConnectEvent event) {
        // Snip
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) throws IOException {

        ProxiedPlayer player = event.getPlayer();

        String formattedName = BungeePerms.getInstance().getPermissionsManager().getUser(player.getName()).getGroupByLadder("default").getPrefix() + player.getName();

        BungeeLog.log("DoodCore", formattedName + " &7is connecting from &b" + player.getAddress());

        Configuration config = getConfig(event.getPlayer().getName());

        ProxyServer.getInstance().getPlayers().stream().filter(p -> !event.getPlayer().equals(p)).forEach(p -> {
            p.sendMessage(new TextComponent(BungeeLog.addColor("&8[" + formattedName + "&8]&e joined the game.")));
        });

        // Are they new?
        if (config != null) {
            if (!config.getBoolean("Welcomed")) {
                try {
                    // They are new, lets welcome them!
                    BungeeLog.log("DoodCore", formattedName + " &dis a new player. Flipping config value..");
                    config.set("Welcomed", true);
                    saveConfig(config, event.getPlayer().getName());

                    DoodCorePlugin.plugin.getProxy().getScheduler().schedule(DoodCorePlugin.plugin, () -> {
                        if (player.isConnected()) {
                            player.sendMessage(new TextComponent(BungeeLog.addColor("&dWelcome to Dreadcraft " + formattedName + "&d!")));
                        }
                    }, 5, TimeUnit.SECONDS);

                    ProxyServer.getInstance().getPlayers().stream().filter(p -> !event.getPlayer().equals(p)).forEach(p -> {
                        p.sendMessage(new TextComponent(BungeeLog.addColor("&8-= &dWelcome " + formattedName + " &dto Dreadcraft! &8=-")));
                    });

                    // Now lets try to send them to SMP by default, then the Lobby next.
                    ServerInfo server = ProxyServer.getInstance().getServerInfo(DEFAULT_SERVER);
                    server.ping(new Callback<ServerPing> () {
                        @Override
                        public void done(ServerPing serverPing, Throwable throwable) {
                            if (throwable == null) {
                                BungeeLog.log("DoodCore", DEFAULT_SERVER + " is online. Sending " + formattedName + " &rthere now.");
                                player.connect(server);
                            } else {
                                // Now let's ping the fallback server and send them there. If the fallback server is down, let's find the first available server instead.
                                BungeeLog.log("DoodCore", DEFAULT_SERVER + " couldn't be reached. Checking fallback server status...");

                                ServerInfo server = ProxyServer.getInstance().getServerInfo(FALLBACK_SERVER);
                                server.ping(new Callback<ServerPing>() {
                                    @Override
                                    public void done(ServerPing serverPing, Throwable throwable) {
                                        if (throwable == null) {
                                            BungeeLog.log("DoodCore", FALLBACK_SERVER + " is online. Sending " + formattedName + " &rthere now.");
                                            player.connect(ProxyServer.getInstance().getServerInfo(FALLBACK_SERVER));
                                        } else {
                                            // Now let's ping the Lobby and send them there. If the lobby is down, let's find the first available server instead.
                                            BungeeLog.log("DoodCore", FALLBACK_SERVER + " couldn't be reached. Looking for first available server...");
                                            if (!sendToFirstAvailable(player)) {
                                                BungeeLog.log("DoodCore", "No servers could be reached. Kicking the player now.");
                                                player.getPendingConnection().disconnect(new TextComponent(BungeeLog.addColor("&eIt appears there are no online servers at this time.. Please try again later!")));
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                BungeeLog.log("DoodCore", formattedName + " &dis not a new player.");

                ServerInfo server = ProxyServer.getInstance().getServerInfo(config.getString("LastServer"));
                server.ping(new Callback<ServerPing>() {
                    @Override
                    public void done(ServerPing serverPing, Throwable throwable) {
                        if (throwable == null) {
                            BungeeLog.log("DoodCore", config.getString("LastServer") + " is online. Sending " + formattedName + " &rthere now.");
                            // BungeeCord should send them there by default. Unless anything changes in the future, let's do nothing.
                            return;
                        } else {
                            // Now let's ping the fallback server and send them there. If the fallback server is down, let's find the first available server instead.
                            BungeeLog.log("DoodCore", config.getString("LastServer") + " couldn't be reached. Checking fallback server status...");

                            ServerInfo server = ProxyServer.getInstance().getServerInfo(FALLBACK_SERVER);
                            server.ping(new Callback<ServerPing>() {
                                @Override
                                public void done(ServerPing serverPing, Throwable throwable) {
                                    if (throwable == null) {
                                        BungeeLog.log("DoodCore", FALLBACK_SERVER + " is online. Sending " + formattedName + " &rthere now.");
                                        player.connect(ProxyServer.getInstance().getServerInfo(FALLBACK_SERVER));
                                    } else {
                                        // Now let's ping the Lobby and send them there. If the lobby is down, let's find the first available server instead.
                                        BungeeLog.log("DoodCore", FALLBACK_SERVER + " couldn't be reached. Looking for first available server...");
                                        if (!sendToFirstAvailable(player)) {
                                            BungeeLog.log("DoodCore", "No servers could be reached. Kicking the player now.");
                                            player.getPendingConnection().disconnect(new TextComponent(BungeeLog.addColor("&eIt appears there are no online servers at this time.. Please try again later!")));
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }
        } else {
            BungeeLog.log("DoodCore", "Error finding configuration file. It was null?");
        }
    }

    public boolean sendToFirstAvailable(ProxiedPlayer player) {
        final boolean[] connected = {false};

        for (Map.Entry<String, ServerInfo> entry : ProxyServer.getInstance().getServers().entrySet()) {
            entry.getValue().ping(new Callback<ServerPing>() {
                @Override
                public void done(ServerPing serverPing, Throwable throwable) {
                    if (throwable == null) {
                        BungeeLog.log("DoodCore", entry.getKey() + " is online. Sending " + player.getDisplayName() + " &rthere now.");
                        player.connect(entry.getValue());
                        connected[0] = true;
                        return;
                    }
                    // offline, try the next server.
                }
            });
        }

        return connected[0];
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) throws IOException {
        ProxiedPlayer player = event.getPlayer();
        String name = player.getName();
        String address = player.getAddress().toString().replaceAll(":" + player.getAddress().getPort(), "") + " | " + player.getAddress().getHostString();

        BungeeLog.log("DoodCore", name + " quit the game.");

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

        if (BungeePerms.getInstance().getPermissionsManager() != null) {
            name = BungeePerms.getInstance().getPermissionsManager().getUser(name).getGroupByLadder("default").getPrefix() + name;

            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {

                p.sendMessage(new TextComponent(BungeeLog.addColor("&8[" + name + "&8]&e quit the game.")));
            }
        }
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String name = player.getName();

        if (BungeePerms.getInstance().getPermissionsManager() != null) {
            name = BungeePerms.getInstance().getPermissionsManager().getUser(name).getGroupByLadder("default").getPrefix() + name;
            BungeeLog.log("DoodCore", name + "&8: &7" + event.getMessage());
        }
    }
}
