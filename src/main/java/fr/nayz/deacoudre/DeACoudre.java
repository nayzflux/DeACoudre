package fr.nayz.deacoudre;

import fr.nayz.deacoudre.listeners.CancelListener;
import fr.nayz.deacoudre.listeners.PlayerListener;
import fr.nayz.deacoudre.managers.ConfigManager;
import fr.nayz.deacoudre.managers.GameManager;
import fr.nayz.deacoudre.scoreboards.LobbyBoard;
import fr.nayz.deacoudre.scoreboards.PlayingBoard;
import fr.nayz.deacoudre.scoreboards.StartingBoard;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DeACoudre extends JavaPlugin {
    private static DeACoudre INSTANCE;
    private GameManager gameManager;
    private ConfigManager configManager;

    public static DeACoudre getInstance() {
        return INSTANCE;

    }

    @Override
    public void onEnable() {
        INSTANCE = this;

        gameManager = new GameManager(this);
        configManager = new ConfigManager(this);

        registerEvents();
        registerCommands();

        new LobbyBoard().runTaskTimer(this, 0L, 20L);
        new StartingBoard().runTaskTimer(this, 0L, 20L);
        new PlayingBoard().runTaskTimer(this, 0L, 20L);
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(gameManager), this);
        pm.registerEvents(new CancelListener(gameManager), this);
    }

    private void registerCommands() {

    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
