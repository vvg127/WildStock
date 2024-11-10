package org.stocks.wildStock.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class Menu implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player p) {
            Inventory inventory = Bukkit.createInventory(p, 54, Component.text("주식",
                    Style.style(TextColor.color(0, 0, 0), TextDecoration.ITALIC.withState(false))));
            p.openInventory(inventory);

            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
        }

        return true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

    }

}
