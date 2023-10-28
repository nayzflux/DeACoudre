package fr.nayz.deacoudre.runnables;

import fr.nayz.deacoudre.managers.GameManager;
import fr.nayz.deacoudre.status.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyRunnable extends BukkitRunnable {
    private final GameManager gameManager;
    private int timer = 0;

    public LobbyRunnable(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        if (gameManager.isStatus(GameStatus.LOBBY)) {
            this.cancel();
            return;
        }

        // Si le nombre de joueurs est suffisant
        if (gameManager.getPlayers().size() >= gameManager.getMinPlayers()) {
            gameManager.setStatus(GameStatus.STARTING);
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setLevel(timer);
        }

        timer++;
    }
}
