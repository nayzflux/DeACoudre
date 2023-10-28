package fr.nayz.deacoudre.maps;

import fr.nayz.deacoudre.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;

public class GameMap {
    private final File sourceWorldFolder;
    private File activeWorldFolder;
    private World bukkitWorld;

    public GameMap(File worldFolder, String worldName, boolean loadOnInit) {
        this.sourceWorldFolder = new File(worldFolder, worldName);

        if (loadOnInit) load();
    }

    public void load() {
        this.activeWorldFolder = new File(
                Bukkit.getWorldContainer().getParent(),
                sourceWorldFolder.getName() + "_active_" + System.currentTimeMillis()
        );

        try {
            FileUtils.copy(sourceWorldFolder, activeWorldFolder);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to load GameMap from source folder " + sourceWorldFolder);
            e.printStackTrace();
        }

        Bukkit.getLogger().info("[Map] Map copied");

        this.bukkitWorld = Bukkit.createWorld(new WorldCreator(activeWorldFolder.getName()));

        Bukkit.getLogger().info("[Map] Map created");

        if (bukkitWorld != null) this.bukkitWorld.setAutoSave(false);

        Bukkit.getLogger().info("[Map] Map loaded");
    }

    public void unload() {
        if (bukkitWorld != null) Bukkit.unloadWorld(bukkitWorld, false);
        Bukkit.getLogger().info("[Map] Map unloaded");

        if (activeWorldFolder != null) FileUtils.delete(activeWorldFolder);
        Bukkit.getLogger().info("[Map] Map deleted");

        bukkitWorld = null;
        activeWorldFolder = null;
    }

    public void restore() {
        unload();
        load();
    }

    public World getBukkitWorld() {
        return bukkitWorld;
    }
}
