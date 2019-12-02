package soen6441riskgame.enums;

/**
 * Type of card that a player can hold
 *
 * <p>
 * The number of each type is used for calculate the validity of a set of 3 cards to trade for
 * armies during reinforcement phase
 */
public enum CardType {
                      Infantry(1),
                      Cavalry(100),
                      Artillery(1000),
                      Wild(10000);

    private final int cardValue;

    /**
     * private constructor
     *
     * @param cardValue value of the type
     */
    CardType(int cardValue) {
        this.cardValue = cardValue;
    }

    /**
     * get card type value
     *
     * @return card value
     */
    public int getCardTypeAsInt() {
        return cardValue;
    }
}
