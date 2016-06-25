package net.doodcore.dooder07.spigot.doodcore.sql;

import net.doodcore.dooder07.spigot.doodcore.DoodCorePlugin;
import net.doodcore.dooder07.spigot.doodcore.DoodLog;
import net.doodcore.dooder07.spigot.doodcore.config.Settings;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * The MIT License (MIT)
 * -
 * Copyright (c) {YEAR} Conor O'Shields
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
