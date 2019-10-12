package soen6441riskgame.commands;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import java.util.Collections;


import soen6441riskgame.enums.InitialPlayerArmy;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.enums.PrintConsoleAndUserInput;
import soen6441riskgame.enums.Card;
import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.views.CardView;
import soen6441riskgame.views.FinishView;
import java.util.Observable;

// TODO: Auto-generated Javadoc
/**
 * Game contains the class to create a model for the game. 
 * the class is bounded with the Game Controller
 * 
 * @version 1.0.0
 */

public class Game extends Observable {

	/** The MapEditor Command. */
	private MapEditorCommands mapModel;
	
	/** The game phase. */
	private GamePhase gamePhase;
	
	/** The current player ID. */
	private int currentPlayerId;
	
	/** The MINIMUM REINFORCEMENT PLAYERS. */
	private int MINIMUM_REINFORCEMENT_PlAYERS = 3;

	/** The connected own countries. */
	private ArrayList<String> connectedOwnCountries = new ArrayList<String>();
	
	/** The Initial country source. */
	private String initialSourceCountry;

	/** The print. */
	PrintConsoleAndUserInput print = new PrintConsoleAndUserInput();

	/** The player list. */
	private ArrayList<Player> playerList = new ArrayList<Player>();
	
	/** The player country. */
	HashMap<Player, ArrayList<Country>> playerCountry = new HashMap<>();

	/** The Risk Cards. */
	private ArrayList<Card> riskCards= new ArrayList<>();
	

	/** The game phase details. */
	private ArrayList<String> gamePhaseDetails = new ArrayList<>();

	/** The exchange number. */
	private Integer armiesAfterExchange= 5;
	
	public boolean dominationViewOn = false;

	CardView cardview = new CardView(this);
	/**
	 * Instantiates a new game.
	 * @param map the map
	 */
	//Initializes the map and the game phase of the game.
	public Game(MapEditorCommands map) {
		super();
		this.mapModel = map;
		this.setGamePhase(GamePhase.Startup);
	}


	//Functions called by the initializeGame() from the GameController. 
	/**
	 * This method adds the player to the game's player list.
	 * @param player get the player
	 */
	public void addPlayer(Player player) {
		this.playerList.add(player);
	}

	/**
	 * This method initializes the Game.
	 * It assigns the initial armies to the player.
	 * It randomly assigns the countries to the players. 
	 */
	public void startGame() {
		this.addObserver(cardview);
		//Assigning the Initial armies.
		for(int i=0; i<playerList.size(); i++){
			playerList.get(i).setNumberOfInitialArmies(InitialPlayerArmy.getInitialArmyCount(playerList.size()));
			System.out.println("Player ID: "+playerList.get(i).getPlayerId()+" Player Name: "+playerList.get(i).getPlayerName()+" Player's Army: "+playerList.get(i).getNumberOfInitialArmies()+" Player's Color"+playerList.get(i).getColor());
			playerList.get(i).setConcuredContinents(mapModel.getContinentList());
			gamePhaseDetails.add("Player ID: "+playerList.get(i).getPlayerId());
			gamePhaseDetails.add("Player Name: "+playerList.get(i).getPlayerName());
			gamePhaseDetails.add("Player's Army: "+playerList.get(i).getNumberOfInitialArmies());
			gamePhaseDetails.add("Player's Color"+playerList.get(i).getColor());
			
		}
		
		int players_count = playerList.size();
		System.out.println("Player Count:"+players_count);
		int countries_count = mapModel.getCountryList().size();
		int players_id = 0;

		ArrayList<Integer> randomNumbers = new ArrayList<>();
		for(int i=0; i<countries_count; i++){
			randomNumbers.add(i);
		}
		Collections.shuffle(randomNumbers, new Random());

		for(int i=0; i<countries_count ; i++){
			if (players_id == players_count){
				players_id = 0;
			}

			Country assign_country = mapModel.getCountryList().get(randomNumbers.get(i));
			assignPlayerCountry(playerList.get(players_id),assign_country);
			assignUnassigned(playerList.get(players_id),assign_country);
			playerList.get(players_id).assignCountryToPlayer(assign_country);
			gamePhaseDetails.add(assign_country.getCountryName()+" added for the player: "+players_id);
			players_id++;
		}

		for (Map.Entry<Player, ArrayList<Country>> entry : playerCountry.entrySet()){
			Player key = entry.getKey();
			ArrayList<Country> value = entry.getValue();
			System.out.println("\n"+key.getPlayerName()+" countries: \n");
			for(Country aString : value){
				System.out.println(aString.getCountryName());
			}
		}
		notifyObserverslocal(this);
	}

