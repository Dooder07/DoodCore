package net.doodcraft.dooder07.spigot.doodcore.config;

import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;
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
public class Settings {

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

    public static Boolean timeRewardsEnabled;
    public static int timeRewardsAmount;
    public static int timeRewardsMax;
    public static int timeRewardsInterval;

    public static List<String> validWelcomeList;

    public static int townDistance;
    public static List<String> townDistanceChecklist;
    public static int chorusDistance;
    public static boolean checkChorusFruitTeleport;

    public static List<String> bruteForcePasswords;

    public static int pvpNewbTime;
    public static List<String> combatBlockedCommands;

    public static boolean despawnSkeletonHorses;
    public static int skeletonHorseReduction;

    public static boolean stickDrops;

    // LOCALE
    public static String serverName;
    public static String pluginPrefix;
    public static String timeRewardMessage;

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

        timeRewardsEnabled = true;
        timeRewardsAmount = 50;
        timeRewardsMax = 1000;
        timeRewardsInterval = 18000;

        validWelcomeList = new ArrayList<>();
        validWelcomeList.add("welcome");
        validWelcomeList.add("whalecum");
        validWelcomeList.add("greetings");
        validWelcomeList.add("hi");
        validWelcomeList.add("hello");
        validWelcomeList.add("herro");
        validWelcomeList.add("hey");
        validWelcomeList.add("hola");
        validWelcomeList.add("yo");
        validWelcomeList.add("yoh");
        validWelcomeList.add("heya");
        validWelcomeList.add("welcum");
        validWelcomeList.add("bienvenidos");
        validWelcomeList.add("velcome");

        townDistance = 200;
        townDistanceChecklist = new ArrayList<>();
        townDistanceChecklist.add("wxcomplete");
        townDistanceChecklist.add("sethome");
        townDistanceChecklist.add("t claim");
        townDistanceChecklist.add("town claim");
        chorusDistance = 10;
        checkChorusFruitTeleport = true;

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

        pvpNewbTime = 360;
        combatBlockedCommands = new ArrayList<>();
        combatBlockedCommands.add("spawn");
        combatBlockedCommands.add("tpa");
        combatBlockedCommands.add("tpahere");
        combatBlockedCommands.add("t");
        combatBlockedCommands.add("home");
        combatBlockedCommands.add("ptp");
        combatBlockedCommands.add("dial");
        combatBlockedCommands.add("warp");


        despawnSkeletonHorses = false;
        skeletonHorseReduction = 75;

        // LOCALE
        serverName = Bukkit.getServerName();
        pluginPrefix = "&e[&6DoodCore&e]&r";
        timeRewardMessage = "&aHere's &2$<amount> - &ajust for playing!";

        // BUILD THE DEFAULT CONFIGS
        Configuration config = new Configuration(DoodCorePlugin.plugin.getDataFolder() + File.separator + "config.yml");
        Configuration locale = new Configuration(DoodCorePlugin.plugin.getDataFolder() + File.separator + "locale.yml");

        //      POPULATE IF DOES NOT ALREADY EXIST!
        //      CONFIG
        config.add("General.ColorfulLogging", colorfulLogging);

        config.add("General.DebugMode", debugMode);
        config.add("General.Developer.Name", developerName);
        config.add("General.Developer.SendMessages", messageDeveloper);

        config.add("MySQL.Enabled", useMySQL);
        config.add("MySQL.Host", mySQLHost);
        config.add("MySQL.Port", mySQLPort);
        config.add("MySQL.Database", mySQLDatabase);
        config.add("MySQL.Password", mySQLPass);
        config.add("MySQL.User", mySQLUser);

        config.add("TimeRewards.Enabled", timeRewardsEnabled);
        config.add("TimeRewards.Amount", timeRewardsAmount);
        config.add("TimeRewards.Max", timeRewardsMax);
        config.add("TimeRewards.Interval", timeRewardsInterval);

        config.add("Welcome.ValidGreetings", validWelcomeList);

        config.add("Compliance.TownCheck.Distance", townDistance);
        config.add("Compliance.TownCheck.Commands", townDistanceChecklist);
        config.add("Compliance.ChorusFruit.Distance", chorusDistance);
        config.add("Compliance.ChorusFruit.CheckTeleport", checkChorusFruitTeleport);

        config.add("AntiHack.PasswordBruteForce.PassList", bruteForcePasswords);

        config.add("PvPManager.DefaultNewbTime", pvpNewbTime);
        config.add("PvPManager.CombatBlockedCommands", combatBlockedCommands);

        config.add("VanillaMod.SkeletonHorses.Despawn", despawnSkeletonHorses);
        config.add("VanillaMod.SkeletonHorses.ReductionPercent", skeletonHorseReduction);

        config.add("VanillaMod.StickDrops", stickDrops);

        //      LOCALE
        locale.add("Server.Name", serverName);
        locale.add("Server.PluginPrefix", pluginPrefix);
        locale.add("TimeRewards.Rewarded", timeRewardMessage);

        config.save();
        locale.save();
        setNewConfigValues(config);
        setNewLocaleValues(locale);
    }

    public static void setNewConfigValues(Configuration config) {
        colorfulLogging = config.getBoolean("General.ColorfulLogging");

        debugMode = config.getBoolean("General.DebugMode");
        developerName = config.getString("General.Developer.Name");
        messageDeveloper = config.getBoolean("General.Developer.SendMessages");

        useMySQL = config.getBoolean("MySQL.Enabled");
        mySQLHost = config.getString("MySQL.Host");
        mySQLPort = config.getInteger("MySQL.Port");
        mySQLDatabase = config.getString("MySQL.Database");
        mySQLPass = config.getString("MySQL.Password");
        mySQLUser = config.getString("MySQL.User");

        timeRewardsEnabled = config.getBoolean("TimeRewards.Enabled");
        timeRewardsAmount = config.getInteger("TimeRewards.Amount");
        timeRewardsMax = config.getInteger("TimeRewards.Max");
        timeRewardsInterval = config.getInteger("TimeRewards.Interval");

        validWelcomeList = config.getStringList("Welcome.ValidGreetings");

        townDistance = config.getInteger("Compliance.TownCheck.Distance");
        townDistanceChecklist = config.getStringList("Compliance.TownCheck.Commands");
        chorusDistance = config.getInteger("Compliance.ChorusFruit.Distance");
        checkChorusFruitTeleport = config.getBoolean("Compliance.ChorusFruit.CheckTeleport");

        pvpNewbTime = config.getInteger("PvPManager.DefaultNewbTime");
        combatBlockedCommands = config.getStringList("PvPManager.CombatBlockedCommands");

        despawnSkeletonHorses = config.getBoolean("VanillaMod.SkeletonHorses.Despawn");
        skeletonHorseReduction = config.getInteger("VanillaMod.SkeletonHorses.ReductionPercent");

        stickDrops = config.getBoolean("VanillaMod.StickDrops");
    }

    public static void setNewLocaleValues(Configuration locale) {
        serverName = locale.getString("Server.Name");
        pluginPrefix = locale.getString("Server.PluginPrefix");
        timeRewardMessage = locale.getString("TimeRewards.Rewarded");
    }

    public static void reload() {
        Configuration config = new Configuration(DoodCorePlugin.plugin.getDataFolder() + File.separator + "config.yml");
        Configuration locale = new Configuration(DoodCorePlugin.plugin.getDataFolder() + File.separator + "locale.yml");

        setNewConfigValues(config);
        setNewLocaleValues(locale);
    }
}
