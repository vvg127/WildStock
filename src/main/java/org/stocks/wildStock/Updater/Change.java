package org.stocks.wildStock.Updater;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.stocks.wildStock.Library.Data;
import org.stocks.wildStock.Library.Stock;
import org.stocks.wildStock.WildStock;

public class Change{

    private final Plugin plugin;
    private BukkitRunnable task;

    public Change(Plugin plugin) {
        this.plugin = plugin;
    }

    public void toggleTask() throws IllegalStateException{

        if (task == null) {
            task = new BukkitRunnable() {
                @Override
                public void run() {

                    for (Stock stock : Data.stocks) {
                        if (stock.isClosed()) {
                            stock.setClosed(false);
                        } else {
                            int value = (int) (Math.random() * ((float) stock.getPrice() / 100 * stock.getChange())) * (((int) (Math.random() * 2) == 1) ? 1 : -1);
                            if (stock.getPrice() + value <= 10) {
                                stock.setClosed(true);
                            } else {stock.setPrice(stock.getPrice() + value);}
                        }
                    }

                    Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(WildStock.class), () -> {
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
                        }}
                    );

                }
            };

            task.runTaskTimerAsynchronously(plugin, /*24*/0 * 20,240/* * 20*/);
            Price.startMode();

        } else {
            task.cancel();
            task = null;
            throw new IllegalStateException();
        }

    }

}
