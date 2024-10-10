package org.stocks.wildStock.Library;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public Map<UUID, Integer> amounts = new HashMap<>();

    public int getChange() {return change;}

    public String getName() {return name;}

    public Component getComponent() {return Component.text(name, Style.style(TextDecoration.ITALIC.withState(false)));}

    public Material getIcon() {return icon;}

    public int getPrice() {return price;}

    public void setPrice(int price) {this.price = price;}

    public void resetPrice() {price = 1000;}

    public int getAmount(UUID uuid) {
        if (!amounts.containsKey(uuid)) {
            throw new RuntimeException();
        }
        return amounts.get(uuid);
    }

    public void setAmount(UUID uuid, int amount) {amounts.put(uuid, amount);}
}
