package net.doodcraft.dooder07.bungeecord.doodcore.config;

import net.doodcraft.dooder07.bungeecord.doodcore.BungeeLog;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class BungeeConfig {

    public static Configuration getConfig(File file) {

        try {

            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

        } catch (IOException e) {

            try {

                BungeeLog.log("DoodCore", "Creating " + file.toString());

                Configuration config = new Configuration();
                saveConfig(config, file);

                return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            } catch (IOException e1) {
                BungeeLog.log("DoodCore", "There was an error saving a file - check read/write perms..?");
                e1.printStackTrace();
                return null;
            }
        }
    }

    public static void saveConfig(Configuration config, File file) throws IOException {
        try {

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);

        } catch (IOException e) {

            if (file.mkdirs()) {
                // success
            }

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        }
    }
}
