package org.stocks.wildStock.Library;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.stocks.wildStock.WildStock;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Data {

    private static final Map<UUID, Integer> money = new HashMap<>();// 보유 자금

    public static final Stock[] stocks = {
            new Stock(50, "1번 주식", Material.IRON_INGOT),
            new Stock(75, "2번 주식", Material.GOLD_INGOT),
            new Stock(100, "3번 주식", Material.DIAMOND),
            new Stock(150, "4번 주식", Material.EMERALD)
    };

    public static int getMoney(UUID uuid) {
        if (!money.containsKey(uuid)) {
            money.put(uuid, 0);
        }
        return money.get(uuid);
    }

    public static void setMoney(UUID uuid, int amount) {money.put(uuid, amount);}

    public static void addMoney(UUID uuid, int amount) {
        setMoney(uuid, getMoney(uuid) + amount);
    }

    //TODO 세이브, 로드 메서드 미구현
    public static void save() {}
    public static void load() {}

    private static final NamespacedKey keyAmount = new NamespacedKey(WildStock.getInstance(), "amount");
    private static final NamespacedKey keyType = new NamespacedKey(WildStock.getInstance(), "goods_type");
    private static final NamespacedKey keyStock = new NamespacedKey(WildStock.getInstance(), "stock_index");

    private static final NamespacedKey keyShop = new NamespacedKey(WildStock.getInstance(), "shop_type");
    private static final NamespacedKey keyAbleToSell = new NamespacedKey(WildStock.getInstance(), "able_to_sell");

    public static NamespacedKey getKeyAmount() {return keyAmount;}
    public static NamespacedKey getKeyStock() {return keyStock;}
    public static NamespacedKey getKeyType() {return keyType;}



}
