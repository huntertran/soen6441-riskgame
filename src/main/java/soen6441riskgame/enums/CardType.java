package soen6441riskgame.enums;

/**
 * Type of card that a player can hold
 */
public enum CardType {
                      Infantry(1),
                      Cavalry(100),
                      Artillery(1000),
                      Wild(10000);

    private final int armies;

    private CardType(int armies) {
        this.armies = armies;
    }

    public int getCardTypeAsInt() {
        return armies;
    }

    public static CardType convertIntToCardType(int armies) {
        for (CardType cardType : CardType.values()) {
            if (cardType.getCardTypeAsInt() == armies) {
                return cardType;
            }
        }

        return null;
    }
}