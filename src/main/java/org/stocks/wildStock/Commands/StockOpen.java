package org.stocks.wildStock.Commands;

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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.stocks.wildStock.Inventory.Icon;
import org.stocks.wildStock.Library.Data;
import org.stocks.wildStock.Library.Stock;

import java.util.ArrayList;
import java.util.List;

public class StockOpen implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equals("stock")) {
            if (commandSender instanceof Player p) {
                open(p);
            }
        }

        return true;
    }

    public static void open(Player p) {
        Inventory inventory = Bukkit.createInventory(p, 54, Component.text("주식",
                Style.style(TextColor.color(0, 0, 0), TextDecoration.ITALIC.withState(false))));

        inventory.setItem(4, Icon.getMoneyIcon(p));

        int slot = 19;
        for (Stock stock : Data.stocks) {
            ItemStack item = new ItemStack(stock.getIcon());
            ItemMeta meta = item.getItemMeta();

            if (stock.isClosed()) {
                meta.displayName(Component.text("상장 폐지된 주식입니다.", Style.style(TextColor.color(255, 0,0), TextDecoration.ITALIC.withState(false))));
                item = new ItemStack(Material.BARRIER);

                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("상장 폐지된 주식입니다.", Style.style(TextColor.color(255, 0,0), TextDecoration.ITALIC.withState(false))));

                meta.lore(lore);
            } else {
                meta.displayName(stock.getComponent());
                meta.lore(stock.getLore(p.getUniqueId()));
            }

            item.setItemMeta(meta);
            inventory.setItem(slot, item);
            slot += 2;
        }

        p.openInventory(inventory);
        p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
    }

}
