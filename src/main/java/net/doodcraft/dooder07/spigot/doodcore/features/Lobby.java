package net.doodcraft.dooder07.spigot.doodcore.features;

import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.spigot.doodcore.StringParser;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.Random;

public class Lobby implements Listener {

    public static Random random = new Random();

    public static ArrayList<Integer> fireworksTasks = new ArrayList<>();

    public static void startFireworksDisplays(Location location) {
        int fireworksTask = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(DoodCorePlugin.plugin, () -> {

            int chance = random.nextInt(3) + 1;

            if (chance == 1) {
                int count = random.nextInt(10) + 3;
                launchRandomFireworks(count, location, 6L);
            }
        }, 1L, 120L);

        fireworksTasks.add(fireworksTask);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getClickedBlock() != null) {
            if (isSign(event.getClickedBlock().getType())) {
                Sign sign = (Sign) event.getClickedBlock().getState();

                if (sign.getLine(1).equalsIgnoreCase("[Leave]")) {
                    if (player.getWorld().equals(Bukkit.getWorld("TNTRun"))) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + player.getName() + " Lobby");
                        player.sendMessage(StringParser.addColor(Settings.pluginPrefix + " &7Returning you to the lobby."));
                    }
                }
            }
        }
    }

    public Boolean isSign(Material mat) {
        return mat.equals(Material.SIGN) || mat.equals(Material.SIGN_POST) || mat.equals(Material.WALL_SIGN);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (Bukkit.getWorld("Lobby") != null) {

            Bukkit.getScheduler().scheduleSyncDelayedTask(DoodCorePlugin.plugin, new Runnable() {

                @Override
                public void run() {
                    if (!player.hasPermission("core.admin.bypass")) {
                        player.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
                    }
                }
            },1L);

            Bukkit.getScheduler().scheduleSyncDelayedTask(DoodCorePlugin.plugin, new Runnable() {

                @Override
                public void run() {
                    player.playEffect(Bukkit.getWorld("Lobby").getSpawnLocation(), Effect.RECORD_PLAY, Material.RECORD_4);
                }
            },35L);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (Bukkit.getWorld("Lobby") != null) {
            if (player.getWorld().equals(Bukkit.getWorld("Lobby"))) {
                if (!player.hasPermission("core.admin.bypass")) {
                    if (event.getTo().getY() <= 57) {
                        player.teleport(player.getWorld().getSpawnLocation());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChange(PlayerChangedWorldEvent event) {
        if (event.getPlayer().getWorld().equals(Bukkit.getWorld("Lobby"))) {
            event.getPlayer().teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
            event.getPlayer().playEffect(Bukkit.getWorld("Lobby").getSpawnLocation(), Effect.RECORD_PLAY, Material.RECORD_4);
        }
    }

    @EventHandler
    public void onChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();

        if (Bukkit.getWorld("Lobby") != null) {
            if (player.getWorld().equals(Bukkit.getWorld("Lobby")) || player.getWorld().equals(Bukkit.getWorld("TNTRun"))) {
                event.setFoodLevel(20);
                event.setCancelled(true);
            }
        }
    }

    public static void launchRandomFireworks(final int count, final Location location, final Long delay) {
        Bukkit.getServer().getScheduler().runTaskLater(DoodCorePlugin.plugin, () -> {

            int r1i = random.nextInt(17) + 1;
            int r2i = random.nextInt(17) + 1;
            int rp = 3;
            int rt = random.nextInt(4) + 1;

            Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();
            FireworkEffect.Type type = null;

            if (rt == 1) type = FireworkEffect.Type.BALL;
            if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
            if (rt == 3) type = FireworkEffect.Type.BURST;
            if (rt == 4) type = FireworkEffect.Type.BALL;
            if (rt == 5) type = FireworkEffect.Type.STAR;

            Color c1 = getFireworkColor(r1i);
            Color c2 = getFireworkColor(r2i);

            assert type != null;
            FireworkEffect effect = FireworkEffect.builder().flicker(random.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(random.nextBoolean()).build();
            fwm.addEffect(effect);
            fwm.setPower(rp);
            fw.setFireworkMeta(fwm);

            if (count > 0)
                launchRandomFireworks(count - 1, location, delay);
        }, delay);
    }

    public static Color getFireworkColor(int i) {
        Color c = null;

        if(i==1){
            c=Color.AQUA;
        }
        if(i==2){
            c=Color.BLACK;
        }
        if(i==3){
            c=Color.BLUE;
        }
        if(i==4){
            c=Color.FUCHSIA;
        }
        if(i==5){
            c=Color.GRAY;
        }
        if(i==6){
            c=Color.GREEN;
        }
        if(i==7){
            c=Color.LIME;
        }
        if(i==8){
            c=Color.MAROON;
        }
        if(i==9){
            c=Color.NAVY;
        }
        if(i==10){
            c=Color.OLIVE;
        }
        if(i==11){
            c=Color.ORANGE;
        }
        if(i==12){
            c=Color.PURPLE;
        }
        if(i==13){
            c=Color.RED;
        }
        if(i==14){
            c=Color.SILVER;
        }
        if(i==15){
            c=Color.TEAL;
        }
        if(i==16){
            c=Color.WHITE;
        }
        if(i==17){
            c=Color.YELLOW;
        }
        return c;
    }
}
