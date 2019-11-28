package soen6441riskgame.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import com.google.gson.annotations.Expose;

import soen6441riskgame.enums.ChangedProperty;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.models.strategies.Strategy;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;
import soen6441riskgame.utils.GameHelper;
import soen6441riskgame.views.PhaseView;

/**
 * Hold player data
 *
 * Each player is a node in a linked list
 */
public class Player extends Observable {
    private static final int MAX_NUMBER_OF_CARD_TO_FORCE_EXCHANGE = 5;
    private static final int LEAST_NUMBER_OF_ARMIES_INIT_IN_TURN = 3;
    private static final int INIT_ARMY_DIVIDE_FRACTION = 3;

    @Expose
    private String name;

    @Expose
    private int unplacedArmies;

    @Expose
    private boolean isPlaying = false;

    private Player nextPlayer;

    @Expose
    private String nextPlayerName;

    private Player previousPlayer;

    @Expose
    private String previousPlayerName;

    @Expose
    private GamePhase currentPhase;

    private ArrayList<Card> holdingCards = new ArrayList<Card>();

    @Expose
    private ArrayList<String> currentPhaseActions = new ArrayList<String>();

    @Expose
    private boolean isPlayerBeAwardCard = false;

    private Strategy strategy;

    /**
     * constructor
     *
     * @param name player's name
     */
    public Player(String name) {
        this.name = name;
        this.currentPhase = GamePhase.WAITING_TO_TURN;
    }

    /**
     * copy constructor
     * 
     * @param serializedPlayer serialized player
     */
    @SuppressWarnings("unchecked")
    public Player(Player serializedPlayer) {
        this.name = serializedPlayer.name;
        this.unplacedArmies = serializedPlayer.unplacedArmies;
        this.isPlaying = serializedPlayer.isPlaying;
        this.nextPlayerName = serializedPlayer.nextPlayerName;
        this.previousPlayerName = serializedPlayer.previousPlayerName;
        this.currentPhase = serializedPlayer.currentPhase;
        this.currentPhaseActions = (ArrayList<String>) serializedPlayer.currentPhaseActions.clone();
        this.isPlayerBeAwardCard = serializedPlayer.isPlayerBeAwardCard;
    }

    /**
     * get player's strategy set by tournament mode
     * 
     * @return player's strategy set by tournament mode
     */
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * set player's strategy for tournament mode
     * 
     * @param strategy player's strategy for tournament mode
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * link next and previous players after construct;
     * 
     * @param players list of current players
     */
    public void linkNextAndPrevious(List<Player> players) {
        for (Player player : players) {
            if (nextPlayerName.equals(player.getName())) {
                setNextPlayer(player);
                continue;
            }

            if (previousPlayerName.equals(player.getName())) {
                setPreviousPlayer(player);
                continue;
            }
        }
    }

    /**
     * re-construct player object
     */
    public void reconstruct() {
        holdingCards = new ArrayList<Card>();

        this.addObserver(GameBoard.getInstance().getGameBoardPlayer().getPhaseView());

        if (currentPhase == GamePhase.REINFORCEMENT) {
            this.addObserver(GameBoard.getInstance().getExchangeCardView());
        }
    }

    /**
     * check if this player have conquered at least 1 country in the attack phase, therefore, be award a
     * card from deck
     *
     * @return is player be award card
     */
    private boolean isPlayerBeAwardCard() {
        return isPlayerBeAwardCard;
    }

    /**
     * mark this player have conquered at least 1 country in the attack phase, therefore, be award a
     * card from deck
     *
     * @param isPlayerAwardCard set the mark
     */
    public void setPlayerBeAwardCard(boolean isPlayerAwardCard) {
        this.isPlayerBeAwardCard = isPlayerAwardCard;
    }