	/**
	 * This method assigns the player to the corresponding country. 
	 * @param player player
	 * @param country country 
	 */
	public void assignPlayerCountry(Player player, Country country){
		if(playerCountry.containsKey(player)){
			playerCountry.get(player).add(country);
		}
		else{
			ArrayList<Country> assign_country = new ArrayList<>();
			assign_country.add(country);
			playerCountry.put(player, assign_country);
		}
		country.setCountryColor(player.getColor());
		country.setPlayerId(player.getPlayerId());
	}

	//Functions called by numberOfArmiesClickListener() from the GameController.
	/**
	 * This method adds armies to the country based on the startup or reinforcement game phase.
	 * @param countryName name of country
	 */
	public void addingCountryArmy(String countryName){
		if(gamePhase == gamePhase.Attack || gamePhase == gamePhase.Fortification){
			print.consoleOut("Armies can't be added during Attack or Fortification Phase.");
			return;
		}
		if(gamePhase == gamePhase.Startup) {
			Boolean added = addingStartupCountryArmy(countryName);
			if(added)
			{
				setupNextPlayerTurn();
			}
		}
		else if (gamePhase == gamePhase.Reinforcement){
			addingReinforcementCountryArmy(countryName);
		}
		updateGame();
		notifyObserverslocal(this);
	}

	/**
	 * This method add armies to the country during the startup phase and returns true when successful.
	 * @param countryName name of country
	 * @return true if valid phase otherwise false
	 */
	public boolean addingStartupCountryArmy(String countryName){
		if(this.gamePhase != gamePhase.Startup){
			print.consoleOut("Not a Valid Phase");
			return false;
		}

		Player player = this.getCurrentPlayer();

		if(player == null){
			print.consoleOut("Player ID"+currentPlayerId+"does not exist.");
			return false;
		}
		if(player.getNumberOfInitialArmies() == 0){
			print.consoleOut("Player "+player.getPlayerName()+"Doesn't have any Armies.");
			this.setupNextPlayerTurn();
			return false;
		}
		Country country = playerCountry.get(player).stream()
				.filter(c -> c.getCountryName().equalsIgnoreCase(countryName)).findAny().orElse(null);
		if (country == null) {
			print.consoleOut("Country name -  " + countryName + " does not exist!");
			return false;
		}
		assignUnassigned(player,country);
		return true;
	}

	/**
	 * This method adds armies to the country during the reinforcement phase and returns when successful. 
	 * @param countryName name of country
	 * @return true
	 */
	public boolean addingReinforcementCountryArmy(String countryName){
		
		if(this.gamePhase != gamePhase.Reinforcement){
			print.consoleOut("Not a Valid Phase");
			return false;
		}

		Player player = this.getCurrentPlayer();

		if(player == null){
			print.consoleOut("Player ID"+currentPlayerId+"does not exist.");
			return false;
		}
		if(player.getNumberOfReinforcedArmies() == 0){
			print.consoleOut("Player "+player.getPlayerName()+": Doesn't have any Armies.");
			return false;
		}
		Country country = playerCountry.get(player).stream()
				.filter(c -> c.getCountryName().equalsIgnoreCase(countryName)).findAny().orElse(null);
		if (country == null) {
			print.consoleOut("Country Name: " + countryName + " does not exist!");
			return false;
		}
		assignReinforcement(player,country);
		gamePhaseDetails.add(player.getPlayerName()+ " added army to the country "+ country.getCountryName());
		notifyObserverslocal(this);
		return true;
	}

