package net.doodcraft.dooder07.bungeecord.doodcore;

import net.doodcraft.dooder07.bungeecord.doodcore.anticheat.PasswordBruteForce;
import net.doodcraft.dooder07.bungeecord.doodcore.config.Settings;
import net.doodcraft.dooder07.bungeecord.doodcore.features.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class DoodCorePlugin extends Plugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        Long start = System.currentTimeMillis();

        plugin = this;

        Settings.setupDefaults();

        registerEvents(plugin, new ConnectHandler(), new BungeeLog(), new MaintenanceMode(), new PasswordBruteForce());
        registerCommands();

        PasswordBruteForce.addAllPlayers();

        BungeeLog.log("DoodCore", "Enabled! &e(" + String.valueOf(System.currentTimeMillis() - start) + "&ems)");
    }

    @Override
    public void onDisable() {
        PasswordBruteForce.removeAllPlayers();
    }

    public void registerEvents(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            getProxy().getPluginManager().registerListener(plugin, listener);
        }
    }

    public void registerCommands() {
        getProxy().getPluginManager().registerCommand(this, new GlobalSayCommand());
        getProxy().getPluginManager().registerCommand(this, new GlobalListCommand());
        getProxy().getPluginManager().registerCommand(this, new GlobalWhoCommand());
        getProxy().getPluginManager().registerCommand(this, new GlobalPlayersCommand());
        getProxy().getPluginManager().registerCommand(this, new GlobalOnlineCommand());
        getProxy().getPluginManager().registerCommand(this, new GlobalStaffCommand());
        getProxy().getPluginManager().registerCommand(this, new GlobalStaffSCommand());
        getProxy().getPluginManager().registerCommand(this, new GlobalAdminCommand());
        getProxy().getPluginManager().registerCommand(this, new GlobalAdminACommand());
        getProxy().getPluginManager().registerCommand(this, new MaintenanceModeCommand());
    }
}
