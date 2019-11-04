package soen6441riskgame.singleton;

import soen6441riskgame.views.ExchangeCardView;

/**
 * Hold the game and map data
 */
public class GameBoard implements Resettable {

    private static GameBoard instance = new GameBoard();

    private GameBoardPlayer gameBoardPlayer = new GameBoardPlayer();
    private GameBoardMap gameBoardMap = new GameBoardMap();
    private ExchangeCardView exchangeCardView = new ExchangeCardView();

    public GameBoard() {
    };

    public ExchangeCardView getExchangeCardView(){
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