package org.stocks.wildStock.Inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.stocks.wildStock.Library.Data;
import org.stocks.wildStock.Library.Stock;

import java.util.ArrayList;
import java.util.List;



public class StockMenu implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equals("stock")) {
            if (commandSender instanceof Player p) {
                open(p);
            }
        }

        return true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().title().equals(Component.text("주식", Style.style(TextColor.color(0, 0, 0), TextDecoration.ITALIC.withState(false))))) {
            if (e.getWhoClicked() instanceof Player p) {
                e.setCancelled(true);
                ItemStack item = e.getCurrentItem();

                if (item == null || e.getClickedInventory() == null) {return;}
                if (e.getClickedInventory().equals(e.getView().getBottomInventory())) {return;}

                if (item.getType() == Material.SUNFLOWER) {
                    p.sendMessage(Component.text("현재 소지 중인 돈 : " + Data.getMoney(p.getUniqueId()),
                            Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
                    p.playSound(p, Sound.BLOCK_CHAIN_PLACE, 1, 1);

                } else if (item.getType() == Data.stocks[0].getIcon()) {
                    Buy.buy(p, item, Data.stocks[0].getPrice(), true);
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);

                } else if (item.getType() == Data.stocks[1].getIcon()) {
                    Buy.buy(p, item, Data.stocks[1].getPrice(), true);
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);

                } else if (item.getType() == Data.stocks[2].getIcon()) {
                    Buy.buy(p, item, Data.stocks[2].getPrice(), true);
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);

                } else if (item.getType() == Data.stocks[3].getIcon()) {
                    Buy.buy(p, item, Data.stocks[3].getPrice(), true);
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);

                } else if (item.getType() == Material.BARRIER) {
                    p.playSound(p, Sound.BLOCK_ANVIL_LAND, 1, 1);
                }

            }
        }
    }

    static void open(Player p) {
        Inventory inventory = Bukkit.createInventory(p, 54, Component.text("주식",
                Style.style(TextColor.color(0, 0, 0), TextDecoration.ITALIC.withState(false))));

        ItemStack money = new ItemStack(Material.SUNFLOWER);
        ItemMeta meta = money.getItemMeta();
        ArrayList<Component> list = new ArrayList<>();

        list.add(Component.text(Data.getMoney(p.getUniqueId()) + " 원", Style.style(TextColor.color(255, 191, 0), TextDecoration.ITALIC.withState(false))));
        meta.lore(list);
        list.clear();
        meta.displayName(Component.text("보유 자금", Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
        money.setItemMeta(meta);
        inventory.setItem(4, money);

        int slot = 19;
        for (Stock stock : Data.stocks) {
            ItemStack item = new ItemStack(stock.getIcon());
            ItemMeta metaS = item.getItemMeta();

            if (stock.isClosed()) {
                metaS.displayName(Component.text("상장 폐지된 주식입니다.", Style.style(TextColor.color(255, 0,0), TextDecoration.ITALIC.withState(false))));
                item = new ItemStack(Material.BARRIER);

                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("상장 폐지된 주식입니다.", Style.style(TextColor.color(255, 0,0), TextDecoration.ITALIC.withState(false))));

                metaS.lore(lore);
            } else {
                metaS.displayName(stock.getComponent());
                metaS.lore(stock.getLore(p.getUniqueId()));
            }

            item.setItemMeta(metaS);
            inventory.setItem(slot, item);
            slot += 2;
        }

        p.openInventory(inventory);
        p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
    }

}
