package org.stocks.wildStock.Inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.stocks.wildStock.Library.Data;
import org.stocks.wildStock.Library.Stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Icon {

    public static ItemStack getMoneyIcon(Player p) {
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

    /** 주식 아이템 (기본값) */
    public static ItemStack getGoods(int index) {
        Stock stock = Data.stocks[index];
        ItemStack item = new ItemStack(stock.getIcon());
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();

        lore.add(Component.text("주 당 가격 : " + stock.getPrice(), Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
        lore.add(Component.text("거래량 : " + 1, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
        lore.add(Component.text("총 가격 : " + stock.getPrice(), Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));

        lore.add(Component.empty());
        lore.add(Component.text("※ 경고 매수/매도 화면에서는",
                Style.style(TextColor.color(255, 0, 0), TextDecoration.ITALIC.withState(false), TextDecoration.BOLD.withState(true))));
        lore.add(Component.text("도중에 변경된 주가가 반영되지 않습니다. ※",
                Style.style(TextColor.color(255, 0, 0), TextDecoration.ITALIC.withState(false), TextDecoration.BOLD.withState(true))));

        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(Data.getKeyAmount(), PersistentDataType.INTEGER, 1);
        data.set(Data.getKeyType(), PersistentDataType.STRING, "stock");
        data.set(Data.getKeyStock(), PersistentDataType.INTEGER, index);

        meta.lore(lore);
        item.setItemMeta(meta);

        return item;
    }

    /** 아이콘 수정 시 사용 (공통) */
    public static void setGoods(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = meta.lore();
        if (lore == null) {return;}

        PersistentDataContainer data = meta.getPersistentDataContainer();

        if (data.get(Data.getKeyAmount(), PersistentDataType.INTEGER) == null) {return;}
        @SuppressWarnings("DataFlowIssue") int amount = data.get(Data.getKeyAmount(), PersistentDataType.INTEGER);

        if (Objects.equals(data.get(Data.getKeyType(), PersistentDataType.STRING), "stock")) {
            if (data.get(Data.getKeyStock(), PersistentDataType.INTEGER) == null) {return;}
            @SuppressWarnings("DataFlowIssue") Stock stock = Data.stocks[data.get(Data.getKeyStock(), PersistentDataType.INTEGER)];

            lore.set(0, Component.text("주 당 가격 : " + stock.getPrice(), Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            lore.set(1, Component.text("거래량 : " + amount, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            lore.set(2, Component.text("총 가격 : " + (stock.getPrice() * amount), Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));

        } //TODO else if 문 추가 예정

        meta.lore(lore);
        item.setItemMeta(meta);

    }

    /** 구매/판매 아이콘 sell == false 일 경우 구매  아이템은 구매/판매의 대상으로 */
    public static ItemStack getAskIcon(Player p, ItemStack item, boolean isSell) {
        ItemMeta meta = item.getItemMeta();

        PersistentDataContainer data = meta.getPersistentDataContainer();

        if (data.get(Data.getKeyAmount(), PersistentDataType.INTEGER) == null) {return null;}
        @SuppressWarnings("DataFlowIssue") int amount = data.get(Data.getKeyAmount(), PersistentDataType.INTEGER);

        if (Objects.equals(data.get(Data.getKeyType(), PersistentDataType.STRING), "stock")) {
            if (data.get(Data.getKeyStock(), PersistentDataType.INTEGER) == null) {return null;}
            @SuppressWarnings("DataFlowIssue") Stock stock = Data.stocks[data.get(Data.getKeyStock(), PersistentDataType.INTEGER)];

            int money = Data.getMoney(p.getUniqueId());

            if (isSell) {

                ItemStack sell = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta sellMeta = sell.getItemMeta();
                List<Component> lore = new ArrayList<>();

                lore.add(Component.text("매도량 : " + amount, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
                lore.add(Component.text("총 가격 : " + (stock.getPrice() * amount), Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
                lore.add(Component.empty());

                if (stock.getAmount(p.getUniqueId()) >= 1) {
                    lore.add(Component.text("매도 시 받는 돈 : " + (stock.getPrice() * amount), Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
                } else {
                    lore.add(Component.text("보유 주식이 부족 합니다.", Style.style(TextColor.color(180, 0, 0), TextDecoration.ITALIC.withState(false))));
                }

                sellMeta.lore(lore);
                sellMeta.displayName(Component.text("매도하기", Style.style(TextColor.color(180, 0, 0), TextDecoration.ITALIC.withState(false))));

                sell.setItemMeta(sellMeta);

                return sell;

            } else {

                ItemStack buy = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                ItemMeta buyMeta = buy.getItemMeta();
                List<Component> lore = new ArrayList<>();

                lore.add(Component.text("매수량 : " + amount, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
                lore.add(Component.text("총 가격 : " + (stock.getPrice() * amount), Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
                lore.add(Component.empty());

                if (money - stock.getPrice() >= 0) {
                    lore.add(Component.text("매수 후 남는 돈 : " + (Data.getMoney(p.getUniqueId()) - (stock.getPrice() * amount)), Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
                } else {
                    lore.add(Component.text("돈이 부족 합니다.", Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
                }

                buyMeta.lore(lore);
                buyMeta.displayName(Component.text("매수하기", Style.style(TextColor.color(0, 180, 0), TextDecoration.ITALIC.withState(false))));

                buy.setItemMeta(buyMeta);

                return buy;

            }



        } //TODO else if 문 추가 예정


        return null;
    }



    /* TODO 임시 주석 처리
        오버로딩 예정

        아이템 전용 (미완)
        ItemStack getGoods() {}

        권한 전용 (미완)
        ItemStack getGoods() {}

        이벤트 전용 (미완)
        ItemStack getGoods() {}

    */

}
