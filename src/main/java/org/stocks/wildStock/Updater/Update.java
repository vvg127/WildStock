package org.stocks.wildStock.Updater;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.stocks.wildStock.WildStock;

public class Update extends BukkitRunnable {



    @Override
    public void run() {

        for (Player p : WildStock.getInstance().getServer().getOnlinePlayers()) {



        }

    }

    public static void startTask() {}

    public static void stopTask() {}

}