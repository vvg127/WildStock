package org.stocks.wildStock;

import org.bukkit.plugin.java.JavaPlugin;
import org.stocks.wildStock.Library.Data;
import org.stocks.wildStock.Stock.Change;

public final class WildStock extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Data.load();
        Change.startTask();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Data.save();
    }
}
