package soen6441riskgame.maps;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;

import soen6441riskgame.commands.Game;
import soen6441riskgame.commands.MapEditorCommands;
import soen6441riskgame.enums.Colors;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.enums.PrintConsoleAndUserInput;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;

import org.junit.Test;
// TODO: Auto-generated Javadoc


/**
 * Test class that tests the correctness in values in Player class that'll be used in further operations .
 *
 *
 * @version 1.0.0
 */
public class PlayerTest {

	/** Declaring objects. */

	Player objPlayertest;

	/** The map model. */
	MapEditorCommands mapModel;

	/** The game object. */
	Game gameObject;

	/** The player 1. */
	Player player1;

	/** The player 2. */
	Player player2;

	/** The player 3. */
	Player player3;

	/** The player 4. */
	Player player4;

	/** The player 5. */
	Player player5;

	/** The country 2. */
	/* The Country Object*/
	Country country1, country2;


	/** The id. */
	int id =0;

	/**
	 * Initializing objects and values for the test cases.
	 * @throws Exception if it is not setting the values at the starting
	 */

	@Before
	public void setUp() throws Exception{
		objPlayertest=new Player(1,"Benjamin");
		objPlayertest.setColor(Colors.RED);
		objPlayertest.setNumberOfInitialArmies(5);
		objPlayertest.setNumberOfReinforcedArmies(3);
		country1 = new Country(1,"Canada");
		country2 = new Country(2,"China");
		country1.setnoOfArmies(3);
		country2.setnoOfArmies(2);
		mapModel = new MapEditorCommands();
		
		
		//		mapModel.readMapFile("src/main/java/soen6441riskgame/mapFiles/World.map");
		mapModel.readMapFile(PrintConsoleAndUserInput.getMapDir()+"World.map");
		gameObject = new Game(mapModel);
		player1 = new Player(0,"Ben");
		player2 = new Player(1,"Tuan");
		player3 = new Player(2,"Bharti");
		player4 = new Player(3,"Tj");
		player5 = new Player(4,"Roger");
		gameObject.addPlayer(player1);
		gameObject.addPlayer(player2);
		gameObject.addPlayer(player3);
		gameObject.addPlayer(player4);
		gameObject.addPlayer(player5);
		gameObject.startGame();

		while (gameObject.getGamePhase() == GamePhase.Startup) {
			// Randomly increase army for the country of player
			ArrayList<Country> playerCountries = gameObject.getCurrentPlayerCountries();

			gameObject.addingCountryArmy(playerCountries.get(id).getCountryName());

			id++;
			if(id==8) {
				id = 0;
			}
		}

	}


	/**
	 * test method to check if it returns correct player id.
	 */

	@Test
	public void testGetPlayerid() {
		assertEquals(1,objPlayertest.getPlayerId());
	}


	/**
	 * test method to check the initial number of armies.
	 */

	@Test
	public void testGetNumberOfInitialArmies() {
		assertEquals(5, objPlayertest.getNumberOfInitialArmies());
	}


	/**
	 * test method to check the reinforced number of armies.
	 */

	@Test 
	public void testGetNumberOfReinforcedArmies() {
		assertEquals(3, objPlayertest.getNumberOfReinforcedArmies());

	}


	/**
	 * test method to check if it returns correct player name.
	 */

	@Test
	public void testGetPlayername() {
		assertEquals("Benjamin",objPlayertest.getPlayerName());
	}


	/**
	 * test method to check if the function getColor() fetches the correct colors.
	 */

	@Test 
	public void testGetColor() {
		assertEquals(Colors.RED, objPlayertest.getColor());
	}


	/**
	 * test method to check if it returns correct color for every player.
	 */

	@Test
	public void testGetPlayerColor() {
		assertEquals(Colors.GREEN,Player.getPlayerColor(2));
		assertEquals(Colors.MAGENTA,Player.getPlayerColor(5));
	}


	/**
	 * test method to check if the method implements the decrease the initial army operation correctly.
	 */

	@Test
	public void testDecreaseNumberOfInitialArmies() {
		objPlayertest.decreasenumberOfInitialArmies();
		System.out.println("Decrease Initial Army:"+objPlayertest.getNumberOfInitialArmies());
	}


	/**
	 * test method to check if the method implements the decrease the reinforced army operation correctly.
	 */

	@Test
	public void testDecreaseNumberOfReinforcedArmies() {
		objPlayertest.decreaseReinforcementArmy();
		System.out.println("Decrease Reinforced Army:"+objPlayertest.getNumberOfReinforcedArmies());
	}

	/**
	 * Test check fortification condition.
	 */
	@Test
	public void testCheckFortificationCondition(){
		assertTrue(objPlayertest.checkFortificationCondition(country1,country2,country2.getnoOfArmies()));

	}

	/**
	 * This is used to test Move armies after attack.
	 */
	@Test
	public void moveArmiesAfterAttackTest(){
		Player currentPlayer = gameObject.getCurrentPlayer(); 
		ArrayList<String> attackingCountryList = gameObject.getAttackPossibleCountries();
		ArrayList<String> attackedCountryList;
		Country attackingCountry,defendingCountry;
		int attackingCountryArmyCount, defendingCountryArmyCount;

		Player defenderPlayer; 
		currentPlayer.setIsConqured(true);	    
		for(String attackingCountryName:attackingCountryList){
			attackedCountryList = gameObject.getOthersNeighbouringCountriesOnly(attackingCountryName);
			attackingCountry = mapModel.getCountryFromName(attackingCountryName);
			attackingCountry.setnoOfArmies(5);
			attackingCountryArmyCount = attackingCountry.getnoOfArmies();

			for(String attackedCountryName : attackedCountryList){
				defenderPlayer = gameObject.getAllPlayers().stream().filter(p -> p.getAssignedListOfCountries().contains(attackedCountryName))
						.findAny().orElse(null);				    	  
				defendingCountry = mapModel.getCountryFromName(attackedCountryName);
				defendingCountry.setnoOfArmies(1);
				defendingCountryArmyCount = defendingCountry.getnoOfArmies();
				defendingCountry.increaseArmyCount(3);
				attackingCountry.decreaseArmyCount(3);
				assertEquals(defendingCountryArmyCount+3, defendingCountry.getnoOfArmies());
				assertEquals(attackingCountryArmyCount-3, attackingCountry.getnoOfArmies());	
				break;	    	
			}
			break;
		}	    

	}

}