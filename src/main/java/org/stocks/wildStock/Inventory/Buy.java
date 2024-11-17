package org.stocks.wildStock.Inventory;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.stocks.wildStock.Library.Data;
import org.stocks.wildStock.WildStock;

import java.util.*;

public class Buy implements Listener {

    private final Map<Player, Inventory> previousIn = new HashMap<>();
    private final Set<Player> chatWait = new HashSet<>();
    private final WildStock plugin;

    public Buy(WildStock plugin) {
        this.plugin = plugin;
    }

    static void buy(Player p, ItemStack item, int price, boolean isStock) {
        String nameIn;
        if (isStock) {
            nameIn = "매수 / 매도";
        } else {nameIn = "구매";}

        Inventory inventory = Bukkit.createInventory(p, 54, Component.text(nameIn,
                Style.style(TextColor.color(0, 0, 0), TextDecoration.ITALIC.withState(false))));

        ItemStack money = new ItemStack(Material.SUNFLOWER);
        ItemMeta moneyMeta = money.getItemMeta();
        ArrayList<Component> list = new ArrayList<>();

        list.add(Component.text(Data.getMoney(p.getUniqueId()) + " 원", Style.style(TextColor.color(255, 191, 0), TextDecoration.ITALIC.withState(false))));
        moneyMeta.lore(list);
        list.clear();
        moneyMeta.displayName(Component.text("보유 자금", Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
        money.setItemMeta(moneyMeta);
        inventory.setItem(4, money);

        /*TODO 구매 화면 아이템 정보
        *  PlainTextComponentSerializer.plainText().serialize(component);를 쓰면 component 를 String 으로 바꿔줌!
        *  여기는 처음 한 번만 실행되는 거니 1로 해도 ㄱㅊ!
        *  혹시 오류 생기면 인스턴스 변수 amount 만들어서 고치셈
        * */
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("가격 : " + price, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));

        if (isStock) {
            lore.add(Component.text("거래량 : " + 1, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
        } else {
            lore.add(Component.text("구매량 : " + 1, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
        }

        lore.add(Component.text("총 가격 : " + price, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));

        if (isStock) {
            lore.add(Component.empty());
            lore.add(Component.text("※ 경고 매수/매도 화면에서는",
                    Style.style(TextColor.color(255, 0, 0), TextDecoration.ITALIC.withState(false), TextDecoration.BOLD.withState(true))));
            lore.add(Component.text("도중에 변경된 주가가 반영되지 않습니다. ※",
                    Style.style(TextColor.color(255, 0, 0), TextDecoration.ITALIC.withState(false), TextDecoration.BOLD.withState(true))));
        }

        meta.lore(lore);
        item.setItemMeta(meta);
        inventory.setItem(22, item);

        for (int i = 1; i <= 3; i++) {
            ItemStack glass = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta glassM = glass.getItemMeta();

            switch (i) {
                case 1:
                    glassM.displayName(Component.text("-1", Style.style(TextColor.color(255, 0, 0), TextDecoration.ITALIC.withState(false))));
                    break;
                case 2:
                    glassM.displayName(Component.text("-8", Style.style(TextColor.color(255, 0, 0), TextDecoration.ITALIC.withState(false))));
                    break;
                case 3:
                    glassM.displayName(Component.text("-32", Style.style(TextColor.color(255, 0, 0), TextDecoration.ITALIC.withState(false))));
                    break;
            }

            glass.setItemMeta(glassM);
            inventory.setItem(22 - i, glass);
        }

        for (int i = 1; i <= 3; i++) {
            ItemStack glass = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta glassM = glass.getItemMeta();

            switch (i) {
                case 1:
                    glassM.displayName(Component.text("+1", Style.style(TextColor.color(0, 255, 0), TextDecoration.ITALIC.withState(false))));
                    break;
                case 2:
                    glassM.displayName(Component.text("+8", Style.style(TextColor.color(0, 255, 0), TextDecoration.ITALIC.withState(false))));
                    break;
                case 3:
                    glassM.displayName(Component.text("+32", Style.style(TextColor.color(0, 255, 0), TextDecoration.ITALIC.withState(false))));
                    break;
            }

            glass.setItemMeta(glassM);
            inventory.setItem(22 + i, glass);
        }

        ItemStack custom = new ItemStack(Material.OAK_SIGN);
        ItemMeta customM = custom.getItemMeta();

        customM.displayName(Component.text("직접 입력", Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
        custom.setItemMeta(customM);

        inventory.setItem(31, custom);

        ItemStack buy = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta buyM = buy.getItemMeta();
        List<Component> loreI = new ArrayList<>();
        if (isStock) {
            loreI.add(Component.text("매수량 : 1", Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            loreI.add(Component.text("총 가격 : " + price, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            loreI.add(Component.empty());

            int left = Data.getMoney(p.getUniqueId()) - price;
            if (left >= 0) {
                loreI.add(Component.text("매수 후 남는 돈 : " + left, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            } else {
                loreI.add(Component.text("돈이 부족 합니다.", Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            }

            buyM.lore(loreI);
            buyM.displayName(Component.text("매수하기", Style.style(TextColor.color(0, 180, 0), TextDecoration.ITALIC.withState(false))));
            buy.setItemMeta(buyM);

            inventory.setItem(48, buy);

            ItemStack sell = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta sellM = sell.getItemMeta();
            loreI.clear();
            loreI.add(Component.text("매도량 : 1", Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            loreI.add(Component.text("총 가격 : " + price, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            loreI.add(Component.empty());

            loreI.add(Component.text("매수 후 남는 돈 : " + (price + Data.getMoney(p.getUniqueId())), Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));

            sellM.lore(loreI);
            sellM.displayName(Component.text("매도하기", Style.style(TextColor.color(180, 0, 0), TextDecoration.ITALIC.withState(false))));
            sell.setItemMeta(sellM);
            
            inventory.setItem(50, sell);

        } else {

            loreI.add(Component.text("구매량 : 1", Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            loreI.add(Component.text("총 가격 : " + price, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            loreI.add(Component.empty());

            int left = Data.getMoney(p.getUniqueId()) - price;
            if (left >= 0) {
                loreI.add(Component.text("구매 후 남는 돈 : " + left, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            } else {
                loreI.add(Component.text("돈이 부족 합니다.", Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            }

            buyM.lore(loreI);
            buyM.displayName(Component.text("구매하기", Style.style(TextColor.color(0, 180, 0), TextDecoration.ITALIC.withState(false))));
            buy.setItemMeta(buyM);

            inventory.setItem(49, buy);
        }

        ItemStack back = new ItemStack(Material.STRUCTURE_VOID);
        ItemMeta metaB = back.getItemMeta();
        metaB.displayName(Component.text("이전 화면으로", Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
        back.setItemMeta(metaB);
        inventory.setItem(8, back);

        p.openInventory(inventory);
    }


    //TODO 구매 화면 재오픈 불가능  클릭 시 변할 거 다 변하게 해야 함 (예시 : 구매 수량 추가 -> 최종 결산도 변화)
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().title().equals(Component.text("매수 / 매도", Style.style(TextColor.color(0, 0, 0), TextDecoration.ITALIC.withState(false))))) {
            if (e.getWhoClicked() instanceof Player p) {
                //TODO 주식인 경우
                e.setCancelled(true);

                ItemStack item = e.getCurrentItem();
                Inventory inventory = e.getView().getTopInventory();
                if (item == null) {return;}
                ItemMeta meta = item.getItemMeta();

                switch (e.getSlot()) {
                    case 4: {
                        p.sendMessage(Component.text("현재 소지 중인 돈 : " + Data.getMoney(p.getUniqueId()),
                                Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
                        p.playSound(p, Sound.BLOCK_CHAIN_PLACE, 1, 1);
                        break;

                    }
                    case 8: {
                        StockMenu.open(p);
                        break;
                    }
                    case 19, 20, 21, 23, 24, 25: {
                        Component name = meta.displayName();
                        if (name == null) {return;}
                        change(inventory, p, Integer.parseInt(PlainTextComponentSerializer.plainText().serialize(name)), true);
                        p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                        break;

                    }
                    case 31: {
                        previousIn.put(p, inventory);
                        chatWait.add(p);
                        p.closeInventory();
                        p.sendMessage(Component.text("채팅창에 원하시는 숫자를 입력해 주세요.",
                                Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
                        p.playSound(p, Sound.ITEM_BOOK_PAGE_TURN, 1.2f, 1);
                        break;

                    }
                    case 48: {

                    }
                    case 50: {

                    }

                }

            }
        } else if (e.getView().title().equals(Component.text("구매", Style.style(TextColor.color(0, 0, 0), TextDecoration.ITALIC.withState(false))))) {
            if (e.getWhoClicked() instanceof Player p) {
                //TODO 주식이 아닌 경우
                e.setCancelled(true);

                ItemStack item = e.getCurrentItem();
                if (item == null || e.getClickedInventory() == null) {return;}
                if (e.getClickedInventory().equals(e.getView().getBottomInventory())) {return;}
                ItemMeta meta = item.getItemMeta();


            }
        }

    }

    //TODO 생각해 보니 주식 매도할 때 주식이 그만큼 없으면 lore 에 경고 메시지 떠야함  buy 쪽이랑 아래 있는거 바꿔야 함
    private void change(Inventory inventory, Player p, int amount, boolean isStock) {
        ItemStack middle = inventory.getItem(22);
        if (middle == null) {return;}
        ItemMeta mMeta = middle.getItemMeta();
        List<Component> mLore = mMeta.lore();
        if (mLore == null) {return;}

        String text = PlainTextComponentSerializer.plainText().serialize(mLore.get(1));
        int count = Math.max(Integer.parseInt(text.substring(6)) + amount, 1);

        String text2 = PlainTextComponentSerializer.plainText().serialize(mLore.getFirst());
        int price = Integer.parseInt(text2.substring(5));

        String text3 = PlainTextComponentSerializer.plainText().serialize(mLore.get(2));

        mLore.set(1, Component.text(text.substring(0, 6) + count, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
        mLore.set(2, Component.text(text3.substring(0, 7) + (count * price), Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));

        mMeta.lore(mLore);
        middle.setItemMeta(mMeta);

        if (isStock) {
            ItemStack buy = inventory.getItem(48);
            if (buy == null) {return;}
            ItemMeta bMeta = buy.getItemMeta();
            List<Component> bLore = bMeta.lore();
            if (bLore == null) {return;}

            bLore.set(0, Component.text(text.substring(0, 6) + count, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            bLore.set(1, mLore.get(2));

            int left = Data.getMoney(p.getUniqueId()) - (price * count);
            if (left < 0) {
                bLore.set(3, Component.text("돈이 부족 합니다.", Style.style(TextColor.color(180, 0, 0), TextDecoration.ITALIC.withState(false))));
            } else {
                bLore.set(3, Component.text("매수 후 남는 돈 : " + left, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            }

            bMeta.lore(bLore);
            buy.setItemMeta(bMeta);

            ItemStack sell = inventory.getItem(50);
            if (sell == null) {return;}
            ItemMeta sMeta = sell.getItemMeta();
            List<Component> sLore = sMeta.lore();
            if (sLore == null) {return;}

            sLore.set(0, Component.text(text.substring(0, 6) + count, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            sLore.set(1, mLore.get(2));
            sLore.set(3, Component.text("매도 후 남는 돈 : " + (Data.getMoney(p.getUniqueId()) + (price * count)), Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));

            sMeta.lore(sLore);
            sell.setItemMeta(sMeta);

        } else {
            ItemStack buy = inventory.getItem(49);
            if (buy == null) {return;}
            ItemMeta bMeta = buy.getItemMeta();
            List<Component> bLore = bMeta.lore();
            if (bLore == null) {return;}

            bLore.set(0, Component.text(text.substring(0, 6) + count, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            bLore.set(1, mLore.get(2));

            int left = Data.getMoney(p.getUniqueId()) - (price * count);
            if (left < 0) {
                bLore.set(3, Component.text("돈이 부족 합니다.", Style.style(TextColor.color(180, 0, 0), TextDecoration.ITALIC.withState(false))));
            } else {
                bLore.set(3, Component.text("구매 후 남는 돈 : " + left, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
            }

            bMeta.lore(bLore);
            buy.setItemMeta(bMeta);
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        if (chatWait.contains(event.getPlayer())) {
            event.setCancelled(true);

            Player p = event.getPlayer();
            String chat = PlainTextComponentSerializer.plainText().serialize(event.message());

            try {
                int amount = Integer.parseInt(chat);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    Inventory inventory = previousIn.get(p);
                    ItemStack item = inventory.getItem(22);
                    if (item == null) {return;}
                    ItemMeta meta = item.getItemMeta();
                    List<Component> lore = meta.lore();
                    if (lore == null) {return;}

                    String text = PlainTextComponentSerializer.plainText().serialize(lore.get(1)).substring(0, 6) + 0;
                    lore.set(1, Component.text(text, Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
                    meta.lore(lore);
                    item.setItemMeta(meta);

                    p.openInventory(inventory);
                    change(inventory, p, amount, lore.size() == 6);
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BIT, 1.2f, 1);

                });



            } catch (NumberFormatException e) {
                p.sendMessage(Component.text("숫자가 올바르지 않습니다.", Style.style(TextColor.color(180, 0, 0), TextDecoration.ITALIC.withState(false))));
                p.playSound(p, Sound.BLOCK_ANVIL_LAND, 1, 1);
                previousIn.remove(p);
                chatWait.remove(p);
            }

        }
    }

}
