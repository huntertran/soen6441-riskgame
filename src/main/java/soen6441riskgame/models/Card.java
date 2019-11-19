package soen6441riskgame.models;

import java.io.PrintStream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;

import soen6441riskgame.enums.CardType;
import soen6441riskgame.models.serializers.NameOnlyJsonAdapter;
import soen6441riskgame.utils.ConsolePrinter;

/**
 * A card that can be exchanged for armies
 */
public class Card implements Viewable {
    @Expose
    private CardType cardType;

    @Expose
    private boolean isExchanged = false;

    @JsonAdapter(NameOnlyJsonAdapter.class)
    @Expose
    private Player holdingPlayer = null;

    /**
     * constructor
     *
     * @param type type of card
     */
    public Card(CardType type) {
        cardType = type;
    }

    /**
     * get the type of card
     *
     * @return card type
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * this value here only valid if the card is still in player hand. Once the card is return to deck,
     * is exchange should be set to false
     *
     * @return is card exchanged for armies
     */
    public boolean isExchanged() {
        return isExchanged;
    }

    /**
     * set isExchanged value. When player exchange the card, this value should be set to true. When
     * player return the card to deck, this value should be set to false
     *
     * @param isExchanged the value to set
     */
    public void setExchanged(boolean isExchanged) {
        this.isExchanged = isExchanged;
    }

    /**
     * get the player that holding this card
     *
     * @return player object
     */
    public Player getHoldingPlayer() {
        return holdingPlayer;
    }

    /**
     * hand over this card to the player
     *
     * @param player player who took this card
     */
    public void setHoldingPlayer(Player player) {
        holdingPlayer = player;
    }

    /**
     * print the card content
     */
    @Override
    public void view(PrintStream printStream, int indent) {
        this.printIndent(printStream, indent);
        ConsolePrinter.printFormat(printStream,
                                   "Card type: %s",
                                   cardType.toString());
    }
}
