package net.doodcraft.dooder07.spigot.doodcore;

import net.doodcraft.dooder07.spigot.doodcore.commands.Compel;
import net.doodcraft.dooder07.spigot.doodcore.commands.Core;
import net.doodcraft.dooder07.spigot.doodcore.commands.Relore;
import net.doodcraft.dooder07.spigot.doodcore.commands.Rename;
import net.doodcraft.dooder07.spigot.doodcore.compat.Compatibility;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import net.doodcraft.dooder07.spigot.doodcore.features.PlayerWelcome;
import net.doodcraft.dooder07.spigot.doodcore.features.TabMenu;
import net.doodcraft.dooder07.spigot.doodcore.features.TimeRewards;
import net.doodcraft.dooder07.spigot.doodcore.sql.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * The MIT License (MIT)
 * -
 * Copyright (c) 2016 Conor O'Shields
 * -
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * -
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * -
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class DoodCorePlugin extends JavaPlugin {

    public static DoodCorePlugin plugin;
    public static long startTime;
    public static List<Integer> tasks;

    public static MySQL mySql = null;
    public static Connection connection = null;
    public static boolean usingMySQL = false;

    @Override
    public void onEnable() {
        startTime = System.currentTimeMillis();

        plugin = this;

        Settings.setupDefaults();
        Compatibility.checkHooks();
        TabMenu.startRefresh();

        initialize();
        registerListeners();
        setExecutors();

        DoodLog.log("DoodCore", "DoodCore is fully loaded! (" + (System.currentTimeMillis() - startTime) + "ms)");
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

        // TASKS
        for (int task : tasks) {
            try {
                Bukkit.getScheduler().cancelTask(task);
                DoodLog.log("DoodCore", "Stopped task [" + task + "]");
            } catch (Exception ex) {
                DoodLog.printError("DoodCore", "Error stopping task " + task + "! (Already stopped?)", ex);
            }
        }

        // FEATURES
        TimeRewards.removeAllPlayers();
    }

    public void registerListeners() {
        registerEvents(plugin, new PlayerWelcome());
        registerEvents(plugin, new TimeRewards());
        registerEvents(plugin, new TabMenu());
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
    }
}