	/**
	 * This method initializes the reinforcement phase for each player by adding corresponding number of armies. 
	 */
	public void reinforcementPhaseSetup() {
		gamePhaseDetails.removeAll(gamePhaseDetails);
		Player player = getCurrentPlayer();
		if(player.getCards().size()>2) {
			
			cardview.Exchange();
			
		}
		gamePhaseDetails.add("card:"+player.getCards().size());
		System.out.println("card:"+player.getCards().size());
		int countries_count = player.calculationForNumberOfArmiesInReinforcement(playerCountry,mapModel.getContinentList());
		gamePhaseDetails.add("Calculating reinforcement");
		countries_count = countries_count < MINIMUM_REINFORCEMENT_PlAYERS ? MINIMUM_REINFORCEMENT_PlAYERS : countries_count;
		System.out.println("Countries Count:" + countries_count);
		gamePhaseDetails.add("Number of armies get from reinforcement:"+countries_count);
		player.setNumberOfReinforcedArmies(countries_count);
	}

	/**
	 * This method updates the game phase of the game during the end of every Startup, Reinforcement and Fortification phase.
	 */
	public void updateGame() {
		if (this.getGamePhase() == gamePhase.Startup) {
//
//			gamePhaseDetails.removeAll(gamePhaseDetails);r
			long pendingPlayersCount = playerList.stream().filter(p -> p.getNumberOfInitialArmies() > 0).count();
			System.out.println(pendingPlayersCount);

			if (pendingPlayersCount == 0) {
				this.setGamePhase(gamePhase.Reinforcement);
				currentPlayerId = 0;
				reinforcementPhaseSetup();
			}
		} 
		else if (this.getGamePhase() == gamePhase.Reinforcement) {	
			if (getCurrentPlayer().getNumberOfReinforcedArmies() == 0) {
				gamePhaseDetails.removeAll(gamePhaseDetails);
				this.setGamePhase(gamePhase.Attack);
			}

		} 
		else if (this.getGamePhase() ==  gamePhase.Attack) {
			gamePhaseDetails.removeAll(gamePhaseDetails);
			this.setGamePhase(gamePhase.Fortification);
			notifyObserverslocal(this);
		}
		else if (this.getGamePhase() == gamePhase.Fortification) {
			gamePhaseDetails.removeAll(gamePhaseDetails);
			this.setGamePhase(gamePhase.Reinforcement);
			notifyObserverslocal(this);
		}
	}

	//Functions called by addSourceCountriesListener() from the GameController.
	/**
	 * This method returns the neighboring connected countries of a specific country.
	 * @param source source countries
	 * @return finalCountries countries
	 */
	public ArrayList<String> getNeighbouringCountries(String source) {

		System.out.println("source Country Name :" + source);
		gamePhaseDetails.add("source Country Name :" + source);
		System.out.print(connectedOwnCountries.toString());
		Player currentPlayer = this.getCurrentPlayer();
		initialSourceCountry = source;

		ArrayList<String> countriesAssignedToPlayer = new ArrayList<String>();
		ArrayList<String> finalCOuntries = new ArrayList<String>();

		ArrayList<Country> countryList = playerCountry.get(currentPlayer);
		ArrayList<String> neighborCountriesName = new ArrayList<>();

		for (Country country : countryList) {
			String countryName = country.getCountryName();
			countriesAssignedToPlayer.add(countryName);
			if (country.getCountryName().equals(source)) {
				for( Country country1 :  country.getNeighboursOfCountry()){
					neighborCountriesName.add(country1.getCountryName());
				}
			}
		}

		Iterator<String> it = neighborCountriesName.iterator();
		while (it.hasNext()) {
			String country = it.next();
			if (!countriesAssignedToPlayer.contains(country)){
				it.remove();
			}
		}

		if(neighborCountriesName!=null) {
			neighborCountriesName.removeAll(connectedOwnCountries);
			connectedOwnCountries.addAll(neighborCountriesName);
		}

		Iterator<String> rec = neighborCountriesName.iterator();
		while (rec.hasNext()) {
			String country = rec.next();
			getConnectedCountries(country, countryList);
		}
		
		System.out.println("1. Neighbouring Countries:"+neighborCountriesName.toString());
		System.out.println("1. Player's Countries:"+countriesAssignedToPlayer.toString());
		finalCOuntries.addAll(connectedOwnCountries);
		connectedOwnCountries.clear();
		gamePhaseDetails.add("Connected Countries: "+ finalCOuntries.toString());
		return finalCOuntries;
	}


