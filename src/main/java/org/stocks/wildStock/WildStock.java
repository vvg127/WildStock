package org.stocks.wildStock;

import org.bukkit.plugin.java.JavaPlugin;
import org.stocks.wildStock.Commands.Menu;
import org.stocks.wildStock.Commands.Shop;
import org.stocks.wildStock.Library.Data;
import org.stocks.wildStock.Stock.Change;

public final class WildStock extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Data.load();
        Change.startTask();

        getServer().getPluginManager().registerEvents(new Menu(), this);
        getServer().getPluginManager().registerEvents(new Shop(), this);

        getCommand("stock").setExecutor(new Menu());
        getCommand("shop").setExecutor(new Shop());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Data.save();
    }
}
