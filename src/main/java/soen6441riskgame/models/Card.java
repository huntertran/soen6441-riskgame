package soen6441riskgame.models;

import soen6441riskgame.enums.CardType;
import soen6441riskgame.utils.ConsolePrinter;

/**
 * A card that can be exchanged for armies
 */
public class Card implements Viewable {
    private CardType cardType;
    private boolean isExchanged = false;
    private Player holdingPlayer = null;

    public Card(CardType type) {
        cardType = type;
    }

    public CardType getCardType() {
        return cardType;
    }

    public boolean isExchanged() {
        return isExchanged;
    }

    public void setExchanged(boolean isExchanged) {
        this.isExchanged = isExchanged;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public Player getHoldingPlayer(){
        return holdingPlayer;
    }

    public void setHoldingPlayer(Player player) {
        holdingPlayer = player;
    }

    @Override
    public void view(int indent) {
        this.printIndent(indent);

        ConsolePrinter.printFormat("Card type: $s | Worth %d armies",
                                   cardType.toString(),
                                   cardType.getCardTypeAsInt());
    }
}
