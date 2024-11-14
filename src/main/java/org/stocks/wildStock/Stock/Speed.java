package org.stocks.wildStock.Stock;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.stocks.wildStock.Library.Data;
import org.stocks.wildStock.Library.Stock;
import org.stocks.wildStock.WildStock;

public class Speed extends BukkitRunnable {

    private static int target;
    private static boolean running;

    public static boolean isRunning() {return running;}
    public static boolean hasTarget() {return target != 0;}

    @Override
    public void run() {
        if (target <= 0 || !running) {
            cancel();
            running = false;
            return;
        }

        for (Stock stock : Data.stocks) {
            if (stock.isClosed()) {
                stock.setClosed(false);
            } else {
                int value = (int) (Math.random() * stock.getChange()) * (((int) (Math.random() * 2) == 1) ? 1 : -1);
                stock.setPrice(Math.max(stock.getPrice() - value, 0));
            }
        }

        target--;

    }

    public static void startMode() {
        if (running) {return;}
        Speed task = new Speed();
        running = true;
        task.runTaskTimerAsynchronously(JavaPlugin.getPlugin(WildStock.class),2L,2);
        Price.startMode();
    }

    public static void stopMode() {
        if (!running) {return;}
        running = false;
        Price.stopMode();
    }

    /** amount 1당 10회 (1초) */
    public static void addTarget(int amount) {
        target += amount * 10;
        if (target < 0) {target = 0;}
    }

}
