package soen6441riskgame.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import soen6441riskgame.enums.ChangedProperty;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;

/**
 * Hold player data Each player is a node in a linked list
 */
public class Player extends Observable {
    private String name;
    private int unplacedArmies;
    private boolean isPlaying = false;
    private Player nextPlayer;
    private Player previousPlayer;
    private GamePhase currentPhase;
    private ArrayList<Card> holdingCards = new ArrayList<Card>();
    private ArrayList<String> currentPhaseActions = new ArrayList<String>();
    private static final int MAX_NUMBER_OF_CARD_TO_FORCE_EXCHANGE = 5;
    private static final int LEAST_NUMBER_OF_ARMIES_INIT_IN_TURN = 3;
    private static final int INIT_ARMY_DIVIDE_FRACTION = 3;
    private boolean isPlayerBeAwardCard = false;

    public Player(String name) {
        this.name = name;
        this.currentPhase = GamePhase.WAITING_TO_TURN;
    }

    public boolean isPlayerBeAwardCard() {
        return isPlayerBeAwardCard;
    }

    public void setPlayerBeAwardCard(boolean isPlayerAwardCard) {
        this.isPlayerBeAwardCard = isPlayerAwardCard;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(GamePhase newPhase) {
        if (currentPhase != newPhase) {

            boolean isChangePhaseAllowed = true;

            if ((newPhase.getGamePhaseAsInt() - currentPhase.getGamePhaseAsInt()) != 1) {
                isChangePhaseAllowed = false;

                if (newPhase != GamePhase.WAITING_TO_TURN
                    || currentPhase != GamePhase.FORTIFICATION) {
                    isChangePhaseAllowed = false;
                } else {
                    isChangePhaseAllowed = true;
                }
            }

            if (isChangePhaseAllowed) {
                currentPhase = newPhase;
                currentPhaseActions.clear();
                setChanged();
                notifyObservers(ChangedProperty.GAME_PHASE);

                if (newPhase == GamePhase.REINFORCEMENT) {
                    this.addObserver(GameBoard.getInstance().getExchangeCardView());
                } else {
                    this.deleteObserver(GameBoard.getInstance().getExchangeCardView());
                }

                getACardFromDeck(newPhase);
            } else {
                ConsolePrinter.printFormat("Player %s cannot change from phase %s to phase %s",
                                           getName(),
                                           currentPhase.toString(),
                                           newPhase.toString());
            }
        }
    }

    /**
     * add new card if player conquer at least 1 country during attack phase
     * 
     * @param newPhase
     */
    private void getACardFromDeck(GamePhase newPhase) {
        if (newPhase == GamePhase.ATTACK) {
            if (isPlayerBeAwardCard()) {
                Card newCard = GameBoard.getInstance().getRandomAvailableCard();
                newCard.setHoldingPlayer(this);
                holdingCards.add(newCard);
                setPlayerBeAwardCard(false);
            }
        }
    }

    public void addCard(Card card) {
        if (holdingCards.size() >= MAX_NUMBER_OF_CARD_TO_FORCE_EXCHANGE) {
            ConsolePrinter.printFormat("You have more than %d cards. Must exchange before attacking.",
                                       MAX_NUMBER_OF_CARD_TO_FORCE_EXCHANGE);
        }

        holdingCards.add(card);
        setChanged();
        notifyObservers(ChangedProperty.CARD);
    }

    public ArrayList<Card> getHoldingCards() {
        return holdingCards;
    }

    /**
     * get the player's card in specific position
     *
     * @param position start with 1
     * @return null if position not exist
     */
    public Card getHoldingCard(int position) {
        if (position > holdingCards.size() || position <= 0) {
            ConsolePrinter.printFormat("You only have %d card", holdingCards.size());
            return null;
        } else {
            return holdingCards.get(position - 1);
        }
    }

    public void removeExchangedCards() {
        for (Iterator<Card> cardList = holdingCards.listIterator(); cardList.hasNext();) {
            Card card = cardList.next();
            if (card.isExchanged()) {
                cardList.remove();
            }
        }
    }

    public ArrayList<String> getCurrentPhaseActions() {
        return currentPhaseActions;
    }

    public void addCurrentPhaseAction(String action) {
        currentPhaseActions.add(action);
        setChanged();
        notifyObservers();
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

    public int getTotalArmies() {
        int totalArmies = 0;

        ArrayList<Country> conqueredCountries = getConqueredCountries();
        for (Country country : conqueredCountries) {
            totalArmies += country.getArmyAmount();
        }

        totalArmies += getUnplacedArmies();

        return totalArmies;
    }

    public ArrayList<Continent> getConqueredContinents() {
        ArrayList<Continent> conquered = new ArrayList<>();

        for (Continent continent : GameBoard.getInstance().getGameBoardMap().getContinents()) {
            if (continent != null) {
                if (this.equals(continent.getConquerer())) {
                    conquered.add(continent);
                }
            }
        }

        return conquered;
    }

    /**
     * get all the conquered country of this player
     *
     * @return empty list if no country
     */
    public ArrayList<Country> getConqueredCountries() {
        ArrayList<Country> conquered = new ArrayList<>();

        for (Country country : GameBoard.getInstance().getGameBoardMap().getCountries()) {
            if (country != null) {
                if (this.equals(country.getConquerer())) {
                    conquered.add(country);
                }
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

    /**
     * REINFORCEMENT PHASE get the number of armies player will get for reinforcement phase for all the
     * country player have.
     *
     * @return the number of armies. Minimum number of armies are #{@value #INIT_ARMY_DIVIDE_FRACTION}
     */
    private int getArmiesFromAllConqueredCountries() {
        ArrayList<Country> conqueredCountries = getConqueredCountries();
        return Math.round(conqueredCountries.size() / INIT_ARMY_DIVIDE_FRACTION);
    }

    /**
     * REINFORCEMENT PHASE get the number of armies player will have for the conquered continent
     *
     * @return the number of armies. 0 if user don't own any continent.
     */
    private int getArmiesFromConqueredContinents() {
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
     */
    public void calculateReinforcementArmies() {
        if (this.getCurrentPhase() != GamePhase.REINFORCEMENT) {
            ConsolePrinter.printFormat("Cannot get new army for player %s on $%s phase",
                                       this.getName(),
                                       this.getCurrentPhase().toString());
            return;
        }

        int armiesFromAllConqueredCountries = getArmiesFromAllConqueredCountries();
        int armiesFromConqueredContinents = getArmiesFromConqueredContinents();

        if (armiesFromAllConqueredCountries < LEAST_NUMBER_OF_ARMIES_INIT_IN_TURN) {
            armiesFromAllConqueredCountries = LEAST_NUMBER_OF_ARMIES_INIT_IN_TURN;
        }

        int newUnplacedArmies = getUnplacedArmies() + armiesFromAllConqueredCountries + armiesFromConqueredContinents;
        setUnplacedArmies(newUnplacedArmies);
    }
}
