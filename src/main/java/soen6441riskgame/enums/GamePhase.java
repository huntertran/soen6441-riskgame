package soen6441riskgame.enums;

/**
 * This Enum initiliazes the Phase of the game
 */
public enum GamePhase {
                       LOST(0),
                       WAITING_TO_TURN(1),
                       REINFORCEMENT(2),
                       ATTACK(3),
                       FORTIFICATION(4),
                       END_OF_GAME(5);

    private final int phase;

    private GamePhase(int phase) {
        this.phase = phase;
    }

    public int getGamePhaseAsInt() {
        return phase;
    }

    public static GamePhase convertIntToGamePhase(int phase) {
        for (GamePhase gamePhase : GamePhase.values()) {
            if (gamePhase.getGamePhaseAsInt() == phase) {
                return gamePhase;
            }
        }

        return null;
    }
}
