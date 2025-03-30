package org.stocks.wildStock.Inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.stocks.wildStock.Library.Data;

public class Money implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p) {
            ItemStack item = e.getCurrentItem();

            if (item != null && item.equals(Icon.getMoneyIcon(p))) {
                e.setCancelled(true);
                p.sendMessage(Component.text("현재 통장 잔고 : " + Data.getMoney(p.getUniqueId()),
                        Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
                p.playSound(p, Sound.BLOCK_CHAIN_PLACE, 1, 1);
            }
        }
    }

}
