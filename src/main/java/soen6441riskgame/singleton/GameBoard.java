package soen6441riskgame.singleton;

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

    public GameBoard() {
        initCards();
    };

    public void initCards() {
        // 2 wild card
        for (int index = 0; index < 2; index++) {
            Card card = new Card(CardType.Wild);
            cards[index] = card;
        }

        // 18 card for each type
        for (int index = 3; index < 20; index++) {
            Card card = new Card(CardType.Infantry);
            cards[index] = card;
        }

        for (int index = 21; index < 38; index++) {
            Card card = new Card(CardType.Cavalry);
            cards[index] = card;
        }

        for (int index = 49; index < 56; index++) {
            Card card = new Card(CardType.Artillery);
            cards[index] = card;
        }
    }

    public ExchangeCardView getExchangeCardView() {
        return exchangeCardView;
    }

    public GameBoardPlayer getGameBoardPlayer() {
        return gameBoardPlayer;
    }

    public GameBoardMap getGameBoardMap() {
        return gameBoardMap;
    }

    /**
     * set a new instance for unit testing
     *
     * @param newTestingInstance
     */
    public static void setTestingInstance(GameBoard newTestingInstance) {
        instance = newTestingInstance;
    }

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