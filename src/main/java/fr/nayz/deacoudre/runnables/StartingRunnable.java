package fr.nayz.deacoudre.runnables;

import fr.nayz.deacoudre.managers.GameManager;
import fr.nayz.deacoudre.messages.Message;
import fr.nayz.deacoudre.status.GameStatus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StartingRunnable extends BukkitRunnable {
    private final GameManager gameManager;
    private int timer = 10;

    public StartingRunnable(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        if (!gameManager.isStatus(GameStatus.STARTING)) {
            this.cancel();
            return;
        }

        // Si le nombre de joueur est insuffisant
        if (gameManager.getPlayers().size() < gameManager.getMinPlayers()) {
            gameManager.setStatus(GameStatus.LOBBY);
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setLevel(timer);
        }

        // Annoncer
        if (timer == 1 || timer == 2 || timer == 3 || timer % 5 == 0) {
            Bukkit.broadcast(Component.text(Message.STARTING_STATUS.getMessage(timer)));
        }

        if (timer == 0) {
            gameManager.setStatus(GameStatus.PLAYING);
            return;
        }

        gameManager.setTimer(timer);

        timer--;
    }
}
