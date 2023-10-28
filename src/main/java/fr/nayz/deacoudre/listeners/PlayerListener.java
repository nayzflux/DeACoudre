package fr.nayz.deacoudre.listeners;

import fr.nayz.deacoudre.managers.GameManager;
import fr.nayz.deacoudre.players.GamePlayer;
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

        event.joinMessage(Component.text(player.getName() + " a rejoint la partie"));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        GamePlayer gamePlayer = gameManager.findGamePlayer(player);
        gameManager.getPlayers().remove(gamePlayer);

        if (gameManager.isStatus(GameStatus.PLAYING)) {
            gameManager.checkWin();
        }

        event.quitMessage(Component.text(player.getName() + " a quitté la partie"));
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
