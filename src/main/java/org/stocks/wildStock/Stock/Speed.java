package org.stocks.wildStock.Stock;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.stocks.wildStock.WildStock;

public class Speed extends BukkitRunnable {

    private static int target;
    private static boolean running;

    @Override
    public void run() {
        if (target == 0 || !running) {
            cancel();
            running = false;
            return;
        }

        //각 클래스에서 오버라이드로 따로 구현

        target--;

    }

    public static void startMode() {
        if (running) {return;}
        Speed task = new Speed();
        running = true;
        task.runTaskTimer(JavaPlugin.getPlugin(WildStock.class),0L,20);
    }

    public static void stopMode() {
        running = false;
    }

}
