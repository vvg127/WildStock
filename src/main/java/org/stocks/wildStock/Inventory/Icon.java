package org.stocks.wildStock.Inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.stocks.wildStock.Library.Data;

import java.util.ArrayList;
import java.util.List;

public class Icon {

    static ItemStack getMoneyIcon(Player p) {
        ItemStack money = new ItemStack(Material.SUNFLOWER);
        ItemMeta meta = money.getItemMeta();
        ArrayList<Component> list = new ArrayList<>();

        list.add(Component.text(Data.getMoney(p.getUniqueId()) + " 원", Style.style(TextColor.color(255, 191, 0), TextDecoration.ITALIC.withState(false))));
        meta.lore(list);
        list.clear();
        meta.displayName(Component.text("보유 자금", Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
        money.setItemMeta(meta);

        return money;
    }




    /* TODO 임시 주석 처리
        오버로딩 예정

        주식 전용 (미완)
        ItemStack getGoods(int index) {}

        아이템 전용 (미완)
        ItemStack getGoods() {}

        권한 전용 (미완)
        ItemStack getGoods() {}

    */

}
