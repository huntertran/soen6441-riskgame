package soen6441riskgame.models;

import java.util.ArrayList;

import soen6441riskgame.enums.CardType;
import soen6441riskgame.utils.ConsolePrinter;

/**
 * a set of card that can be trade, according to game rule
 */
public class CardSet {
    private ArrayList<Card> cards = new ArrayList<>();
    private static final int NUMBER_OF_CARD_IN_SET = 3;

    /**
     * init the set with {@value #NUMBER_OF_CARD_IN_SET} cards
     *
     * @param first  1st card
     * @param second 2nd card
     * @param third  3rd card
     */
    public CardSet(Card first, Card second, Card third) {
        cards.add(first);
        cards.add(second);
        cards.add(third);
    }

    /**
     * the value of {@value #NUMBER_OF_CARD_IN_SET} Infantry cards
     *
     * @return the value
     */
    private int allInfantry() {
        return CardType.Infantry.getCardTypeAsInt() * NUMBER_OF_CARD_IN_SET;
    }

    /**
     * the value of {@value #NUMBER_OF_CARD_IN_SET} Cavalry cards
     *
     * @return the value
     */
    private int allCavalry() {
        return CardType.Cavalry.getCardTypeAsInt() * NUMBER_OF_CARD_IN_SET;
    }

    /**
     * the value of {@value #NUMBER_OF_CARD_IN_SET} Artillery cards
     *
     * @return the value
     */
    private int allArtillery() {
        return CardType.Artillery.getCardTypeAsInt() * NUMBER_OF_CARD_IN_SET;
    }

    /**
     * the value of {@value #NUMBER_OF_CARD_IN_SET} Wild cards
     *
     * @return the value
     */
    private int allWild() {
        return CardType.Wild.getCardTypeAsInt() * NUMBER_OF_CARD_IN_SET;
    }

    /**
     * the value of {@value #NUMBER_OF_CARD_IN_SET} cards in 3 types (except Wild)
     *
     * @return the value
     */
    private int oneOfEach() {
        return CardType.Infantry.getCardTypeAsInt()
               + CardType.Cavalry.getCardTypeAsInt()
               + CardType.Artillery.getCardTypeAsInt();
    }

    /**
     * check if the set is a valid set
     *
     * @return is set valid
     */
    public boolean isSetValid() {
        int cardValue = 0;
        for (Card card : cards) {
            if (card == null) {
                ConsolePrinter.printFormat("Some cards in set are invalid (null)");
                return false;
            }

            cardValue += card.getCardType().getCardTypeAsInt();
        }

        return cardValue == allInfantry()
               || cardValue == allCavalry()
               || cardValue == allArtillery()
               || cardValue == allWild()
               || cardValue == oneOfEach()
               || cardValue > CardType.Wild.getCardTypeAsInt();
    }

    /**
     * get the armies that can be trade by the set
     *
     * The first set traded in - 4 armies
     *
     * The second set traded in - 6 armies
     *
     * The third set traded in - 8 armies
     *
     * The fourth set traded in - 10 armies
     *
     * The fifth set traded in - 12 armies
     *
     * The sixth set traded in - 15 armies
     *
     * After the sixth set has been traded in, each additional set is worth 5 more armies. Example: If
     * you trade in the seventh set, you get 20 armies; if you trade in the eighth, you get 25 armies,
     * and so on. “First” and “second” set, etc., refer to sets traded in by anyone during the game.
     * Thus, if you trade in the third set in the game, you receive 8 armies, even if it’s the first set
     * you have traded in.
     *
     * @param tradeTime the time player trading this set
     * @return the number of armies
     */
    public int getTradeInArmies(int tradeTime) {
        int armies = 0;
        if (isSetValid()) {
            if (tradeTime > 6) {
                armies = 15 + (tradeTime - 6) * 5;
            } else {
                switch (tradeTime) {
                    case 1: {
                        return 4;
                    }
                    case 2: {
                        return 6;
                    }
                    case 3: {
                        return 8;
                    }
                    case 4: {
                        return 10;
                    }
                    case 5: {
                        return 12;
                    }
                    case 6: {
                        return 15;
                    }
                    default: {
                        return 0;
                    }
                }
            }
        }

        return armies;
    }

    /**
     * mark all the cards in set were exchanged
     */
    public void setCardsExchanged() {
        for (Card card : cards) {
            card.setExchanged(true);
        }
    }

    public String getCardsIndexForTournament() {
        String result = "";

        if (isSetValid()) {
            Player holdingPlayer = cards.get(0).getHoldingPlayer();

            result += holdingPlayer.getHoldingCards().indexOf(cards.get(0));
            result += " ";
            result += holdingPlayer.getHoldingCards().indexOf(cards.get(1));
            result += " ";
            result += holdingPlayer.getHoldingCards().indexOf(cards.get(2));

        }

        return result;
    }
}