	/**
	 * This method recursively explores all the nodes connected to a country and returns the neighboring countries.
	 * @param source source countries
	 * @param countryList list of countries
	 *
	 */
	public void getConnectedCountries(String source, ArrayList<Country> countryList) {
		System.out.println("source Country Name :" + source);

		ArrayList<String> countriesAssignedToPlayer = new ArrayList<String>();
		ArrayList<String> neighborCountriesName = new ArrayList<String>();

		for (Country country : countryList) {
			String countryName = country.getCountryName();
			countriesAssignedToPlayer.add(countryName);
			if (country.getCountryName().equals(source)) {
				for( Country country1 :  country.getNeighboursOfCountry()){
					neighborCountriesName.add(country1.getCountryName());
				}
			}
		}

		Iterator<String> it = neighborCountriesName.iterator();
		while (it.hasNext()) {
			String country = it.next();
			if (!countriesAssignedToPlayer.contains(country)||country.equals(initialSourceCountry)){
				it.remove();
			}
		}

		if(neighborCountriesName!=null) {
			neighborCountriesName.removeAll(connectedOwnCountries);
			connectedOwnCountries.addAll(neighborCountriesName);
		}

		Iterator<String> rec = neighborCountriesName.iterator();
		while (rec.hasNext()) {
			String country = rec.next();
			getConnectedCountries(country, countryList);
		}

		System.out.println("1. Neighbouring Countries:"+neighborCountriesName.toString());
		System.out.println("1. Player's Countries:"+countriesAssignedToPlayer.toString());

	}

	/**
	 * This method returns the number of armies assigned to a specific country.
	 * @param sourceCountryName source country names
	 * @return noOfArmies number of armies
	 */
	public int getArmiesAssignedToCountry(String sourceCountryName) {
		Player currentPlayer = this.getCurrentPlayer();
		int noOfArmies = 0;

		for (Country country : playerCountry.get(currentPlayer)) {
			if (country.getCountryName().equals(sourceCountryName)) {
				noOfArmies = country.getnoOfArmies();
			}
		}
		return noOfArmies;
	}

	//Functions called by addMoveArmyButtonListener() from GameController.
	/**
	 * This method checks whether the source and destination countries belongs to the player and moves the armies from source to destination.
	 * @param source  source as string
	 * @param destination destination countries as string
	 * @param armies count of armies as int
	 * @return true
	 */
	public boolean fortificationPhase(String source, String destination, int armies){
		Player player = getCurrentPlayer();
		gamePhaseDetails.remove(gamePhaseDetails);
		Country sourceCountry = playerCountry.get(player).stream()
				.filter(c -> c.getCountryName().equalsIgnoreCase(source)).findAny().orElse(null);

		Country destinationCountry = playerCountry.get(player).stream()
				.filter(c -> c.getCountryName().equalsIgnoreCase(destination)).findAny().orElse(null);
		
		// player class function
		boolean success = player.fortificationPhase(sourceCountry, destinationCountry, armies);
		gamePhaseDetails.add("Moving "+armies+" armies from " +  source+" to "+ destination);
		if(player.getIsConqured()){
			System.out.println("Conquered");
			Card riskCard = getRiskCardFromDeck();
			
			if(riskCard == null){
				System.out.println("No Cards are Available Right Now.");
			} else {
				player.addCard(riskCard);				
			}

			player.setIsConqured(false);

		}
		notifyObserverslocal(this);
		this.setupNextPlayerTurn();
		setGamePhase(gamePhase.Reinforcement);
		reinforcementPhaseSetup();
		notifyObserverslocal(this);
		return true;
	}
	
	/**
	 * This function skip the fortification phase.
	 */
	public void skipFortification() {
		Player player = getCurrentPlayer();
		if(player.getIsConqured()){
			System.out.println("Conqured");
			Card riskCard = getRiskCardFromDeck();

			if(riskCard == null){
				System.out.println("No Cards Available Right Now.");
			} else {
				player.addCard(riskCard);
			}

			player.setIsConqured(false);

		}
		
		this.setupNextPlayerTurn();
		setGamePhase(gamePhase.Reinforcement);
		reinforcementPhaseSetup();
		notifyObserverslocal(this);
	}

    /**
     * This function initialises the Risk Cards during the startup of the game.
     */

	public void initializeRiskCards(){

		int t=0;
		riskCards.clear();
		int countriesCount = mapModel.getCountryList().size();
		for (int i = 0; i<countriesCount; i++) {
			if (t==0) {
				riskCards.add(Card.Infantry);
			} else if (t==1) {
				riskCards.add(Card.Cavalry);
			} else if (t==2) {
				riskCards.add(Card.Artillery);
			}
			t++;

			if (t == 3) {
				t=0;
			}
		}
		Collections.shuffle(riskCards, new Random());
		System.out.println(riskCards.toString());
	}

