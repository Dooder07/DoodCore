package net.doodcraft.dooder07.bungeecord.doodcore.config;

import net.doodcraft.dooder07.bungeecord.doodcore.DoodCorePlugin;
import net.md_5.bungee.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Settings {
    public static Configuration getConfig() {
        return BungeeConfig.getConfig(new File(DoodCorePlugin.plugin.getDataFolder() + File.separator + "config.yml"));
    }

    public static Configuration getLocale() {
        return BungeeConfig.getConfig(new File(DoodCorePlugin.plugin.getDataFolder() + File.separator + "locale.yml"));
    }

    // CONFIG
    public static Boolean colorfulLogging;
    public static Boolean debugMode;
    public static String developerName;
    public static boolean messageDeveloper;

    public static Boolean useMySQL;
    public static String mySQLHost;
    public static int mySQLPort;
    public static String mySQLUser;
    public static String mySQLPass;
    public static String mySQLDatabase;

    public static List<String> bruteForcePasswords;

    // LOCALE
    public static String pluginPrefix;

    public static void setupDefaults() {
        // CONFIG
        colorfulLogging = false;

        debugMode = false;
        developerName = "Dooder07";
        messageDeveloper = false;

        useMySQL = false;
        mySQLHost = "localhost";
        mySQLPort = 3306;
        mySQLDatabase = "core";
        mySQLPass = "password";
        mySQLUser = "root";

        bruteForcePasswords = new ArrayList<>();
        bruteForcePasswords.add("password");
        bruteForcePasswords.add("passwort");
        bruteForcePasswords.add("password1");
        bruteForcePasswords.add("passwort1");
        bruteForcePasswords.add("pass");
        bruteForcePasswords.add("wurst");
        bruteForcePasswords.add("admin");
        bruteForcePasswords.add("hallo");
        bruteForcePasswords.add("auth");
        bruteForcePasswords.add("authme");
        bruteForcePasswords.add("pw");
        bruteForcePasswords.add("me");
        bruteForcePasswords.add("qwerty");

        // LOCALE
        pluginPrefix = "&e[&6DoodCore&e]&r";

        // BUILD THE DEFAULT CONFIGS
        Configuration config = getConfig();
        Configuration locale = getLocale();

        //      POPULATE IF DOES NOT ALREADY EXIST!
        //      CONFIG
        add(config, "General.ColorfulLogging", colorfulLogging);

        add(config, "General.DebugMode", debugMode);
        add(config, "General.Developer.Name", developerName);
        add(config, "General.Developer.SendMessages", messageDeveloper);

        add(config, "MySQL.Enabled", useMySQL);
        add(config, "MySQL.Host", mySQLHost);
        add(config, "MySQL.Port", mySQLPort);
        add(config, "MySQL.Database", mySQLDatabase);
        add(config, "MySQL.Password", mySQLPass);
        add(config, "MySQL.User", mySQLUser);

        add(config, "AntiHack.PasswordBruteForce.PassList", bruteForcePasswords);

        //      LOCALE
        add(locale, "Server.PluginPrefix", pluginPrefix);

        // save
        try {
            BungeeConfig.saveConfig(config, new File(DoodCorePlugin.plugin.getDataFolder() + File.separator + "config.yml"));
            BungeeConfig.saveConfig(locale, new File(DoodCorePlugin.plugin.getDataFolder() + File.separator + "locale.yml"));

            // set
            setNewConfigValues(config);
            setNewLocaleValues(locale);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setNewConfigValues(Configuration config) {
        colorfulLogging = config.getBoolean("General.ColorfulLogging");

        debugMode = config.getBoolean("General.DebugMode");
        developerName = config.getString("General.Developer.Name");
        messageDeveloper = config.getBoolean("General.Developer.SendMessages");

        useMySQL = config.getBoolean("MySQL.Enabled");
        mySQLHost = config.getString("MySQL.Host");
        mySQLPort = config.getInt("MySQL.Port");
        mySQLDatabase = config.getString("MySQL.Database");
        mySQLPass = config.getString("MySQL.Password");
        mySQLUser = config.getString("MySQL.User");
    }

    public static void setNewLocaleValues(Configuration locale) {
        pluginPrefix = locale.getString("Server.PluginPrefix");
    }

    public static void reload() {
        Configuration config = getConfig();
        Configuration locale = getLocale();

        setNewConfigValues(config);
        setNewLocaleValues(locale);
    }

    public static void add(Configuration config, String key, Object value) {
        config.set(key, value);
    }
}
