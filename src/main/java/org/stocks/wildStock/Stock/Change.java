package org.stocks.wildStock.Stock;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.stocks.wildStock.Library.Data;
import org.stocks.wildStock.Library.Stock;
import org.stocks.wildStock.WildStock;

public class Change extends BukkitRunnable {

    private static boolean running = false;

    public static boolean isRunning() {return running;}

    @Override
    public void run() {

        if (!running) {
            cancel();
            running = false;
            return;
        }

        for (Stock stock : Data.stocks) {
            if (stock.isClosed()) {
                stock.setClosed(false);
            } else {
                int value = (int) (Math.random() * stock.getChange()) * (((int) (Math.random() * 2) == 1) ? 1 : -1);
                if (stock.getPrice() + value <= 0) {
                    stock.setClosed(true);
                } else {stock.setPrice(stock.getPrice() + value);}
            }
        }

        Server server = WildStock.getPlugin(WildStock.class).getServer();
        server.broadcast(
                Component.text("주가가 변경되었습니다.",
                        Style.style(TextDecoration.ITALIC.withState(false), TextColor.color(0, 255, 0)))
        );

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP,1, 1);
            for (Stock stock : Data.stocks) {
                p.sendMessage(Component.text(stock.isClosed()));
            }
        }

    }

    public static void startTask() {
        if (running) {return;}
        Change task = new Change();
        running = true;
        task.runTaskTimer(JavaPlugin.getPlugin(WildStock.class),/*24*/0 * 20,240/* * 20*/);
        Price.startMode();
    }

    public static void stopTask() {
        if (!running) {return;}
        running = false;
        Price.stopMode();
    }

}