    /**
     * This function returns a risk card from the deck when called.
     * @return riskCard
     */
	public Card getRiskCardFromDeck(){
        System.out.println(riskCards.toString());
        if(riskCards.size() > 0){
	        Card riskCard = riskCards.get(0);
	        riskCards.remove(0);
	        return riskCard;
        }
        return null;
    }

    /**
     * This function adds the risk card back to the deck when called.
     * @param riskCard Card
     */
    public void addRiskCardToDeck(Card riskCard){
		if(riskCards.size()>0){
			riskCards.add(riskCard);
		}
	}


	/**
	 * This function performs the exchange operations for the risk cards by assigning armies to the player.
     * @param selectedRiskCards arraylist which contains selected risk cards
	 * @return true if cards exchanges otherwise false
	 */
	public boolean exchangeRiskCards(ArrayList<String> selectedRiskCards){

		if(selectedRiskCards.size() == 3){

			Card firstCard = getCurrentPlayer().getCards().stream().filter( x -> x == Card.valueOf(selectedRiskCards.get(0))).findFirst().orElse(null);

			Card secondCard = getCurrentPlayer().getCards().stream().filter( x -> x == Card.valueOf(selectedRiskCards.get(1))).findFirst().orElse(null);

			Card thirdCard = getCurrentPlayer().getCards().stream().filter( x -> x == Card.valueOf(selectedRiskCards.get(2))).findFirst().orElse(null);

			if(firstCard == null || secondCard == null || thirdCard == null){
				System.out.println("Some Cards doesn't belong to the player.");
			}

			boolean sameRiskCards = (firstCard == secondCard) && (secondCard == thirdCard);
			boolean differentRiskCards = (firstCard != secondCard) && (secondCard != thirdCard) && (firstCard != thirdCard);
			boolean sameAndDifferent = (firstCard != secondCard) || (secondCard != thirdCard) || (firstCard != thirdCard);
			if(sameRiskCards || differentRiskCards || sameAndDifferent){

				getCurrentPlayer().getCards().remove(firstCard);
				getCurrentPlayer().getCards().remove(secondCard);
				getCurrentPlayer().getCards().remove(thirdCard);
				getCurrentPlayer().setInitialArmiesafterExchange(armiesAfterExchange);
				armiesAfterExchange= armiesAfterExchange+5;
				addRiskCardToDeck(firstCard);
				addRiskCardToDeck(secondCard);
				addRiskCardToDeck(thirdCard);
				notifyObserverslocal(this);
				return true;
			} else { System.out.println("Choose the correct combination of the cards."); }
		} else { System.out.println("Choose at least three cards for the exchange."); }
		notifyObserverslocal(this);
		return false;
	}

	//Functions called by other functions within the Game model.

	//Getter and Setter functions of Map. 

	/**
	 * This function is used to get map.
	 * @return mapModel
	 */
	public MapEditorCommands getMap() {
		return mapModel;
	}


	/**
	 *  This is used to set map.
	 * @param map map
	 */
	public void setMap(MapEditorCommands map) {
		this.mapModel = map;
	}



	/**
	 *This is used to get game phase.
	 * @return gamePhase getGamePhase
	 */
	public GamePhase getGamePhase() {
		return gamePhase;
	}


	/**
	 * This is used to set game phase.
	 * @param gamePhase Game phase
	 */
	public void setGamePhase(GamePhase gamePhase) {
		this.gamePhase = gamePhase;
	}


	/**
	 * Getter function for all the Player.
	 * @return playerList list of players
	 */
	public ArrayList<Player> getAllPlayers() {
		return playerList;
	}


	/**
	 * Getter function for Current Player Id.
	 * @return currentPlayerId, current id of player
	 */
	public int getCurrentPlayerId() {
		return currentPlayerId;
	}


	/**
	 * Getter function for Current Player.
	 * @return currentPlayer current player
	 */
	public Player getCurrentPlayer() {
		Player currentPlayer = playerList.get(currentPlayerId);
		return currentPlayer;
	}


