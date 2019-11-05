package soen6441riskgame.models;

import java.util.ArrayList;

import soen6441riskgame.enums.CardType;
import soen6441riskgame.utils.ConsolePrinter;

public class CardSet {
    private ArrayList<Card> cards = new ArrayList<>();

    public CardSet(Card first, Card second, Card third) {
        cards.add(first);
        cards.add(second);
        cards.add(third);
    }

    public int allInfantry() {
        return CardType.Infantry.getCardTypeAsInt() * 3;
    }

    public int allCavalry() {
        return CardType.Cavalry.getCardTypeAsInt() * 3;
    }

    public int allArtillery() {
        return CardType.Artillery.getCardTypeAsInt() * 3;
    }

    public int allWild() {
        return CardType.Wild.getCardTypeAsInt() * 3;
    }

    public int oneOfEach() {
        return CardType.Infantry.getCardTypeAsInt()
               + CardType.Cavalry.getCardTypeAsInt()
               + CardType.Artillery.getCardTypeAsInt();
    }

    public boolean isSetValid() {
        int cardValue = 0;
        for (Card card : cards) {
            if (card == null) {
                ConsolePrinter.printFormat("Some cards in set are invalid (null): %d", cards.indexOf(card));
            }
            cardValue += card.getCardType().getCardTypeAsInt();
        }

        if (cardValue == allInfantry()
            || cardValue == allCavalry()
            || cardValue == allArtillery()
            || cardValue == allWild()
            || cardValue == oneOfEach()
            || cardValue > CardType.Wild.getCardTypeAsInt()) {
            return true;
        } else {
            return false;
        }
    }

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

    public void setCardsExchanged() {
        for (Card card : cards) {
            card.setExchanged(true);
        }
    }
}