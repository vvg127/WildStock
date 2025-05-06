package org.stocks.wildStock.Inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.stocks.wildStock.Library.Data;


public class StockMenu implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().title().equals(Component.text("주식", Style.style(TextColor.color(0, 0, 0), TextDecoration.ITALIC.withState(false))))) {
            if (e.getWhoClicked() instanceof Player p) {
                e.setCancelled(true);
                ItemStack item = e.getCurrentItem();

                if (item == null || e.getClickedInventory() == null) {return;}
                if (e.getClickedInventory().equals(e.getView().getBottomInventory())) {return;}

                if (item.getType() == Material.BARRIER) {
                    p.playSound(p, Sound.BLOCK_ANVIL_LAND, 1, 1);

                } else {
                    for (int i = 0; i < 4; i++) {
                        if (item.getType() == Data.stocks[i].getIcon()) {
                            Buy.buy(p, i);
                            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                        }
                    }
                }

            }
        }
    }
}
