package fr.nayz.deacoudre.messages;

import fr.nayz.api.GameAPI;
import fr.nayz.commons.accounts.Account;
import org.bukkit.entity.Player;

public enum Message {
    JOIN("§8» [RANK_PREFIX][PLAYER_NAME] §7a rejoint la partie de §d§lDé à Coudre §7!"),
    QUIT("§8» [RANK_PREFIX][PLAYER_NAME] §7a quitté la partie de §d§lDé à Coudre §7!"),
    STARTING_STATUS("§8» §7La partie démarre dans §b[TIMER] §7secondes."),
    POINTS_ADD("§8» §7Vous avez remporté §e+[TIMER]pts§7."),
    LOBBY_STATUS("§8» §7En attente de joueur..."),
    PLAYING_STATUS("§8» §7Sauter dans l'eau sans toucher de bloc\n§8» §7Plus vous prenez des risques plus les points que vous gagnez seront imporants\n§8» §7Soyez le dernier survivant"),
    PLAYER_PLAYTIME("§8» §7C'est au tour de [RANK_PREFIX][PLAYER_NAME] §7de tenter le grand saut."),
    PLAYER_DIED("§8» [RANK_PREFIX][PLAYER_NAME] §7est mort !"),
    WINNER_ANNOUNCE("§8» [RANK_PREFIX][PLAYER_NAME] §6est le dernière joueur en vie, il remporte la partie !"),
    NEW_ROUND("§8» §7Nouvelle manche\n§8» §eBonus de points augmenter"),
    SECTION("§7§m----------------------------------------");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(int timer) {
        return message.replace("[TIMER]", String.valueOf(timer));
    }

    public String getMessage(Player player) {
        Account account = GameAPI.getInstance().getAccountManager().getAccount(player);
        return message.replace("[PLAYER_NAME]", player.getName()).replace("[RANK_PREFIX]", account.getRank().getChatPrefix());
    }
}
