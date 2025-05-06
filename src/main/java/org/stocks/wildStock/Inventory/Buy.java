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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.stocks.wildStock.Commands.StockOpen;
import org.stocks.wildStock.Library.Data;
import org.stocks.wildStock.Library.Stock;
import org.stocks.wildStock.WildStock;

import java.util.*;


public class Buy implements Listener {

    private final Map<Player, Inventory> previousIn = new HashMap<>();
    private final Set<Player> chatWait = new HashSet<>();
    private final WildStock plugin;

    public Buy(WildStock plugin) {
        this.plugin = plugin;
    }

    /* TODO isStock 쓸 바에는 차라리 오버로딩해서 하는게 나을 듯
    *   쨋든 뜯어 고쳐아 함
    *   지금은 아이템 상점 고려 없이 진행하기로
    *
    * */
    static void buy(Player p, int index) {
        Inventory inventory = Bukkit.createInventory(p, 54, Component.text("매수 / 매도",
                Style.style(TextColor.color(0, 0, 0), TextDecoration.ITALIC.withState(false))));
        inventory.setItem(4, Icon.getMoneyIcon(p));

        ItemStack item = Icon.getGoods(index);
        inventory.setItem(22, item);

        /*TODO 구매 화면 아이템 정보
        *  PlainTextComponentSerializer.plainText().serialize(component);를 쓰면 component 를 String 으로 바꿔줌!
        *  여기는 처음 한 번만 실행되는 거니 1로 해도 ㄱㅊ! --> Icon 에서 기본값 설정 예정. 일반화 가능
        *  혹시 오류 생기면 인스턴스 변수 amount 만들어서 고치셈
        * */

        Stock stock = Data.stocks[index];
        int price = stock.getPrice();

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

        inventory.setItem(48, Icon.getAskIcon(p, item, false));
        inventory.setItem(50, Icon.getAskIcon(p, item, true));

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
                e.setCancelled(true);

                ItemStack item = e.getCurrentItem();
                Inventory inventory = e.getView().getTopInventory();
                if (item == null) {return;}
                ItemMeta meta = item.getItemMeta();

                switch (e.getSlot()) {
                    case 8: {
                        StockOpen.open(p);
                        break;
                    }
                    case 19, 20, 21, 23, 24, 25: {
                        Component name = meta.displayName();
                        if (name == null) {return;}
                        change(inventory, p, Integer.parseInt(PlainTextComponentSerializer.plainText().serialize(name)));
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
                        //TODO 48이 매수  50이 매도

                        ItemStack target = inventory.getItem(22);
                        if (target == null) {return;}
                        ItemMeta targetMeta = target.getItemMeta();

                        PersistentDataContainer data = targetMeta.getPersistentDataContainer();

                        if (data.get(Data.getKeyAmount(), PersistentDataType.INTEGER) == null) {return;}
                        @SuppressWarnings("DataFlowIssue") int count = data.get(Data.getKeyAmount(), PersistentDataType.INTEGER);

                        if (data.get(Data.getKeyStock(), PersistentDataType.INTEGER) == null) {return;}
                        @SuppressWarnings("DataFlowIssue") int index = data.get(Data.getKeyStock(), PersistentDataType.INTEGER);

                        if (Data.getMoney(p.getUniqueId()) >= count * Data.stocks[index].getPrice()) {
                            Data.addMoney(p.getUniqueId(), count * Data.stocks[index].getPrice() * -1);

                            Data.stocks[index].addAmount(p.getUniqueId(), count);
                            p.sendMessage(Component.text(Data.stocks[index].getName() + " 주식을 " + count +"주 매입했습니다.",
                                    Style.style(TextColor.color(0, 255, 0), TextDecoration.ITALIC.withState(false))));
                            p.playSound(p, Sound.ENTITY_VILLAGER_YES, 1, 1);
                            StockOpen.open(p);

                        } else {
                            p.sendMessage(Component.text("돈이 부족합니다.",
                                    Style.style(TextColor.color(180, 0, 0), TextDecoration.ITALIC.withState(false))));
                            p.playSound(p, Sound.ENTITY_VILLAGER_HURT, 1, 1);
                        }
                        break;

                    }
                    case 50: {
                        ItemStack target = inventory.getItem(22);
                        if (target == null) {return;}
                        ItemMeta targetMeta = target.getItemMeta();

                        PersistentDataContainer data = targetMeta.getPersistentDataContainer();

                        if (data.get(Data.getKeyAmount(), PersistentDataType.INTEGER) == null) {return;}
                        @SuppressWarnings("DataFlowIssue") int count = data.get(Data.getKeyAmount(), PersistentDataType.INTEGER);

                        if (data.get(Data.getKeyStock(), PersistentDataType.INTEGER) == null) {return;}
                        @SuppressWarnings("DataFlowIssue") int index = data.get(Data.getKeyStock(), PersistentDataType.INTEGER);

                        if (Data.stocks[index].getAmount(p.getUniqueId()) >= count) {
                            Data.addMoney(p.getUniqueId(), count * Data.stocks[index].getPrice());

                            Data.stocks[index].addAmount(p.getUniqueId(), count * -1);
                            p.sendMessage(Component.text(Data.stocks[index].getName() + " 주식을 " + count +"주 매도했습니다.",
                                    Style.style(TextColor.color(0, 255, 0), TextDecoration.ITALIC.withState(false))));
                            p.playSound(p, Sound.ENTITY_VILLAGER_YES, 1, 1);
                            StockOpen.open(p);

                        } else {
                            p.sendMessage(Component.text("주식이 부족합니다.",
                                    Style.style(TextColor.color(180, 0, 0), TextDecoration.ITALIC.withState(false))));
                            p.playSound(p, Sound.ENTITY_VILLAGER_HURT, 1, 1);
                        }
                        break;

                    }

                }

            }
        }

    }

    /*TODO 생각해 보니 주식 매도할 때 주식이 그만큼 없으면 lore 에 경고 메시지 떠야함  buy 쪽이랑 아래 있는거 바꿔야 함
    *  수정완 테스트 필요*/
    // TODO 주식용
    private void change(Inventory inventory, Player p, int amount) {
        ItemStack item = inventory.getItem(22);
        if (item == null) {return;}
        ItemMeta meta = item.getItemMeta();

        PersistentDataContainer data = meta.getPersistentDataContainer();

        if (data.get(Data.getKeyAmount(), PersistentDataType.INTEGER) == null) {return;}
        @SuppressWarnings("DataFlowIssue") int count = data.get(Data.getKeyAmount(), PersistentDataType.INTEGER);

        if (count + amount <= 0 && count == 1) {
            p.sendMessage(Component.text("이미 최소치입니다!", Style.style(TextColor.color(180, 0, 0), TextDecoration.ITALIC.withState(false))));
            p.playSound(p, Sound.ENTITY_PILLAGER_HURT, 1, 1);
            return;
        } else if (count + amount <= 0) {
            data.set(Data.getKeyAmount(), PersistentDataType.INTEGER, 1);
        } else {
            data.set(Data.getKeyAmount(), PersistentDataType.INTEGER, count + amount);
        }


        item.setItemMeta(meta);
        Icon.setGoods(item);

        inventory.setItem(48, Icon.getAskIcon(p, item, false));
        inventory.setItem(50, Icon.getAskIcon(p, item, true));

        p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);

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
                    change(inventory, p, amount);
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
