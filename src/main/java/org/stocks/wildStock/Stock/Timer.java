package org.stocks.wildStock.Stock;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.stocks.wildStock.WildStock;

public class Timer{

    private boolean isOver = true;

    public void timer(int second) {

        isOver = false;

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                isOver = true;
            }
        };

        task.runTaskLaterAsynchronously(JavaPlugin.getPlugin(WildStock.class), second * 20L);

    }
}
