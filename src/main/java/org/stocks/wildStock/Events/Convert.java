package org.stocks.wildStock.Events;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.stocks.wildStock.Library.Data;

public class Convert implements Listener {

    private int getPrice(Material item) {

        if (item == Material.IRON_INGOT) {
            return 500;
        } else if (item == Material.GOLD_INGOT) {
            return 300;
        } else if (item == Material.DIAMOND) {
            return 2000;
        } else if (item == Material.EMERALD) {
            return 200;
        }

        return 0;
    }

    @EventHandler
    public void ItemGet(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player p) {
            ItemStack item = e.getItem().getItemStack();
            if (item.getItemMeta().hasEnchants()) {return;}

            if (getPrice(item.getType()) != 0) {
                Data.addMoney(p.getUniqueId(), getPrice(item.getType()) * item.getAmount());
                p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
                p.sendMessage(Component.text("계좌에 " + getPrice(item.getType()) * item.getAmount() + "원이 입금되었습니다!"));
                e.setCancelled(true);
                e.getItem().remove();
            }
        }
    }

    @EventHandler
    public void ItemGetOther(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p) {
            Inventory in = e.getClickedInventory();
            if (in == null) {return;}
            if (in.equals(e.getView().getTopInventory())) {return;}

            ItemStack item = e.getCurrentItem();
            if (item == null || item.getItemMeta() == null || item.getItemMeta().hasEnchants()) {return;}

            if (e.getSlotType() == InventoryType.SlotType.RESULT) {return;}

            if (getPrice(item.getType()) != 0) {
                Data.addMoney(p.getUniqueId(), getPrice(item.getType()) * item.getAmount());
                p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
                p.sendMessage(Component.text("계좌에 " + getPrice(item.getType()) * item.getAmount() + "원이 입금되었습니다!"));
                in.remove(item.getType());
                e.setCancelled(true);
            }
        }
    }

}
