package net.doodcraft.dooder07.spigot.doodcore.sql;

import net.doodcraft.dooder07.spigot.doodcore.DoodCorePlugin;
import net.doodcraft.dooder07.spigot.doodcore.DoodLog;
import net.doodcraft.dooder07.spigot.doodcore.config.Settings;

import java.sql.ResultSet;
import java.sql.Statement;

public class SqlHandler {
    public static String query(String key, String keyValue, String field) {
        try {
            Statement statement = DoodCorePlugin.connection.createStatement();

            ResultSet set = statement.executeQuery("SELECT * FROM " + Settings.mySQLDatabase + " WHERE " + key + " = '" + keyValue + "';");
            set.next();

            if (set.getString(key) == null) {
                return null;
            } else {
                return set.getString(field);
            }
        } catch (Exception ex) {
            DoodLog.log("DoodCore", "Unhandled SQL exception in SqlHandler.query()");
            return null;
        }
    }
}
