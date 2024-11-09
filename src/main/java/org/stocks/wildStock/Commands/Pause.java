package org.stocks.wildStock.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.stocks.wildStock.Stock.Change;

public class Pause implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (Change.isRunning()) {
            Change.stopTask();
            commandSender.sendMessage(
                    Component.text("주식 변동이 정지되었습니다!"
                            , Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
        } else {
            Change.startTask();
            commandSender.sendMessage(
                    Component.text("주식 변동이 재시작되었습니다!"
                            , Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC.withState(false))));
        }

        if (commandSender instanceof Player) {
            ((Player) commandSender).playSound(
                    (Player) commandSender, Sound.BLOCK_NOTE_BLOCK_BELL, 1,1);
        }

        return true;
    }
}
