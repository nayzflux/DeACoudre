package fr.nayz.deacoudre.scoreboards;

import fr.nayz.deacoudre.DeACoudre;
import fr.nayz.deacoudre.status.GameStatus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class LobbyBoard extends BukkitRunnable {
    private final static LobbyBoard INSTANCE = new LobbyBoard();

    public static LobbyBoard getInstance() {
        return INSTANCE;
    }

    @Override
    public void run() {
        if (!DeACoudre.getInstance().getGameManager().isStatus(GameStatus.LOBBY)) {
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

        objective.getScore("§1").setScore(6);
        objective.getScore("§8» §6" + player.getName()).setScore(5);
        objective.getScore("§2").setScore(4);

        Team playerCount = scoreboard.registerNewTeam("player-count");
        playerCount.addEntry("§8» §7Joueurs: ");
        playerCount.suffix(Component.text("§d" + DeACoudre.getInstance().getGameManager().getPlayers().size()));
        objective.getScore("§8» §7Joueurs: ").setScore(3);

        objective.getScore("§8» §7Status: §aLobby").setScore(2);

        objective.getScore("§3").setScore(1);
        objective.getScore("§emc.nayz.fr").setScore(0);

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Team playerCount = scoreboard.getTeam("player-count");
        playerCount.suffix(Component.text("§d" + DeACoudre.getInstance().getGameManager().getPlayers().size()));
    }
}