	/**
	 * Getter function for getting all the Current Player Countries using current player's ID.
	 * @return playerCountry arraylist
	 */
	public ArrayList<Country> getCurrentPlayerCountries() {
		Player currentPlayer = playerList.get(currentPlayerId);
		return playerCountry.get(currentPlayer);
	}


	/**
	 * Getter function for getting all the Current Player Countries using current player's object.
	 * @param currentPlayer current player 
	 * @return playerCountry player country
	 */
	public ArrayList<Country> getPlayersCountry(Player currentPlayer) {
		return playerCountry.get(currentPlayer);
	}


	/**
	 * Function the sets the next player's turn.
	 */
	public void setupNextPlayerTurn(){
		print.consoleOut("Current Player ID:"+currentPlayerId);
		currentPlayerId++;
		if(currentPlayerId==playerList.size()){
			currentPlayerId = 0;
		}
		if(playerCountry.get(getCurrentPlayer()).size()==0) {
			setupNextPlayerTurn();
		}
	}


	/**
	 * Function the returns the armies of the country of the current player.	 *
	 * @param countryName name of the country
	 */
	public void getCountryArmies(String countryName) {
		int armies_number = 0;
		Player player = this.getCurrentPlayer();
		for(Country country: playerCountry.get(player)){
			if(country.getCountryName().equals(countryName)){
				armies_number = country.getnoOfArmies();
			}
		}
	}


	/**
	 * Function that moves an army from the player's initial army to the country's army.
	 * @param player player 
	 * @param country country
	 */
	public void assignUnassigned(Player player, Country country){
		player.decreasenumberOfInitialArmies();
		country.increaseArmyCount();
	}


	/**
	 * Function that moves an army from the player's reinforcement army to the country's army.
	 * @param player player 
	 * @param country country
	 */
	public void assignReinforcement(Player player, Country country){
		player.decreaseReinforcementArmy();
		country.increaseArmyCount();
	}


	/**
	 * This is the method that notifies all the observers connected to the observable.
	 * @param game game view
	 */
	private void notifyObserverslocal(Game game){
		setChanged();
		notifyObservers(this);
	}

	/**
	 * Returns number of dices for attacking / defending country.
	 *
	 * @param countryName the country name
	 * @param playerStatus the player status
	 * @return Integer
	 */
	public int getMaximumDices(String countryName, String playerStatus) {
		int allowableAttackingArmies = 0;
		if (this.gamePhase ==GamePhase.Attack) {
			// Will also add validation if the attacker is assigned to player or not

			Country c = mapModel.getCountryFromName(countryName);

			if (c != null) {
				allowableAttackingArmies = getCurrentPlayer().getNumberDices(c, playerStatus);
			}
		}
		return allowableAttackingArmies;
	}

	/**
	 * Returns allowable dices for attacking country.
	 * @param countryName the country name
	 * @return Integer
	 */
	public ArrayList<String> getOthersNeighbouringCountriesOnly(String countryName) {
		ArrayList<String> allowableAttackingArmies = new ArrayList<String>();
		if (this.gamePhase ==GamePhase.Attack) {
			// Will also add validation if the attacker is assigned to player or not

			Country c = mapModel.getCountryFromName(countryName);
			Player currentPlayer = this.getCurrentPlayer();
			ArrayList<Country> countryList = playerCountry.get(currentPlayer);

			if (c != null) {
				allowableAttackingArmies = c.getNeighboursString();
				for (Country country : countryList) {
					String countryName1 = country.getCountryName();
					allowableAttackingArmies.remove(countryName1);
				}

			}
		}
		return allowableAttackingArmies;
	}

