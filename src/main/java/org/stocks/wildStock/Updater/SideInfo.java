package org.stocks.wildStock.Updater;


import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class SideInfo {

    public static void refresh(Player p) {

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Info", Criteria.DUMMY, Component.text("정보"));

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore("이름: " + p.getName()).setScore(0);

        p.setScoreboard(scoreboard);


    }



}
