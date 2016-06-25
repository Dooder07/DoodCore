package net.doodcraft.dooder07.spigot.doodcore.compat;

import net.doodcraft.dooder07.spigot.doodcore.DoodLog;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

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
public class Compatibility {

    public static HashMap<String, Plugin> hooked;

    public static void checkHooks() {
        hooked = new HashMap<>();

        if (hookPlugin("Vault", "1.5.0", "1.5.9")) {
            Vault.setupChat();
            Vault.setupEconomy();
            Vault.setupPermissions();
        }

        hookPlugin("ProtocolLib", "4.0.2", "4.0.2");
        hookPlugin("Essentials", "2.0.1", "2.0.1");
    }

    public static boolean hookPlugin(String name, String min, String max) {
        Plugin hook = Bukkit.getPluginManager().getPlugin(name);

        if (hook != null) {
            String rawVersion = hook.getDescription().getVersion();

            String[] versionPart = rawVersion.split("\\-");

            String version = versionPart[0];

            if (checkVersion(version, min, max)) {

                if (!hooked.containsKey(name)) {
                    hooked.put(name, hook);
                }

                DoodLog.log("DoodCore", "&aPlugin '&b" + name + "&a' found and hooked!");

                return true;
            } else {

                try {
                    if (!hooked.containsKey(name)) {
                        hooked.put(name, hook);
                    }

                    DoodLog.log("DoodCore", "&aPlugin '&b" + name + "&a' found and hooked!");

                    return true;
                } catch (Exception ex) {

                    DoodLog.printError("DoodCore", "&cError hooking '" + name + "'. Version &e" + rawVersion + " &cis incompatible.", ex);

                    DoodLog.log("DoodCore", "&cTry using '" + name + "' versions &e" + min + " &cthrough &e" + max + " &cor report this to Dooder07.");

                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public static boolean checkVersion(String version, String min, String max) {
        return compareVersions(version, min) >= 0 && compareVersions(version, max) <= 0;
    }

    public static Integer compareVersions(String str1, String str2) {
        String[] vals1 = str1.split("\\.");

        String[] vals2 = str2.split("\\.");

        int i = 0;

        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }

        if (i < vals1.length && i < vals2.length) {

            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));

            return Integer.signum(diff);
        } else {

            return Integer.signum(vals1.length - vals2.length);
        }
    }
}
