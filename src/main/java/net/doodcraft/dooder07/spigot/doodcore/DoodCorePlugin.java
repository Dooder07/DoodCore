package net.doodcraft.dooder07.spigot.doodcore;

import net.doodcraft.dooder07.spigot.doodcore.bungee.BungeeCordListener;
import net.doodcraft.dooder07.spigot.doodcore.bungee.CommandFactory;
import net.doodcraft.dooder07.spigot.doodcore.commands.*;
import net.doodcraft.dooder07.spigot.doodcore.compat.Compatibility;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import net.doodcraft.dooder07.spigot.doodcore.features.*;
import net.doodcraft.dooder07.spigot.doodcore.sql.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public class DoodCorePlugin extends JavaPlugin {

    public static DoodCorePlugin plugin;
    public static long startTime;

    public static MySQL mySql = null;
    public static Connection connection = null;
    public static boolean usingMySQL = false;

    @Override
    public void onEnable() {
        startTime = System.currentTimeMillis();

        plugin = this;

        Settings.setupDefaults();
        Compatibility.checkHooks();

        initialize();
        registerListeners();
        setExecutors();

        String art = "\n .----------------.  .----------------. \n| .--------------. || .--------------. |\n| |  ________    | || |     ______   | |\n| | |_   ___ `.  | || |   .' ___  |  | |\n| |   | |   `. \\ | || |  / .'   \\_|  | |\n| |   | |    | | | || |  | |         | |\n| |  _| |___.' / | || |  \\ `.___.'\\  | |\n| | |________.'  | || |   `._____.'  | |\n| |              | || |              | |\n| '--------------' || '--------------' |\n '----------------'  '----------------' \n";

        DoodLog.log("DoodCore", art);
        DoodLog.log("DoodCore", "DoodCore v" + plugin.getDescription().getVersion() +  " is fully loaded! (" + (System.currentTimeMillis() - startTime) + "ms)");
    }

    @Override
    public void onDisable() {
        cleanUp();
    }

    public static void reload() {
        cleanUp();
        initialize();
    }

    public static void initialize() {
        // CONFIGURATION
        Settings.reload();

        // SQL
        if (Settings.useMySQL) {
            // MySQL
            DoodLog.log("DoodCore", "&7&oEstablishing link with MySQL..");
            try {
                mySql = new MySQL(Settings.mySQLHost, String.valueOf(Settings.mySQLPort), Settings.mySQLDatabase, Settings.mySQLUser, Settings.mySQLPass);
                connection = mySql.openConnection();
                DoodLog.log("DoodCore", "&aMySQL link established!");
                DoodLog.log("DoodCore", "&7However, no support for MySQL or SQLite has been added yet!");
                usingMySQL = true;
            } catch (Exception ex) {
                DoodLog.printError("DoodCore", "Unhandled exception connecting to MySQL database!", ex);
                DoodLog.log("DoodCore", "&cMySQL link failed! :(");
                usingMySQL = false;
            }
        } else {
            // SQLite
            DoodLog.log("DoodCore", "&cMySQL is disabled, however SQLite is not added/supported yet.");
            usingMySQL = false;
        }

        // FEATURES
        TimeRewards.addAllPlayers();
        PvPManager.addAllPlayers();

        // BUNGEECORD
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", new BungeeCordListener());
        CommandFactory.setupAbstractCommands();

        // ADD RECIPES
//        addRecipe(EasterEgg.getExplosiveArrowRecipe());

        // LOBBY
        if (Bukkit.getWorld("Lobby") != null) {
            Lobby.startFireworksDisplays(new Location(Bukkit.getWorld("Lobby"), -60.5, 76, -60.5));
            Lobby.startFireworksDisplays(new Location(Bukkit.getWorld("Lobby"), 60.5, 76, 60.5));
            Lobby.startFireworksDisplays(new Location(Bukkit.getWorld("Lobby"), -60.5, 76, 60.5));
            Lobby.startFireworksDisplays(new Location(Bukkit.getWorld("Lobby"), 60.5, 76, -60.5));
        }
    }

    public static void cleanUp() {
        // SQL
        if (usingMySQL) {
            try {
                if (mySql.checkConnection()) {
                    DoodLog.log("DoodCore", "&aMySQL link closed!");
                    mySql.closeConnection();
                }
            } catch (SQLException ex) {
                DoodLog.printError("DoodCore", "Unhandled exception closing MySQL connection!", ex);
            }
        }

        // FEATURES
        TimeRewards.removeAllPlayers();

        // LOBBY
        for (int task : Lobby.fireworksTasks) {
            Bukkit.getServer().getScheduler().cancelTask(task);
        }
    }

    public void registerListeners() {
        registerEvents(plugin, new PlayerWelcome());
        registerEvents(plugin, new TimeRewards());
        registerEvents(plugin, new ChorusFruit());
        registerEvents(plugin, new TownDistance());
        registerEvents(plugin, new MobFarming());
        registerEvents(plugin, new PotionBlocking());
        registerEvents(plugin, new SkeletonHorses());
        registerEvents(plugin, new StickDrops());
        registerEvents(plugin, new PvPManager());
        registerEvents(plugin, new ConnectMessages());
        registerEvents(plugin, new WatchList());
        registerEvents(plugin, new EasterEgg());
        registerEvents(plugin, new Lobby());
        registerEvents(plugin, new ChunkFix());
    }

    public static void registerEvents(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public void setExecutors() {
        getCommand("core").setExecutor(new Core());
        getCommand("compel").setExecutor(new Compel());
        getCommand("rename").setExecutor(new Rename());
        getCommand("relore").setExecutor(new Relore());
        getCommand("search").setExecutor(new Search());
        getCommand("watchlist").setExecutor(new WatchList());
    }

    public static void addRecipe(Recipe recipe) {
        DoodCorePlugin.plugin.getServer().addRecipe(recipe);
    }
}
