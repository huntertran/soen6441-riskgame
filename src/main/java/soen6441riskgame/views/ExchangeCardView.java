package soen6441riskgame.views;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import soen6441riskgame.enums.ChangedProperty;
import soen6441riskgame.models.Card;
import soen6441riskgame.models.Player;

public class ExchangeCardView implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        ChangedProperty property = (ChangedProperty) arg;

        if (property != ChangedProperty.CARD) {
            return;
        }

        Player player = (Player) o;
        ArrayList<Card> cards = player.getHoldingCards();

        for (Card card : cards) {
            System.out.format("Position: %d | ", cards.indexOf(card) + 1);
            card.view(1);
        }

    }
}