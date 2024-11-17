package org.stocks.wildStock.Stock;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.stocks.wildStock.Library.Data;
import org.stocks.wildStock.Library.Stock;
import org.stocks.wildStock.WildStock;

public class Price extends BukkitRunnable {

    private static int target;
    private static boolean running;

    @Override
    public void run() {
        if (target <= 0 || !running) {
            for (Stock stock : Data.stocks) {
                stock.setXTen(false);
            }
            cancel();
            running = false;
            return;
        }

        target--;

    }

    public static void startMode() {
        if (running || target <= 0) {return;}
        Price task = new Price();
        running = true;
        task.runTaskTimer(JavaPlugin.getPlugin(WildStock.class),20L,20);
        for (Stock stock : Data.stocks) {
            stock.setXTen(true);
        }
    }

    public static void stopMode() {
        if (!running) {return;}
        running = false;
        for (Stock stock : Data.stocks) {
            stock.setXTen(false);
        }
    }

    /** amount 1당 1초 */
    public static void addTarget(int amount) {
        target += amount;
        if (target < 0) {target = 0;}
    }

}
