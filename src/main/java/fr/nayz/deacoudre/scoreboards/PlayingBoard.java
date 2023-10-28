package fr.nayz.deacoudre.scoreboards;

import fr.nayz.deacoudre.DeACoudre;
import fr.nayz.deacoudre.players.GamePlayer;
import fr.nayz.deacoudre.status.GameStatus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class PlayingBoard extends BukkitRunnable {
    private final static PlayingBoard INSTANCE = new PlayingBoard();

    public static PlayingBoard getInstance() {
        return INSTANCE;
    }

    @Override
    public void run() {
        if (!DeACoudre.getInstance().getGameManager().isStatus(GameStatus.PLAYING)) {
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

        objective.getScore("§1").setScore(10);
        objective.getScore("§8» §6" + player.getName()).setScore(9);

        objective.getScore("§4").setScore(8);

        Team pts = scoreboard.registerNewTeam("pts");
        pts.addEntry("§8» §7Points: ");
        pts.suffix(Component.text("§e" + 0));
        objective.getScore("§8» §7Points: ").setScore(7);

        objective.getScore("§2").setScore(6);

        Team rounds = scoreboard.registerNewTeam("rounds");
        rounds.addEntry("§8» §7Manches: ");
        rounds.suffix(Component.text("§d" + DeACoudre.getInstance().getGameManager().getRound()));
        objective.getScore("§8» §7Manches: ").setScore(5);

        Team alives = scoreboard.registerNewTeam("alives");
        alives.addEntry("§8» §7En vie: ");
        alives.suffix(Component.text("§d" + DeACoudre.getInstance().getGameManager().getAlivePlayers().size()));
        objective.getScore("§8» §7En vie: ").setScore(4);

        Team timer = scoreboard.registerNewTeam("timer");
        timer.addEntry("§8» §7Temps: ");
        timer.suffix(Component.text("§d" + DeACoudre.getInstance().getGameManager().getTimer() + "s"));
        objective.getScore("§8» §7Temps: ").setScore(3);

        objective.getScore("§8» §7Status: §aEn jeu").setScore(2);

        objective.getScore("§3").setScore(1);
        objective.getScore("§emc.nayz.fr").setScore(0);

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();

        GamePlayer gamePlayer = DeACoudre.getInstance().getGameManager().findGamePlayer(player);

        if (gamePlayer == null) return;

        Team rounds = scoreboard.getTeam("rounds");
        rounds.suffix(Component.text("§d" + DeACoudre.getInstance().getGameManager().getRound()));

        Team pts = scoreboard.getTeam("pts");
        pts.suffix(Component.text("§e" + gamePlayer.getPoints()));

        Team alives = scoreboard.getTeam("alives");
        alives.suffix(Component.text("§d" + DeACoudre.getInstance().getGameManager().getAlivePlayers().size()));

        Team timer = scoreboard.getTeam("timer");
        timer.suffix(Component.text("§d" + DeACoudre.getInstance().getGameManager().getTimer() + "s"));
    }
}
