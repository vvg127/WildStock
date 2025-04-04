package org.stocks.wildStock.Library;

import org.bukkit.Material;

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

}
