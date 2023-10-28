package fr.nayz.deacoudre.status;

public enum GameStatus {
    LOBBY("§aEn attente"),
    STARTING("§eDémarrage"),
    PLAYING("§dEn jeu"),
    END("§cFin");

    private final String displayName;

    GameStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
