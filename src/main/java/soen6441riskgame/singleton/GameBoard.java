package soen6441riskgame.singleton;

import java.util.ArrayList;

import soen6441riskgame.models.Player;

/**
 * Hold the game and map data
 */
public class GameBoard implements Resettable {

    private static GameBoard instance = new GameBoard();

    private GameBoardPlayer gameBoardPlayer = new GameBoardPlayer();
    private GameBoardMap gameBoardMap = new GameBoardMap();

    public GameBoard() {
    };

    public GameBoardPlayer getGameBoardPlayer(){
        return gameBoardPlayer;
    }

    public GameBoardMap getGameBoardMap(){
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

    public ArrayList<Player> getPlayers() {
        return gameBoardPlayer.getPlayers();
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

    /**
     * get player object from name
     *
     * @param name
     * @return null if player name is not in the list
     */
    public Player getPlayerFromName(String name) {
        return gameBoardPlayer.getPlayerFromName(name);
    }

    /**
     * add a new player and link to next/previous player
     *
     * @param name
     */
    public void addPlayer(String name) {
        gameBoardPlayer.addPlayer(name);
    }

    /**
     * remove a player and destroy link to next/previous player
     *
     * @param name
     */
    public void removePlayer(String name) {
        gameBoardPlayer.removePlayer(name);
    }
}