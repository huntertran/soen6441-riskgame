package soen6441riskgame.models;

import java.util.ArrayList;

import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.singleton.GameMap;

/**
 * Hold player data Each player is a node in a linked list
 */
public class Player {
    private String name;
    private int armies;
    private int unplacedArmies;
    private boolean isPlaying = false;
    private boolean isLost = false;
    private Player nextPlayer;
    private Player previousPlayer;
    private GamePhase currentPhase;

    public Player(String name) {
        this.name = name;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(GamePhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    /**
     * get previous player on the linked list
     * 
     * @return
     */
    public Player getPreviousPlayer() {
        return previousPlayer;
    }

    public void setPreviousPlayer(Player previousPlayer) {
        this.previousPlayer = previousPlayer;

        if (previousPlayer.getNextPlayer() != this) {
            previousPlayer.setNextPlayer(this);
        }
    }

    /**
     * get next player on the linked list
     * @return
     */
    public Player getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
        if (nextPlayer.getPreviousPlayer() != this) {
            nextPlayer.setPreviousPlayer(this);
        }
    }

    public String getName() {
        return name;
    }

    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean isLost) {
        this.isLost = isLost;
    }

    /**
     * get all the conquered country of this player
     * @return empty list if no country
     */
    public ArrayList<Country> getConqueredCountries() {
        ArrayList<Country> conquered = new ArrayList<>();

        for (Country country : GameMap.getInstance().getCountries()) {
            if (country.getConquerer().equals(this)) {
                conquered.add(country);
            }
        }

        return conquered;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public int getUnplacedArmies() {
        return unplacedArmies;
    }

    public void setUnplacedArmies(int unplacedArmies) {
        this.unplacedArmies = unplacedArmies;
    }

    public int getArmies() {
        return armies;
    }

    public void setArmies(int armies) {
        this.armies = armies;
    }

    public void setName(String name) {
        this.name = name;
    }
}
