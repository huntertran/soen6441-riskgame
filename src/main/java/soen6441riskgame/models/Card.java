package soen6441riskgame.models;

import soen6441riskgame.enums.CardType;
import soen6441riskgame.utils.ConsolePrinter;


/**
* The Card Class.
* This class initializes the card type according to the Excahange Card requirement
* 
* 
*
*/

public class Card implements Viewable {
    private CardType cardType;
    private boolean isExchanged = false;

    public Card(CardType type){
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

    @Override
    public void view(int indent) {
        this.printIndent(indent);

        ConsolePrinter.printFormat("Card type: $s | Worth %d armies",
                                   cardType.toString(),
                                   cardType.getCardTypeAsInt());
    }
}