    /**
     * get player's current phase
     *
     * @return game phase of the player
     */
    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    /**
     * set player's current phase
     *
     * @param newPhase the phase to set
     */
    public void setCurrentPhase(GamePhase newPhase) {
        if (currentPhase != newPhase) {

            boolean isChangePhaseAllowed = isChangePhaseAllowed(newPhase);

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

                if (newPhase == GamePhase.FORTIFICATION) {
                    getACardFromDeck();
                }
            } else {
                ConsolePrinter.printFormat("Player %s cannot change from phase %s to phase %s",
                                           getName(),
                                           currentPhase.toString(),
                                           newPhase.toString());
            }
        }
    }

    private boolean isChangePhaseAllowed(GamePhase newPhase) {
        boolean isChangePhaseAllowed = true;

        if ((newPhase.getGamePhaseAsInt() - currentPhase.getGamePhaseAsInt()) != 1) {
            isChangePhaseAllowed = newPhase == GamePhase.WAITING_TO_TURN
                                   && currentPhase == GamePhase.FORTIFICATION;
        }

        if (newPhase == GamePhase.ATTACK) {
            if (holdingCards.size() >= MAX_NUMBER_OF_CARD_TO_FORCE_EXCHANGE) {
                ConsolePrinter.printFormat("You have more than %d cards. Must exchange before attacking.",
                                           MAX_NUMBER_OF_CARD_TO_FORCE_EXCHANGE);

                isChangePhaseAllowed = false;
            }
            // check if unplaced armies == 0 , then just skip the reinforcement phase
            else if (this.getUnplacedArmies() == 0) {
                isChangePhaseAllowed = true;
            }
        }

        if (newPhase == GamePhase.END_OF_GAME && currentPhase == GamePhase.ATTACK) {
            isChangePhaseAllowed = true;
        }

        return isChangePhaseAllowed;
    }

    /**
     * add new card if player conquer at least 1 country during attack phase
     */
    private void getACardFromDeck() {
        if (isPlayerBeAwardCard()) {
            Card newCard = GameBoard.getInstance().getRandomAvailableCard();
            newCard.setHoldingPlayer(this);
            holdingCards.add(newCard);
            setPlayerBeAwardCard(false);
            setChanged();
            notifyObservers(ChangedProperty.CARD);
        }
    }

    /**
     * get player's list of cards
     *
     * @return player's list of cards
     */
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

    /**
     * return all the exchanged cards that player is holding
     */
    public void removeExchangedCards() {
        for (Iterator<Card> cardList = holdingCards.listIterator(); cardList.hasNext();) {
            Card card = cardList.next();
            if (card.isExchanged()) {
                card.setExchanged(false);
                card.setHoldingPlayer(null);
                cardList.remove();
            }
        }
    }

    /**
     * get the list of action for current phase
     *
     * @return list of action for current phase
     */
    public ArrayList<String> getCurrentPhaseActions() {
        return currentPhaseActions;
    }

    /**
     * add new action for current phase
     *
     * @param action the action string
     */
    public void addCurrentPhaseAction(String action) {
        currentPhaseActions.add(action);
        setChanged();
        notifyObservers(ChangedProperty.GAME_PHASE);
    }

    /**
     * get previous player on the linked list
     *
     * @return previous player
     */
    public Player getPreviousPlayer() {
        return previousPlayer;
    }

    /**
     * set previous player
     *
     * @param previousPlayer the player object
     */
    public void setPreviousPlayer(Player previousPlayer) {
        this.previousPlayer = previousPlayer;
        this.previousPlayerName = previousPlayer.getName();

        if (previousPlayer.getNextPlayer() != this) {
            previousPlayer.setNextPlayer(this);
        }
    }

    /**
     * get next player on the linked list
     *
     * @return next player on the linked list
     */
    public Player getNextPlayer() {
        return nextPlayer;
    }

    /**
     * set next player
     *
     * @param nextPlayer the player object
     */
    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
        this.nextPlayerName = nextPlayer.getName();
        if (nextPlayer.getPreviousPlayer() != this) {
            nextPlayer.setPreviousPlayer(this);
        }
    }

    /**
     * get player name
     *
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * get total armies a player have
     *
     * @return total armies
     */
    public int getTotalArmies() {
        int totalArmies = 0;

        ArrayList<Country> conqueredCountries = getConqueredCountries();
        for (Country country : conqueredCountries) {
            totalArmies += country.getArmyAmount();
        }

        totalArmies += getUnplacedArmies();

        return totalArmies;
    }

    /**
     * get a list of conquered continents of this player
     *
     * @return list of conquered continents
     */
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

    /**
     * check if this player is still in the game
     *
     * @return is this player is still in the game
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * set is this player is till in the game
     *
     * @param isPlaying is this player is till in the game
     */
    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    /**
     * get player unplaced armies
     *
     * @return player unplaced armies
     */
    public int getUnplacedArmies() {
        return unplacedArmies;
    }

    /**
     * set player unplaced armies
     *
     * @param unplacedArmies the number of armies
     */
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

    /**
     * do the reinforcement
     *
     * @param country        country to reinforce
     * @param numberOfArmies armies to reinforce
     */
    public void reinforce(Country country, int numberOfArmies) {
        if (!country.isCountryBelongToPlayer(this)) {
            return;
        }

        if (this.getUnplacedArmies() != 0) {
            country.receiveArmiesFromUnPlacedArmies(numberOfArmies);
            this.addCurrentPhaseAction("Reinforce: " + country.getName() + " with " + numberOfArmies);
        }

        if (this.getUnplacedArmies() == 0) {
            ConsolePrinter.printFormat("Player %s enter ATTACK phase", this.getName());
            this.setCurrentPhase(GamePhase.ATTACK);
        }
    }

    /**
     * do the fortify
     *
     * @param fromCountry    from country
     * @param toCountry      to country
     * @param numberOfArmies armies to fortify
     */
    public void fortify(Country fromCountry, Country toCountry, int numberOfArmies) {
        fromCountry.moveArmies(toCountry, numberOfArmies);
        this.addCurrentPhaseAction("Fortify: from "
                                   + fromCountry.getName()
                                   + " to "
                                   + toCountry.getName()
                                   + " with "
                                   + numberOfArmies
                                   + " armies");
    }

    /**
     * do the attack
     *
     * @param attackingCountry attacker country
     * @param defendingCountry defender country
     * @param attackerNumDice  number of dices for attacker
     * @param defenderNumDice  number of dices for defender
     */
    public void attack(Country attackingCountry,
                       Country defendingCountry,
                       int attackerNumDice,
                       int defenderNumDice) {
        // attack starts
        int[] attackerDiceValues = new int[attackerNumDice];
        int[] defenderDiceValues = new int[defenderNumDice];

        printDiceValues(attackerNumDice, defenderNumDice, attackerDiceValues, defenderDiceValues);

        Player currentPlayer = this;

        // now we will check who loses an army
        int attackerMaxDiceValue = GameHelper.getMax(attackerDiceValues, false);
        int defenderMaxDiceValue = GameHelper.getMax(defenderDiceValues, false);

        if (attackerMaxDiceValue > defenderMaxDiceValue) {
            // defending army is lost
            lostOneArmy(defendingCountry, currentPlayer);
        } else {
            // attacking army is lost
            lostOneArmy(attackingCountry, currentPlayer);
        }

        if (defenderNumDice != 1 && attackerNumDice != 1) {
            int attackerSecondMaxDiceValue = GameHelper.getMax(attackerDiceValues, true);
            int defenderSecondMaxDiceValue = GameHelper.getMax(defenderDiceValues, true);

            if (attackerSecondMaxDiceValue > defenderSecondMaxDiceValue) {
                lostOneArmy(defendingCountry, currentPlayer);
            } else {
                lostOneArmy(attackingCountry, currentPlayer);
            }
        }
        // attack ends
    }

    private void printDiceValues(int attackerNumDice,
                                 int defenderNumDice,
                                 int[] attackerDiceValues,
                                 int[] defenderDiceValues) {
        StringBuilder printDiceValues = new StringBuilder("Attacker: ");

        for (int i = 0; i < attackerNumDice; i++) {
            attackerDiceValues[i] = GameHelper.rollDice();
            printDiceValues.append(attackerDiceValues[i]).append("    ");
        }
        printDiceValues.append("\nDefender: ");
        for (int i = 0; i < defenderNumDice; i++) {
            defenderDiceValues[i] = GameHelper.rollDice();
            printDiceValues.append(defenderDiceValues[i]).append("    ");
        }
        ConsolePrinter.printFormat("%s", printDiceValues.toString());
    }

    private void lostOneArmy(Country lostArmyCountry, Player lostArmyPlayer) {
        String playerRole = lostArmyPlayer.getCurrentPhase() == GamePhase.ATTACK ? "attacker" : "defender";

        lostArmyCountry.setArmyAmount(lostArmyCountry.getArmyAmount() - 1);
        ConsolePrinter.printFormat("The %s %s has lost 1 army from %s. %d armies left.",
                                   playerRole,
                                   lostArmyCountry.getConquerer().getName(),
                                   lostArmyCountry.getName(),
                                   lostArmyCountry.getArmyAmount());

        lostArmyPlayer.addCurrentPhaseAction("Attack: The "
                                             + playerRole
                                             + " "
                                             + lostArmyCountry.getConquerer().getName()
                                             + " has lost 1 army from "
                                             + lostArmyCountry.getName()
                                             + " | "
                                             + lostArmyCountry.getArmyAmount()
                                             + " armies left.");
    }

    /**
     * do the attack move
     *
     * @param fromCountry    from country
     * @param toCountry      to country
     * @param numberOfArmies armies to move
     */
    public void attackMove(Country fromCountry, Country toCountry, int numberOfArmies) {
        fromCountry.moveArmies(toCountry, numberOfArmies);
    }

    public ArrayList<CardSet> buildValidCardSets() {
        ArrayList<CardSet> cardSets = new ArrayList<>();

        ArrayList<Card> cards = this.getHoldingCards();

        if (cards.size() < 3) {
            return cardSets;
        }

        HashMap<Card, Boolean> cardsInSet = new HashMap<>();
        for (Card card : cards) {
            cardsInSet.put(card, false);
        }

        ArrayList<Card> notPicked = (ArrayList<Card>) GameHelper.getAllKeysForValue(cardsInSet, false);

        while (notPicked.size() >= 5) {
            ArrayList<Integer> cardIndexes = new ArrayList<>();
            for (int cardIndex = 0; cardIndex < notPicked.size(); cardIndex++) {
                cardIndexes.add(cardIndex);
            }

            // randomly pick 3 card with index
            ArrayList<Integer> picked = GameHelper.getRandomElements(cardIndexes, 3);
            CardSet cardSet = new CardSet(cards.get(picked.get(0)),
                                          cards.get(picked.get(1)),
                                          cards.get(picked.get(2)));
            if (cardSet.isSetValid()) {
                cardSets.add(cardSet);
                for (int cardIndex : picked) {
                    cardsInSet.put(cards.get(cardIndex), true);
                }
            }

            notPicked = (ArrayList<Card>) GameHelper.getAllKeysForValue(cardsInSet, false);
        }

        return cardSets;
    }
}
