package org.stocks.wildStock.Updater;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.stocks.wildStock.WildStock;

public class Update extends BukkitRunnable {

    private static WildStock plugin;

    @Override
    public void run() {

        for (Player p : plugin.getServer().getOnlinePlayers()) {



        }

    }

    public static void startFirst(WildStock pluginGet) {
        plugin = pluginGet;

    }

    public static void startTask() {}

    public static void stopTask() {}

}