	/**
	 * Method for  attack phase where attack will handled.
	 * @param attackerCountry the attacker country
	 * @param defenderCountry the defender country
	 * @param attackerDiceCount the attacker dice count
	 * @param defendergDiceCount the defenderg dice count
	 * @return true, if attack done
	 */
	public Boolean attackPhase(String attackerCountry, String defenderCountry, int attackerDiceCount, int defendergDiceCount) {
		
		Country attCountry = mapModel.getCountryFromName(attackerCountry);
		Country defCountry = mapModel.getCountryFromName(defenderCountry);
		gamePhaseDetails.add(attackerCountry+" is attacking the "+ defenderCountry);
		if (attCountry == null || defCountry == null) {
			return false;
		}

		if (defCountry.getnoOfArmies() < defendergDiceCount) {
			gamePhaseDetails.add("Defender doesn't have sufficiant armies");
			return false;
		}
		Player defenderPlayer = playerList.stream().filter(p -> p.getPlayerId()==defCountry.getPlayerId())
				.findAny().orElse(null);

		if (defenderPlayer == null) {
			return false;
		}

		getCurrentPlayer().attackPhaseActions(defenderPlayer, attCountry, defCountry, attackerDiceCount, defendergDiceCount,playerCountry,gamePhaseDetails);

		//playerCountry;
		if (isMapConcured()) {
			FinishView finish = new FinishView();
			finish.Exchange(getCurrentPlayer().getPlayerName());
			System.out.println("Congratulation!"+this.getCurrentPlayer().getPlayerName() + ": You Win.");
			gamePhaseDetails.add("Congratulation!"+this.getCurrentPlayer().getPlayerName() + ": You Win.");
		} else if (!checkAttackPossible()) {
			gamePhaseDetails.add("Attack not possible.");
			updateGame();
		}
		getCurrentPlayer().setConcuredContinents(mapModel.getContinentList());
		notifyObserverslocal(this);
		if(defCountry.getPlayerId()==attCountry.getPlayerId()&& attCountry.getnoOfArmies()>1) {
			return true;
		}

		return false;
	}
	
