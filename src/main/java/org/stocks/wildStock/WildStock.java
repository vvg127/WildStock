package org.stocks.wildStock;

import org.bukkit.plugin.java.JavaPlugin;
import org.stocks.wildStock.Commands.ShopOpen;
import org.stocks.wildStock.Commands.StockOpen;
import org.stocks.wildStock.Items.Convert;
import org.stocks.wildStock.Inventory.Buy;
import org.stocks.wildStock.Inventory.Money;
import org.stocks.wildStock.Inventory.StockMenu;
import org.stocks.wildStock.Commands.Pause;
import org.stocks.wildStock.Inventory.Shop;
import org.stocks.wildStock.Library.Data;
import org.stocks.wildStock.Updater.Change;

public final class WildStock extends JavaPlugin {
    private static WildStock instance;

    @Override
    public void onEnable() {
        instance = this;

        Data.load();
        Change change = new Change();
        change.toggleTask();

        getServer().getPluginManager().registerEvents(new StockMenu(), this);
        getServer().getPluginManager().registerEvents(new Shop(), this);
        getServer().getPluginManager().registerEvents(new Buy(this), this);
        getServer().getPluginManager().registerEvents(new Convert(), this);
        getServer().getPluginManager().registerEvents(new Money(), this);

        getCommand("stock").setExecutor(new StockOpen());
        getCommand("shop").setExecutor(new ShopOpen());
        getCommand("pause").setExecutor(new Pause(change));

    }

    @Override
    public void onDisable() {
        Data.save();
    }

    public static WildStock getInstance() {
        return instance;
    }
}
