package soen6441riskgame.enums;

/**
 * Phase of the game
 */
public enum GamePhase {
                       LOST(0),
                       WAITING_TO_TURN(1),
                       REINFORCEMENT(2),
                       ATTACK(3),
                       FORTIFICATION(4),
                       END_OF_GAME(5);

    private final int phase;

    /**
     * private constructor
     * @param phase the phase
     */
    private GamePhase(int phase) {
        this.phase = phase;
    }

    /**
     * get the number re-present the game phase
     * @return number re-present the game phase
     */
    public int getGamePhaseAsInt() {
        return phase;
    }
}
