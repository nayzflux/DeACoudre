package fr.nayz.deacoudre.runnables;

import fr.nayz.deacoudre.managers.GameManager;
import fr.nayz.deacoudre.status.GameStatus;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayingRunnable extends BukkitRunnable {
    private final GameManager gameManager;
    private int timer = 0;

    public PlayingRunnable(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        if (!gameManager.isStatus(GameStatus.PLAYING)) {
            this.cancel();
            return;
        }

        gameManager.setTimer(timer);

        timer++;
    }
}
