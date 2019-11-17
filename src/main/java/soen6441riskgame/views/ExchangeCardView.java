package soen6441riskgame.views;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import soen6441riskgame.enums.ChangedProperty;
import soen6441riskgame.models.Card;
import soen6441riskgame.models.Player;
import soen6441riskgame.utils.ConsolePrinter;

/**
 * Implementation of a “card exchange view” using the Observer pattern. The card exchange view
 * should be created only during the reinforcement phase. It should display all the cards owned by
 * the current player, then allow the player to select some cards to exchange. If the player selects
 * cards, they are given the appropriate number of armies as reinforcement. The player can choose
 * not to exchange cards and exit the card exchange view. If the player own 5 cards or more, they
 * must exchange cards. The cards exchange view should cease to exist after the cards exchange.
 */
public class ExchangeCardView extends SeparatedView implements Observer {

    public ExchangeCardView() {
        super("Exchange Card");
    }

    /**
     * update the view when player's card changed (add/remove)
     */
    @Override
    public void update(Observable o, Object arg) {
        clearView();

        ChangedProperty property = (ChangedProperty) arg;

        if (property != ChangedProperty.CARD) {
            return;
        }

        Player player = (Player) o;
        ArrayList<Card> cards = player.getHoldingCards();

        for (Card card : cards) {
            ConsolePrinter.printFormat(printStream,
                                       false,
                                       "Position: %d | ",
                                       cards.indexOf(card) + 1);
            card.view(printStream, 1);
        }
    }
}