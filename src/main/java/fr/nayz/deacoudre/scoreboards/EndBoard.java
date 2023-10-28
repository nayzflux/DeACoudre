package fr.nayz.deacoudre.scoreboards;

import fr.nayz.deacoudre.DeACoudre;
import fr.nayz.deacoudre.status.GameStatus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class EndBoard extends BukkitRunnable {
    private final static EndBoard INSTANCE = new EndBoard();

    public static EndBoard getInstance() {
        return INSTANCE;
    }

    @Override
    public void run() {

    }

    public void createNewScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("scoreboard", Criteria.DUMMY, Component.text("§d§lDé à Coudre"));

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore("§1").setScore(7);
        objective.getScore("§8» §6" + player.getName()).setScore(6);
        objective.getScore("§2").setScore(5);

        objective.getScore("§8» §7Points: §e" + DeACoudre.getInstance().getGameManager().findGamePlayer(player).getPoints()).setScore(4);

        objective.getScore("§3").setScore(3);

        objective.getScore("§8» §7Gagnant: §b" + DeACoudre.getInstance().getGameManager().getWinner().getName()).setScore(2);
        objective.getScore("§8» §7Status: §cFin").setScore(2);

        objective.getScore("§4").setScore(1);
        objective.getScore("§emc.nayz.fr").setScore(0);

        player.setScoreboard(scoreboard);
    }
}