	/**
	 * Checks if is map concurred.
	 * @return true, if is map concurred
	 */
	public boolean isMapConcured() {
		if(mapModel.getCountryList().size() == playerCountry.get(this.getCurrentPlayer()).size()) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * method to get countries from the attackers country where number of armies are getter than 1.
	 * @return attackerCountry arraylist of attacker country
	 */
	public ArrayList<String> getAttackPossibleCountries() {
		ArrayList<String> attackerCountry = new ArrayList<String>();
		ArrayList<Country> countryList = this.getCurrentPlayerCountries();
		for (int i = 0; i < countryList.size(); i++) {
			Country temp_cname = countryList.get(i);
			if (temp_cname.getnoOfArmies()>1) {
				attackerCountry.add(temp_cname.getCountryName());
			}
		}
		return attackerCountry;

	}

	/**
	 * This method is to check current user can attack or not.
	 * @return boolean
	 */
	public boolean checkAttackPossible() {
		ArrayList<String> attackerPossibleCountries = getAttackPossibleCountries();
		if (attackerPossibleCountries.size() == 0) {
			return false;
		}else {
			for (String countryName : attackerPossibleCountries) {
				ArrayList<String> neighborCountries = getOthersNeighbouringCountriesOnly(countryName);
				if (neighborCountries.size() > 0) {
					return true;
				}
			}
			return false;
		}
	}


	/**
	 * Method for performing All out attack phase.
	 * @param attackerCountry the attacker country
	 * @param defenderCountry the defender country
	 * @return true, if attack phase out
	 */
	public Boolean attackAllOutPhase(String attackerCountry, String defenderCountry) {

		Country attCountry = mapModel.getCountryFromName(attackerCountry);
		Country defCountry = mapModel.getCountryFromName(defenderCountry);


		if (attCountry == null || defCountry == null) {
			return false;
		}

		while ((attCountry.getPlayerId()!=defCountry.getPlayerId()) && attCountry.getnoOfArmies() > 1) {
			int attackerDiceCount = this.getMaximumDices(attackerCountry, "Attacker");
			int defenderDiceCount = this.getMaximumDices(defenderCountry, "Defender");

			attackPhase(attackerCountry, defenderCountry, attackerDiceCount, defenderDiceCount);
		}
		notifyObserverslocal(this);
		if(defCountry.getPlayerId()==attCountry.getPlayerId()&& attCountry.getnoOfArmies()>1) {
			return true;
		}
		return false;
	}

	/**
	 * move Armies after attack.
	 * @param attackersCountry Attacker country
	 * @param atteckersNewCountry Attacker new country
	 * @param attackerMoveArmies Attacker move armies
	 */
	public void moveArmies(Country attackersCountry, Country atteckersNewCountry, int attackerMoveArmies) {
		attackersCountry.decreaseArmyCount(attackerMoveArmies);
		atteckersNewCountry.increaseArmyCount(attackerMoveArmies);
		notifyObserverslocal(this);

	}
	


	/**
	 * Gets the percentage of map controlled by every player.
	 * @return the percentage of map controlled by every player
	 */
	public HashMap<Integer, Float> getPercentageOfMapControlledByEveryPlayer() {
		float totalNumberOfCountries = 0;
		ArrayList<Continent> allContinents = mapModel.getContinentList();
		for (Continent continent : allContinents) {
			ArrayList<Country> country = continent.getCountryList();
			totalNumberOfCountries = totalNumberOfCountries + country.size();
		}

		// storing  the percentage in a hashmap with the Player ID.
		HashMap<Integer, Float> mapPercentageStoredInMap = new HashMap<Integer, Float>();
		for (Player player : playerList) {

			// get size of player country list
			int playerCountries = playerCountry.get(player).size();

			// find percentage
			float percentage = (playerCountries / totalNumberOfCountries) * 100;			

			// get player id and percentage and then put in map
			int playerId = player.getPlayerId();			
			mapPercentageStoredInMap.put(playerId, percentage);
		}
		return mapPercentageStoredInMap;

	}


    /**
     * Get the number of continents and their name by each player
     * @return hashMap for a player and continent
     */
    public HashMap<Integer, String> getContinentsControlledByEachPlayer() {
		HashMap<Integer, String> continentsOfPlayer = new HashMap<Integer, String>();
		ArrayList<String> nameOfTheContinent = new ArrayList<>();
		String numberAndName= null;

		for (Player player : this.playerList) {
			int numberOfContinents = player.getConquerdContinents().size();
			for (Continent continentName: player.getConquerdContinents()) {
				nameOfTheContinent.add(continentName.getContinentName());
				numberAndName = "(" + numberOfContinents + "): "+nameOfTheContinent;
			}
			continentsOfPlayer.put(player.getPlayerId(), numberAndName);
			numberAndName="";
			nameOfTheContinent.clear();
		}
		return continentsOfPlayer;
	}

	/**
	 * Gets list of players.
	 * @param countriesListOfPlayer Countries list of players
	 * @return countriesListString Countries list
	 */
	public ArrayList<String> countryListStringOfPlayer(ArrayList<Country> countriesListOfPlayer) {
		ArrayList<String> countriesListString = new ArrayList<>();
		for(Country countryForAdding : countriesListOfPlayer){
			countriesListString.add(countryForAdding.getCountryName());
		}
		return countriesListString;
	}
	

	
	/**
	 * This method is used to get the number of armies for each player.
	 * @return numberOfArmies Number of armies
	 */
	public HashMap<Integer, Integer> getNumberOfArmiesForEachPlayer() {
		HashMap<Integer, Integer> numberOfArmies = new HashMap<Integer, Integer>();
		for (Player player : this.playerList) {
			for (Country country : player.getAssignedListOfCountries()) {
				int totalArmies = country.getnoOfArmies();
				if(numberOfArmies.containsKey(player.getPlayerId())) 
				{
					totalArmies += numberOfArmies.get(player.getPlayerId());
				}
				numberOfArmies.put(player.getPlayerId(), totalArmies);
			}
		}
		return numberOfArmies;
	}

	/**
	 * Get all the players and countries.
	 * @return playerCountry Player country
	 */
	public HashMap<Player, ArrayList<Country>> playerandCountries(){
		return playerCountry;
	}

	/**
	 * the method is used to get the updated phase while the game is being played
	 * @return gamePhaseDetail
	 */
	public ArrayList<String> getGamePhaseDetails() {
		return gamePhaseDetails;
	}

	
	/**
	 * update the reinforcement value.
	 */
	public void updateReinforcementValue() {
		reinforcementPhaseSetup();
		notifyObserverslocal(this);
	}
	
	public boolean isAttackerDefenderValid(Country attCountry,Country  defCountry,int defendergDiceCount) {
		if (attCountry == null || defCountry == null) {
			return false;
		}

		if (defCountry.getnoOfArmies() < defendergDiceCount) {
			gamePhaseDetails.add("Defender doesn't have sufficiant armies");
			return false;
		}
		Player defenderPlayer = playerList.stream().filter(p -> p.getPlayerId()==defCountry.getPlayerId())
				.findAny().orElse(null);

		if (defenderPlayer == null) {
			return false;
		}
		return true;
	}
}
