package fr.nayz.deacoudre.listeners;

import fr.nayz.deacoudre.managers.GameManager;
import fr.nayz.deacoudre.messages.Message;
import fr.nayz.deacoudre.players.GamePlayer;
import fr.nayz.deacoudre.scoreboards.EndBoard;
import fr.nayz.deacoudre.scoreboards.LobbyBoard;
import fr.nayz.deacoudre.scoreboards.PlayingBoard;
import fr.nayz.deacoudre.scoreboards.StartingBoard;
import fr.nayz.deacoudre.status.GameStatus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final GameManager gameManager;

    public PlayerListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        gameManager.getPlayers().add(new GamePlayer(player));
        gameManager.teleportToLobby(player);

        if (gameManager.getPlayers().size() >= gameManager.getMinPlayers()) {
            if (!gameManager.isStatus(GameStatus.LOBBY)) return;
            gameManager.setStatus(GameStatus.STARTING);
        }

        // Scoreboard
        switch (gameManager.getStatus()) {
            case LOBBY:
                LobbyBoard.getInstance().createNewScoreboard(player);
                break;
            case STARTING:
                StartingBoard.getInstance().createNewScoreboard(player);
                break;
            case PLAYING:
                PlayingBoard.getInstance().createNewScoreboard(player);
                break;
            case END:
                EndBoard.getInstance().createNewScoreboard(player);
                break;
        }

        event.joinMessage(Component.text(Message.JOIN.getMessage(player)));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        GamePlayer gamePlayer = gameManager.findGamePlayer(player);
        gameManager.getPlayers().remove(gamePlayer);

        if (gameManager.isStatus(GameStatus.PLAYING)) {
            gameManager.checkWin();
        }

        event.quitMessage(Component.text(Message.QUIT.getMessage(player)));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!gameManager.isStatus(GameStatus.PLAYING)) {
            return;
        }

        GamePlayer gamePlayer = gameManager.findGamePlayer(player);

        if (!gamePlayer.canPlay()) return;

        if (!gamePlayer.isAlive()) return;

        if (gamePlayer.hasPlayed()) return;

        if (player.getLocation().getBlock().getType() == Material.WATER) {
            gamePlayer.success();
            gameManager.teleportToWaiting(player);
            gameManager.playNext();
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!gameManager.isStatus(GameStatus.PLAYING)) {
            return;
        }

        GamePlayer gamePlayer = gameManager.findGamePlayer(player);

        if (!gamePlayer.isAlive()) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getDamage() >= player.getHealth()) {
            gamePlayer.die();
            gameManager.teleportToWaiting(player);
            gameManager.checkWin();
            gameManager.playNext();
        }
    }
}
