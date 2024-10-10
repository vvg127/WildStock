package org.stocks.wildStock.Library;

import org.bukkit.Material;

import java.util.Map;
import java.util.UUID;

public class Data {

    private static Map<UUID, Integer> money;// 보유 자금

    public static Stock[] stocks = {
            new Stock(50, "안정적인 주식", Material.IRON_INGOT),
            new Stock(75, "살짝 위험한 주식", Material.GOLD_INGOT),
            new Stock(100, "운 나쁘면 폐지되는 주식", Material.DIAMOND),
            new Stock(150, "폐지 확률 높은 주식", Material.EMERALD)
    };

    public static int getMoney(UUID uuid) {
        if (!money.containsKey(uuid)) {
            throw new RuntimeException();
        }
        return money.get(uuid);
    }

    public static void setMoney(UUID uuid, int amount) {money.put(uuid, amount);}

}
