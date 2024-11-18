package org.stocks.wildStock.Library;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

import java.util.*;

public class Stock {

    public Stock(int change, String name, Material icon) {
        this.change = change;
        this.name = name;
        this.icon = icon;
    }

    private final int change;
    private final String name;
    private final Material icon;
    private int price = 1000;
    private boolean xTen = false;
    private boolean closed = false;
    private final Map<UUID, Integer> amounts = new HashMap<>();
    private final List<Integer> logs = new ArrayList<>();

    public int getChange() {return (xTen) ? change * 10 : change;}

    public void setXTen(boolean xTen) {this.xTen =xTen;}

    public boolean isXTen() {return xTen;}

    public String getName() {return name;}

    public Component getComponent() {return Component.text(name, Style.style(TextDecoration.ITALIC.withState(false)));}

    public Material getIcon() {return icon;}

    public int getPrice() {return price;}

    public void setPrice(int price) {
        logs.add(this.price);
        if (logs.size() > 5) {logs.removeFirst();}
        this.price = price;
    }

    public boolean isClosed() {return closed;}

    public void setClosed(boolean closed) {
        this.closed = closed;
        price = 1000;
        if (closed) {
            logs.clear();
            amounts.clear();
        }
    }

    public int getAmount(UUID uuid) {
        if (!amounts.containsKey(uuid)) {
            amounts.put(uuid, 0);
        }
        return amounts.get(uuid);
    }

    public void setAmount(UUID uuid, int amount) {amounts.put(uuid, amount);}

    public void addAmount(UUID uuid, int amount) {amounts.put(uuid, getAmount(uuid) + amount);}

    public List<Component> getLore(UUID uuid) {
        List<Component> lore = new ArrayList<>();

        Component text = Component.text(getPrice(), Style.style(TextColor.color(255, 191, 0), TextDecoration.ITALIC.withState(false)));

        lore.add(Component.text("현재 주가 : ",
                Style.style(TextColor.color(255, 255, 255),TextDecoration.ITALIC.withState(false))).append(text));

        lore.add(Component.text("보유 주식 : " + getAmount(uuid) + " 주", Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));

        lore.add(Component.text("변동폭 : " + getChange() + "%", Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
        lore.add(Component.empty());

        int temp = -1;
        int count = 0;
        List<Component> list = new ArrayList<>();
        for (int num : logs) {
            if (count == 5) {break;}
            if (temp == -1) {
                temp = num;

                if (logs.size() < 5) {
                    list.add(Component.text("—" + num, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
                    count++;
                }

            } else {
                if (num > temp) {
                    list.add(Component.text("▲" + num, Style.style(TextColor.color(255, 0, 0), TextDecoration.ITALIC.withState(false))));
                } else if (num < temp) {
                    list.add(Component.text("▼" + num, Style.style(TextColor.color(0, 0, 255), TextDecoration.ITALIC.withState(false))));
                } else {
                    list.add(Component.text("—" + num, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
                }
                temp = num;
                count++;
            }
        }

        lore.addAll(list.reversed());

        return lore;
    }
}
