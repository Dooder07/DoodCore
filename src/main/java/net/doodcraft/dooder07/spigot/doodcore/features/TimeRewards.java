package net.doodcraft.dooder07.spigot.doodcore.features;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import mkremins.fanciful.FancyMessage;
import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.spigot.doodcore.Methods;
import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import net.doodcraft.dooder07.spigot.doodcore.compat.Compatibility;
import net.doodcraft.dooder07.spigot.doodcore.compat.Vault;
import net.doodcraft.dooder07.spigot.doodcore.config.Configuration;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class TimeRewards implements Listener {

    public static HashMap<UUID, Integer> earningTasks = new HashMap<>();

    public static Configuration getConfig(String name) {
        return new Configuration(DoodCorePlugin.plugin.getDataFolder() + File.separator + "rewards" + File.separator + name.toLowerCase() +".yml");
    }

    @EventHandler
    public static void onJoin(PlayerJoinEvent event) {
        addPayee(event.getPlayer());
    }

    public static void addPayee(Player player) {
        Configuration config = getConfig(player.getName());

        if (!Settings.timeRewardsEnabled) {
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();

        if (!config.contains("LastReward")) {
            config.add("Earned.Today", "0");
            config.add("Earned.AllTime", "0");
            config.add("LastReward", dateFormat.format(date));
            config.save();
        }

        if (earningTasks.get(player.getUniqueId()) == null) {
            int earnedToday = config.getInteger("Earned.Today");
            String lastReward = config.getString("LastReward");
            boolean newDay = compareDates(dateFormat.format(date), lastReward);

            if (newDay) {
                config.set("Earned.Today", 0);
                config.set("LastReward", dateFormat.format(date));
                config.save();
                startPayout(player);
                return;
            }

            if (earnedToday < Settings.timeRewardsMax) {
                startPayout(player);
            }
        }
    }

    public static void removePayee(Player player) {
        stopPayout(player);
    }

    public static void startPayout(final Player player) {
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(DoodCorePlugin.plugin, () -> {
            Configuration config = getConfig(player.getName());
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date();

            int earnedToday = config.getInteger("Earned.Today");
            String lastReward = config.getString("LastReward");
            boolean newDay = compareDates(dateFormat.format(date), lastReward);

            if (Bukkit.getPlayer(player.getUniqueId()) == null) {
                removePayee(player);
            } else {
                if (Compatibility.isHooked("Essentials")) {
                    Essentials ess = (Essentials) Compatibility.getPlugin("Essentials");
                    User p = ess.getUser(player);

                    if (p.isAfk()) {
                        player.sendMessage(StringParser.addColor("&cYou did not earn money because you were marked afk."));
                        return;
                    }
                }

                if (earnedToday >= Settings.timeRewardsMax) {
                    if (newDay) {
                        config.set("Earned.Today", 0);
                        config.set("LastReward", dateFormat.format(date));
                        config.save();
                        pay(player);
                    }
                } else {
                    pay(player);
                }
            }
        }, Settings.timeRewardsInterval, Settings.timeRewardsInterval);

        earningTasks.put(player.getUniqueId(), taskId);
    }

    public static void stopPayout(Player player) {
        if (earningTasks.containsKey(player.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(earningTasks.get(player.getUniqueId()));
            earningTasks.remove(player.getUniqueId());
        }
    }

    public static void pay(Player player) {
        Configuration config = getConfig(player.getName());

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();

        int earnedToday = config.getInteger("Earned.Today");
        int earnedAllTime = config.getInteger("Earned.AllTime");

        if (Compatibility.isHooked("Vault")) {
            Economy econ = Vault.economy;
            econ.depositPlayer(player, Settings.timeRewardsAmount);

            config.set("Earned.Today", earnedToday + Settings.timeRewardsAmount);
            config.set("Earned.AllTime", earnedAllTime + Settings.timeRewardsAmount);
            config.set("LastReward", dateFormat.format(date));

            config.save();

            int today = earnedToday + Settings.timeRewardsAmount;

            FancyMessage msg = new FancyMessage(StringParser.addColor(Settings.pluginPrefix + " " + Settings.timeRewardMessage.replaceAll("<amount>", String.valueOf(Settings.timeRewardsAmount))));
            msg.tooltip(StringParser.addColor("&8[&b$<today>&8/&9$<max>&8]".replaceAll("<today>", String.valueOf(today)).replaceAll("<max>", String.valueOf(Settings.timeRewardsMax))));

            Methods.sendFancyMessage(player, msg);
        } else {
            player.sendMessage(StringParser.addColor("&cThere was an error with TimeRewards! Tell an admin!"));
        }
    }

    public static boolean compareDates(String one, String two) {
        return !one.equals(two);
    }

    public static void addAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(TimeRewards::addPayee);
    }

    public static void removeAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(TimeRewards::removePayee);
    }
}
