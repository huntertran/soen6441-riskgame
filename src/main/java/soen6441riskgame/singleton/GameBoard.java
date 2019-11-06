package soen6441riskgame.singleton;

import java.util.ArrayList;
import java.util.Random;

import soen6441riskgame.enums.CardType;
import soen6441riskgame.models.Card;
import soen6441riskgame.views.ExchangeCardView;

/**
 * Hold the game and map data
 */
public class GameBoard implements Resettable {
    private static final int NUMBER_OF_CARDS = 56;

    private static GameBoard instance = new GameBoard();
    private GameBoardPlayer gameBoardPlayer = new GameBoardPlayer();
    private GameBoardMap gameBoardMap = new GameBoardMap();
    private ExchangeCardView exchangeCardView = new ExchangeCardView();
    private Card[] cards = new Card[NUMBER_OF_CARDS];

    /**
     * init GameBoard
     */
    public GameBoard() {
        initCards();
    };

    /**
     * create a deck of {@value #NUMBER_OF_CARDS} cards, including 2 wild cards and equal cards for
     * other type
     */
    public void initCards() {
        // 2 wild card
        for (int index = 0; index < 2; index++) {
            Card card = new Card(CardType.Wild);
            cards[index] = card;
        }

        // 18 card for each type
        for (int index = 2; index < 20; index++) {
            Card card = new Card(CardType.Infantry);
            cards[index] = card;
        }

        for (int index = 20; index < 38; index++) {
            Card card = new Card(CardType.Cavalry);
            cards[index] = card;
        }

        for (int index = 38; index < 56; index++) {
            Card card = new Card(CardType.Artillery);
            cards[index] = card;
        }
    }

    /**
     * get an specific card from deck for test
     *
     * @param cardType type of card to get
     * @return a card that not hold by any player
     */
    public Card getSpecificCardForTest(CardType cardType) {
        ArrayList<Card> availableCard = new ArrayList<>();

        for (Card card : cards) {
            if (card.getHoldingPlayer() == null) {
                availableCard.add(card);
            }
        }

        for (Card card : availableCard) {
            if (card.getCardType() == cardType) {
                return card;
            }
        }

        return null;
    }

    /**
     * randomly get an available card from deck
     *
     * @return a card that not hold by any player
     */
    public Card getRandomAvailableCard() {
        ArrayList<Card> availableCard = new ArrayList<>();

        for (Card card : cards) {
            if (card.getHoldingPlayer() == null) {
                availableCard.add(card);
            }
        }

        Random random = new Random();
        int cardIndex = random.nextInt(availableCard.size() - 1) + 1;

        return availableCard.get(cardIndex);
    }

    /**
     * get Exchange card view
     *
     * @return the exchange card view observing the cards holding by player in turn
     */
    public ExchangeCardView getExchangeCardView() {
        return exchangeCardView;
    }

    /**
     * get GameBoardPlayer instance
     *
     * @return GameBoardPlayer instance that hold players
     */
    public GameBoardPlayer getGameBoardPlayer() {
        return gameBoardPlayer;
    }

    /**
     * get GameBoardMap instance
     *
     * @return GameBoardMap instance that Countries and Continents
     */
    public GameBoardMap getGameBoardMap() {
        return gameBoardMap;
    }

    /**
     * set a new instance for unit testing
     *
     * @param newTestingInstance new instance of GameBoard
     */
    public static void setTestingInstance(GameBoard newTestingInstance) {
        instance = newTestingInstance;
    }

    /**
     * get singleton instance of GameBoard
     *
     * @return GameBoard instance
     */
    public static GameBoard getInstance() {
        return instance;
    }

    /**
     * reset the map
     */
    @Override
    public void reset() {
        gameBoardMap.reset();
        gameBoardPlayer.reset();
    }
}