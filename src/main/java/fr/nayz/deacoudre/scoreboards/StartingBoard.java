package fr.nayz.deacoudre.scoreboards;

import fr.nayz.deacoudre.DeACoudre;
import fr.nayz.deacoudre.status.GameStatus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class StartingBoard extends BukkitRunnable {
    private final static StartingBoard INSTANCE = new StartingBoard();

    public static StartingBoard getInstance() {
        return INSTANCE;
    }

    @Override
    public void run() {
        if (!DeACoudre.getInstance().getGameManager().isStatus(GameStatus.STARTING)) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            updateScoreboard(player);
        }
    }

    public void createNewScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("scoreboard", Criteria.DUMMY, Component.text("§d§lDé à Coudre"));

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore("§1").setScore(7);
        objective.getScore("§8» §6" + player.getName()).setScore(6);
        objective.getScore("§2").setScore(5);

        Team playerCount = scoreboard.registerNewTeam("player-count");
        playerCount.addEntry("§8» §7Joueurs: ");
        playerCount.suffix(Component.text("§d" + DeACoudre.getInstance().getGameManager().getPlayers().size()));
        objective.getScore("§8» §7Joueurs: ").setScore(4);

        Team timer = scoreboard.registerNewTeam("timer");
        timer.addEntry("§8» §7Temps: ");
        timer.suffix(Component.text("§d" + DeACoudre.getInstance().getGameManager().getTimer() + "s"));
        objective.getScore("§8» §7Temps: ").setScore(3);

        objective.getScore("§8» §7Status: §eDémarrage").setScore(2);

        objective.getScore("§3").setScore(1);
        objective.getScore("§emc.nayz.fr").setScore(0);

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();

        Team playerCount = scoreboard.getTeam("player-count");
        playerCount.suffix(Component.text("§d" + DeACoudre.getInstance().getGameManager().getPlayers().size()));

        Team timer = scoreboard.getTeam("timer");
        timer.suffix(Component.text("§d" + DeACoudre.getInstance().getGameManager().getTimer() + "s"));
    }
}
