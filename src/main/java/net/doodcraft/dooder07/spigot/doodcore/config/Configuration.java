package net.doodcraft.dooder07.spigot.doodcore.config;

import net.doodcraft.dooder07.spigot.doodcore.DoodLog;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

import java.io.File;
import java.io.IOException;
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
public class Configuration {
    private File file = null;
    private YamlConfiguration yaml = new YamlConfiguration();

    public Configuration(File file) {
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    DoodLog.log("DoodCore", "Creating new file " + file.getName());
                }
            } catch (IOException ex) {
                DoodLog.printError("DoodCore", "&cError initializing configuration file.", ex);
            }
        }
        load();
    }

    public Configuration(String path) {

        file = new File(path);

        // TODO: Make this better?
        if (!file.exists() || file == null) {
            try {
                if (file.createNewFile()) {
                    DoodLog.log("DoodCore", "Creating new file " + file.getName());
                }
            } catch (IOException ex) {
                DoodLog.log("DoodCore", "---- If this is your first time running Core, this can be ignored! ----");
                DoodLog.printError("DoodCore", "&cError initializing configuration file path.", ex);
                DoodLog.log("DoodCore", "---- If this is your first time running Core, this can be ignored! ----");
            }
        }
        load();
    }

    private void load() {
        try {
            yaml.load(file);
        } catch (Exception ex) {
            DoodLog.printError("DoodCore", "&cError loading configuration file.", ex);
        }
    }

    public void save() {
        try {
            yaml.save(file);
        } catch (Exception ex) {
            DoodLog.printError("DoodCore", "&cError saving configuration file.", ex);
        }
    }

    public void delete() {
        try {
            if (file.createNewFile()) {
                DoodLog.log("DoodCore", "Deleting file " + file.getName());
            }
        } catch (Exception ex) {
            DoodLog.printError("DoodCore", "&cError deleting configuration file.", ex);
        }
    }

    public int getInteger(String s) {
        return yaml.getInt(s);
    }

    public void reload() {
        save();
        load();
    }

    public String getString(String s) {
        return yaml.getString(s);
    }

    public Object get(String s) {
        return yaml.get(s);
    }

    public boolean getBoolean(String s) {
        return yaml.getBoolean(s);
    }

    public void add(String s, Object o) {
        if (!contains(s)) {
            set(s, o);
        }
    }

    public void addToStringList(String s, String o) {
        yaml.getStringList(s).add(o);
    }

    public void removeFromStringList(String s, String o) {
        yaml.getStringList(s).remove(o);
    }

    public List<String> getStringList(String s) {
        return yaml.getStringList(s);
    }

    public void addToIntegerList(String s, int o) {
        yaml.getIntegerList(s).add(o);
    }

    public void removeFromIntegerList(String s, int o) {
        yaml.getIntegerList(s).remove(o);
    }

    public List<Integer> getIntegerList(String s) {
        return yaml.getIntegerList(s);
    }

    public void createNewStringList(String s, List<String> list) {
        yaml.set(s, list);
    }

    public void createNewIntegerList(String s, List<Integer> list) {
        yaml.set(s, list);
    }

    public void remove(String s) {
        set(s, null);
    }

    public boolean contains(String s) {
        return yaml.contains(s);
    }

    public double getDouble(String s) {
        return yaml.getDouble(s);
    }

    public void set(String s, Object o) {
        yaml.set(s, o);
    }

    public void increment(String s) {
        yaml.set(s, getInteger(s) + 1);
    }

    public void decrement(String s) {
        yaml.set(s, getInteger(s) - 1);
    }

    public void increment(String s, int i) {
        yaml.set(s, getInteger(s) + i);
    }

    public void decrement(String s, int i) {
        yaml.set(s, getInteger(s) - i);
    }

    public YamlConfigurationOptions options() {
        return yaml.options();
    }
}
