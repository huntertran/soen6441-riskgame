package soen6441riskgame.models;

import java.util.ArrayList;
import java.util.Observable;
import soen6441riskgame.controllers.GameController;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.singleton.GameBoard;

/**
 * Hold player data Each player is a node in a linked list
 */
public class Player extends Observable {
    private String name;
    private int armies;
    private int unplacedArmies;
    private boolean isPlaying = false;
    private Player nextPlayer;
    private Player previousPlayer;
    private GamePhase currentPhase;

    public Player(String name) {
        this.name = name;
        this.currentPhase = GamePhase.WAITING_TO_TURN;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(GamePhase currentPhase) {
        if (this.currentPhase != currentPhase) {
            this.currentPhase = currentPhase;
            setChanged();
            notifyObservers();
        }
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
     *
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

    /**
     * get all the conquered country of this player
     *
     * @return empty list if no country
     */
    public ArrayList<Country> getConqueredCountries() {
        ArrayList<Country> conquered = new ArrayList<>();

        for (Country country : GameBoard.getInstance().getGameBoardMap().getCountries()) {
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

    /**
     * REINFORCEMENT PHASE get the number of armies player will get for reinforcement phase for all the
     * country player have.
     *
     * @return the number of armies. Minimum number of armies are 3
     */
    public int getArmiesFromAllConqueredCountries() {
        ArrayList<Country> conqueredCountries = getConqueredCountries();
        return Math.round(conqueredCountries.size() / 3);
    }

    /**
     * REINFORCEMENT PHASE get the number of armies player will have for the conquered continent
     *
     * @param currentPlayer current player
     * @return the number of armies. 0 if user don't own any continent.
     */
    public int getArmiesFromConqueredContinents() {
        int armiesFromConqueredContinents = 0;

        for (Continent continent : GameBoard.getInstance().getGameBoardMap().getContinents()) {
            if (this.equals(continent.getConquerer())) {
                armiesFromConqueredContinents = armiesFromConqueredContinents + continent.getArmy();
            }
        }

        return armiesFromConqueredContinents;
    }

    /**
     * REINFORCEMENT PHASE calculate the number of armies a player will have for his reinforcement phase
     *
     * @param gameController
     */
    public void calculateReinforcementArmies(GameController gameController) {
        int armiesFromAllConqueredCountries = getArmiesFromAllConqueredCountries();
        int armiesFromConqueredContinents = getArmiesFromConqueredContinents();
        if (armiesFromAllConqueredCountries < 3) {
            armiesFromAllConqueredCountries = 3;
        }
        int newUnplacedArmies = getUnplacedArmies() + armiesFromAllConqueredCountries + armiesFromConqueredContinents;
        setUnplacedArmies(newUnplacedArmies);
    }
}
