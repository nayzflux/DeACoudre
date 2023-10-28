package fr.nayz.deacoudre;

import fr.nayz.deacoudre.listeners.CancelListener;
import fr.nayz.deacoudre.listeners.PlayerListener;
import fr.nayz.deacoudre.managers.ConfigManager;
import fr.nayz.deacoudre.managers.GameManager;
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
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(gameManager), this);
        pm.registerEvents(new CancelListener(gameManager), this);
    }

    private void registerCommands() {

    }
}
