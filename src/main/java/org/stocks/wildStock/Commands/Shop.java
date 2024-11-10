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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.stocks.wildStock.Library.Data;

import java.util.ArrayList;

public class Shop implements CommandExecutor, Listener {
    /*TODO 상점
    * 상점 - 세부 상점 순으로 진입
    * 초고속 모드 (임시) , 고배율 모드 (임시) 모두 지속 시간 기본 3분
    * 추가 구매 시 지속 시간 연장
    * */

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player p) {
            Inventory inventory = Bukkit.createInventory(p, 54, Component.text("상점",
                    Style.style(TextColor.color(0, 0, 0), TextDecoration.ITALIC.withState(false))));
            p.openInventory(inventory);

            ItemStack money = new ItemStack(Material.SUNFLOWER);
            ItemMeta meta = money.getItemMeta();
            ArrayList<Component> list = new ArrayList<>();

            list.add(Component.text(Data.getMoney(p.getUniqueId()) + " 원", Style.style(TextColor.color(255, 191, 0), TextDecoration.ITALIC.withState(false))));
            meta.lore(list);
            list.clear();
            meta.displayName(Component.text("보유 자금", Style.style(TextColor.color(0, 0, 0), TextDecoration.ITALIC.withState(false))));
            money.setItemMeta(meta);
            inventory.setItem(5, money);

            //TODO meta랑 list는 재활용


            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
        }

        return true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

    }

}
