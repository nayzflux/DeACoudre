package fr.nayz.deacoudre.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.ObjectInputFilter;

public class ConfigManager {
    private final Plugin plugin;
    private final FileConfiguration config;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;

        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }


}
