package fr.nayz.deacoudre.runnables;

import fr.nayz.deacoudre.managers.GameManager;
import fr.nayz.deacoudre.status.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class EndRunnable extends BukkitRunnable {
    private final GameManager gameManager;
    private int timer = 10;

    public EndRunnable(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        if (!gameManager.isStatus(GameStatus.END)) {
            this.cancel();
            return;
        }

        // Si le serveur est vide
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            gameManager.stop();
        }

        if (timer == 0) {
            gameManager.stop();
            this.cancel();
            return;
        }

        gameManager.setTimer(timer);

        timer--;
    }
}
