package fr.nayz.deacoudre.players;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GamePlayer {
    private final Player player;
    private int points;
    private boolean isAlive;
    private int round;
    private boolean hasPlayed;
    private boolean canPlay;


    public GamePlayer(Player player) {
        this.player = player;
        this.points = 0;
        this.isAlive = true;
        this.round = 0;
        this.hasPlayed = false;
        this.canPlay = false;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int amount) {
        points += amount;
        player.sendMessage(Component.text("Vous remporté §e+" + amount + "pts"));
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean hasPlayed() {
        return hasPlayed;
    }

    public void die() {
        if (!isAlive) return;

        isAlive = false;

        player.sendMessage("Vous êtes mort");

        Bukkit.getLogger().info("Dead: " + player.getName());
    }

    public void success() {
        if (hasPlayed) return;

        Bukkit.getLogger().info("Success: " + player.getName());

        round += 1;
        hasPlayed = true;
        canPlay = false;

        Block block1 = player.getLocation().add(new Vector(1, 0, 0)).getBlock();
        Block block2 = player.getLocation().add(new Vector(0, 0, 1)).getBlock();
        Block block3 = player.getLocation().add(new Vector(-1, 0, 0)).getBlock();
        Block block4 = player.getLocation().add(0, 0, -1).getBlock();

        int amount = 1 + round;

        if (block1.getType() != Material.WATER) {
            amount += 1;
        }

        if (block2.getType() != Material.WATER) {
            amount += 1;
        }

        if (block3.getType() != Material.WATER) {
            amount += 1;
        }

        if (block4.getType() != Material.WATER) {
            amount += 1;
        }


        addPoints(amount);

        player.getLocation().getBlock().setType(Material.RED_WOOL);

        player.sendMessage("Vous avez réussi à survivre");
    }

    public void setHasPlayed(boolean hasPlayed) {
        this.hasPlayed = hasPlayed;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    public boolean canPlay() {
        return canPlay;
    }
}
