package net.doodcraft.dooder07.bungeecord.doodcore;

import net.doodcraft.dooder07.bungeecord.doodcore.features.ConnectMessages;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

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
public class DoodCorePlugin extends Plugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        BungeeLog.log("Core", "Enabled!");

        registerEvents(plugin, new ConnectMessages());
    }

    public void registerEvents(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            getProxy().getPluginManager().registerListener(plugin, listener);
        }
    }
}
