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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.stocks.wildStock.Inventory.Icon;

public class ShopOpen implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (command.getName().equals("shop")) {
            if (commandSender instanceof Player p) {
                open(p);
            }
        }

        return true;
    }

    public static void open(Player p) {
        Inventory inventory = Bukkit.createInventory(p, 36, Component.text("상점",
                Style.style(TextColor.color(0, 0, 0), TextDecoration.ITALIC.withState(false))));

        inventory.setItem(4, Icon.getMoneyIcon(p));

        ItemStack item = new ItemStack(Material.RAW_IRON);
        {
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("자원 상점",Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));

            item.setItemMeta(meta);
        }

        ItemStack event = new ItemStack(Material.OMINOUS_BOTTLE);
        {
            ItemMeta meta = event.getItemMeta();
            meta.displayName(Component.text("이벤트 상점",Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);

            event.setItemMeta(meta);
        }

        ItemStack permission = new ItemStack(Material.COMMAND_BLOCK);
        {
            ItemMeta meta = permission.getItemMeta();
            meta.displayName(Component.text("권한 상점",Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));

            permission.setItemMeta(meta);
        }

        ItemStack special = new ItemStack(Material.NETHER_STAR);
        {
            ItemMeta meta = special.getItemMeta();
            meta.displayName(Component.text("특수 아이템 상점",Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));

            special.setItemMeta(meta);
        }

        ItemStack hidden = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        {
            ItemMeta meta = hidden.getItemMeta();
            meta.displayName(Component.text("HIDDEN",Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            meta.setHideTooltip(true);

            hidden.setItemMeta(meta);
        }

        inventory.setItem(19, item);
        inventory.setItem(21, event);
        inventory.setItem(23, permission);
        inventory.setItem(25, special);
        inventory.setItem(38, hidden);

        p.openInventory(inventory);
        p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
    }

    public static void openLower(Player p, ItemStack item) {
        Inventory inventory = Bukkit.createInventory(p, 54, Component.text("상점",
                Style.style(TextColor.color(0, 0, 0), TextDecoration.ITALIC.withState(false))));

        inventory.setItem(4, item);





    }
}
