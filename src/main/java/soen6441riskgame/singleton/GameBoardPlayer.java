package soen6441riskgame.singleton;

import java.util.ArrayList;
import soen6441riskgame.models.Player;
import soen6441riskgame.utils.ConsolePrinter;

public class GameBoardPlayer implements Resettable {
    private ArrayList<Player> players = new ArrayList<Player>();

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * get player object from name
     *
     * @param name
     * @return null if player name is not in the list
     */
    public Player getPlayerFromName(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    /**
     * add a new player and link to next/previous player
     *
     * @param name
     */
    public void addPlayer(String name) {
        Player player = getPlayerFromName(name);
        if (player == null) {
            player = new Player(name);
            players.add(player);
            int previousPlayerIndex = 0;
            if (players.size() > 1) {
                previousPlayerIndex = players.size() - 2;
            }
            Player previousPlayer = players.get(previousPlayerIndex);
            Player nextPlayer = players.get(0);
            player.setPreviousPlayer(previousPlayer);
            player.setNextPlayer(nextPlayer);
            ConsolePrinter.printFormat("Player %s added", name);
        }
    }

    /**
     * remove a player and destroy link to next/previous player
     *
     * @param name
     */
    public void removePlayer(String name) {
        Player player = getPlayerFromName(name);
        if (player != null) {
            players.remove(player);
            Player previousPlayer = player.getPreviousPlayer();
            Player nextPlayer = player.getNextPlayer();
            previousPlayer.setNextPlayer(nextPlayer);
            nextPlayer.setPreviousPlayer(previousPlayer);
            ConsolePrinter.printFormat("Player %s removed", name);
        } else {
            ConsolePrinter.printFormat("Player %s not exist in game", name);
        }
    }

    @Override
    public void reset() {
        players = new ArrayList<>();
    }
}