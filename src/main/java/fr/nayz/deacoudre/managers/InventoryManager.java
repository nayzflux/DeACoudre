package fr.nayz.deacoudre.managers;

import fr.nayz.api.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {
    public InventoryManager() {

    }

    public void giveLobbyInventory(Player player) {
        ItemStack profile = new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(player.getUniqueId()).setName("§8» §6§lProfile §7(Clique-droit)").toItem();
        ItemStack shop = new ItemBuilder(Material.GOLD_INGOT).setName("§8» §d§lBoutique §7(Clique-droit)").toItem();

        Inventory inv = player.getInventory();
        inv.setItem(0, profile);
        inv.setItem(4, shop);
    }

    public void giveLobbyInventory() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            giveLobbyInventory(player);
        }
    }

    public void clearInventory(Player player) {
        player.getInventory().clear();
        player.getActivePotionEffects().clear();
        player.getEquipment().clear();
    }

    public void clearInventory() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            clearInventory(player);
        }
    }
}
