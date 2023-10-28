package fr.nayz.deacoudre.managers;

import fr.nayz.deacoudre.maps.GameMap;
import fr.nayz.deacoudre.messages.Message;
import fr.nayz.deacoudre.players.GamePlayer;
import fr.nayz.deacoudre.runnables.EndRunnable;
import fr.nayz.deacoudre.runnables.LobbyRunnable;
import fr.nayz.deacoudre.runnables.PlayingRunnable;
import fr.nayz.deacoudre.runnables.StartingRunnable;
import fr.nayz.deacoudre.scoreboards.EndBoard;
import fr.nayz.deacoudre.scoreboards.LobbyBoard;
import fr.nayz.deacoudre.scoreboards.PlayingBoard;
import fr.nayz.deacoudre.scoreboards.StartingBoard;
import fr.nayz.deacoudre.status.GameStatus;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private final Plugin plugin;

    private final GameMap map;
    private final List<GamePlayer> players;
    private final int minPlayers = 2;
    private Location lobby;
    private Location waiting;
    private Location play;
    private GameStatus status;
    private int round;
    private Player winner;
    private int timer;

    public GameManager(Plugin plugin) {
        this.plugin = plugin;

        plugin.getDataFolder().mkdirs();

        File gameMapsFolder = new File(plugin.getDataFolder(), "gameMaps");

        if (!gameMapsFolder.exists()) {
            gameMapsFolder.mkdirs();
        }

        this.map = new GameMap(gameMapsFolder, "map1", true);

        this.lobby = new Location(map.getBukkitWorld(), -2.5, 61, -2.5);
        this.play = new Location(map.getBukkitWorld(), -2.5, 53, -8.5);
        this.waiting = new Location(map.getBukkitWorld(), -3.5, 4, -6.5);

        this.players = new ArrayList<>();

        this.timer = 0;

        setStatus(GameStatus.LOBBY);
    }

    public void startRound() {
        if (!isStatus(GameStatus.PLAYING)) return;

        round++;

        Bukkit.broadcast(Component.text(Message.SECTION.getMessage()));
        Bukkit.broadcast(Component.text(Message.NEW_ROUND.getMessage()));
        Bukkit.broadcast(Component.text(Message.SECTION.getMessage()));

        for (GamePlayer player : players) {
            player.setHasPlayed(false);
        }

        if (getAlivePlayers().isEmpty()) {
            return;
        }

        playNext();
    }

    public void playNext() {
        if (!isStatus(GameStatus.PLAYING)) return;

        GamePlayer gamePlayer = findGamePlayerWhoHasntPlayed();

        if (gamePlayer == null) {
            startRound();
            return;
        }

        Bukkit.getLogger().info("PlayNext: " + gamePlayer.getPlayer().getName());

        play(gamePlayer);
    }

    public void checkWin() {
        if (getAlivePlayers().isEmpty()) {
            setStatus(GameStatus.END);
        }

        if (getAlivePlayers().size() == 1) {
            Player player = getAlivePlayers().get(0);
            winner = player;
            setStatus(GameStatus.END);

            Bukkit.broadcast(Component.text(Message.SECTION.getMessage()));
            Bukkit.broadcast(Component.text(Message.WINNER_ANNOUNCE.getMessage(player)));
            Bukkit.broadcast(Component.text(Message.SECTION.getMessage()));
        }
    }

    public void play(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();

        teleportToPlaying(player);

        ItemStack bucket = new ItemStack(Material.WATER_BUCKET);

        player.getInventory().setItem(0, bucket);

        player.showTitle(Title.title(Component.text("§6A vous de jouer !"), Component.text("§cEviter de toucher le sol")));

        Bukkit.broadcast(Component.text(Message.PLAYER_PLAYTIME.getMessage(player)));

        gamePlayer.setCanPlay(true);
    }

    public void stop() {
        plugin.getServer().setWhitelist(true);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kick(Component.text("Server is restarting"));
        }

        map.restore();
        players.clear();
        setStatus(GameStatus.LOBBY);

        this.round = 0;

        this.lobby = new Location(map.getBukkitWorld(), -2.5, 61, -2.5);
        this.play = new Location(map.getBukkitWorld(), -2.5, 53, -8.5);
        this.waiting = new Location(map.getBukkitWorld(), -3.5, 4, -6.5);

        plugin.getServer().setWhitelist(false);
    }

    public void teleportToLobby(Player player) {
        player.teleport(lobby);
    }

    public void teleportToWaiting(Player player) {
        player.teleport(waiting);
    }

    public void teleportToPlaying(Player player) {
        player.teleport(play);
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;

        switch (status) {
            case LOBBY:
                Bukkit.broadcast(Component.text(Message.LOBBY_STATUS.getMessage()));

                new LobbyRunnable(this).runTaskTimer(plugin, 0L, 20L);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    LobbyBoard.getInstance().createNewScoreboard(player);
                }

                break;
            case STARTING:
                new StartingRunnable(this).runTaskTimer(plugin, 0L, 20L);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    StartingBoard.getInstance().createNewScoreboard(player);
                }

                break;
            case PLAYING:
                Bukkit.broadcast(Component.text(Message.SECTION.getMessage()));
                Bukkit.broadcast(Component.text(Message.PLAYING_STATUS.getMessage()));
                Bukkit.broadcast(Component.text(Message.SECTION.getMessage()));

                for (GamePlayer player : players) {
                    teleportToWaiting(player.getPlayer());
                }

                startRound();

                new PlayingRunnable(this).runTaskTimer(plugin, 0L, 20L);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayingBoard.getInstance().createNewScoreboard(player);
                }
                break;
            case END:
                new EndRunnable(this).runTaskTimer(plugin, 0L, 20L);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    EndBoard.getInstance().createNewScoreboard(player);
                }
                break;
        }
    }

    public boolean isStatus(GameStatus status) {
        return this.status == status;
    }

    public List<Player> getAlivePlayers() {
        return players.stream().filter(p -> p.isAlive()).toList().stream().map(p -> p.getPlayer()).toList();
    }

    public GamePlayer findGamePlayerWhoHasntPlayed() {
        return players.stream().filter(p -> p.isAlive() && !p.hasPlayed()).findFirst().orElse(null);
    }

    public GamePlayer findGamePlayer(Player player) {
        return players.stream().filter(p -> p.getPlayer() == player).findFirst().orElse(null);
    }


    public List<GamePlayer> getPlayers() {
        return players;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getRound() {
        return round;
    }

    public Player getWinner() {
        return winner;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}
