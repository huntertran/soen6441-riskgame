package soen6441riskgame.maps;

import static org.junit.Assert.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

import soen6441riskgame.commands.Game;
import soen6441riskgame.commands.MapEditorCommands;
import soen6441riskgame.controllers.GameController;
import soen6441riskgame.enums.GameMode;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.enums.PrintConsoleAndUserInput;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;


// TODO: Auto-generated Javadoc
/**
 * This test Class deals with the game model class. It will tests the game play by executing the game automatically . 
 * 
 * @version 1.0.0
 *
 */
public class GameTest {
	
	/** The mapEditor Commands. */
	MapEditorCommands mapModel;
	
	/** The tournament map model. */
	MapEditorCommands tournamentMapModel;
	
	/** The game object. */
	Game gameObject;
	
	/** The tournament game. */
	Game tournamentGame;
	
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
	
	/** The Tournament player 1. */
	Player tourPlayer1;
	
	/** The Tournament player 2. */
	Player tourPlayer2;
	
	/** The Tournament player 3. */
	Player tourPlayer3;
	
	/** The Tournament player 4. */
	Player tourPlayer4;
	
	/** The id. */
	int id =0;
	
	/**
	 * Initializing the values and object to start a game .
	 */
	@Before
	public void setUp(){
		mapModel = new MapEditorCommands();
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

		
		 
		// Randomly increase army for the country of player

		while (gameObject.getGamePhase() == GamePhase.Startup) {
			
			ArrayList<Country> playerCountries = gameObject.getCurrentPlayerCountries();
			gameObject.addingCountryArmy(playerCountries.get(id).getCountryName());

			id++;
			if(id==8) {
				id = 0;
			}
		}
	}	
	
	/**
	 * Test method for checking current phase.
	 */
	@Test
	public void testCurrentPhaseIsReinforcement() {
		assertEquals(GamePhase.Reinforcement, gameObject.getGamePhase());
	}

	/**
	 * Test the calculation of the number of armies during the reinforcement phase.
	 */
	@Test
	public void testCalculationOfReinforcementArmies() {
		Player player = gameObject.getCurrentPlayer();
		int a = player.calculationForNumberOfArmiesInReinforcement(gameObject.playerandCountries(),mapModel.getContinentList());
		assertEquals(3, a);
	}

	/**
	 * Test the current player object is same or not.
	 */
	@Test
	public void testIsItPlayerObjectSame() {
		Player player = gameObject.getCurrentPlayer();
		assertEquals(player1, player);
	}

	/**
	 * Test the current map file is same as get imported.
	 */
	@Test
	public void testIsItMapObjectSame() {
		MapEditorCommands mapTest = gameObject.getMap();
		assertEquals(mapModel, mapTest);
	}
	
	
	
	/**
	 * Test If attack is possible from the current player.
	 */
	@Test
	public void isAttackPossible() {
		if (gameObject.getAttackPossibleCountries().size() == 0) {
			assertFalse(gameObject.checkAttackPossible());
		}else {
			for (String countryName : gameObject.getAttackPossibleCountries()) {
				ArrayList<String> neighborCountries = gameObject.getOthersNeighbouringCountriesOnly(countryName);
				if (neighborCountries.size() > 0) {
					assertTrue(gameObject.checkAttackPossible());
				}
			}
		}
	}
	
	/**
	 * Test the fortification phase.
	 */
	@Test
	public void checkFortification() {
		Country selectedCountry;
		Country destinationCountry;
		GamePhase A = gameObject.getGamePhase();
		gameObject.setGamePhase(GamePhase.Fortification);
		if(gameObject.getCurrentPlayer().getAssignedListOfCountries().size()>1) {
			selectedCountry = gameObject.getCurrentPlayer().getAssignedListOfCountries().get(0);
			destinationCountry = gameObject.getCurrentPlayer().getAssignedListOfCountries().get(1);
			if (selectedCountry.getnoOfArmies()<=1) {
				selectedCountry.increaseArmyCount();
			}
			int a = selectedCountry.getnoOfArmies()-1;
			int b = destinationCountry.getnoOfArmies()+1;
			gameObject.fortificationPhase(selectedCountry.getCountryName(), destinationCountry.getCountryName(), 1);
			assertEquals(selectedCountry.getnoOfArmies(), a);
			assertEquals(destinationCountry.getnoOfArmies(), b);
		}

		gameObject.setGamePhase(A);
		
	}
	

	/**
	 * This test case is used to test if the game is saved or not with the current time.
	 * Test game is saved or not.
	 */
	@Test
	public void testGameIsSavedOrNot(){
		try {
					
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy_hhmm");
			String saveGameFileWithTime = dateFormat.format(cal.getTime());
			
			String filePath = ".\\src\\main\\java\\soen6441riskgame\\savedGames\\" + saveGameFileWithTime+ ".txt";
			FileInputStream fileInput = new FileInputStream(filePath);
			ObjectInputStream objectInput =new ObjectInputStream(fileInput);
			Game testGameObject=(Game) objectInput.readObject();
			objectInput.close();
			fileInput.close();
			
			
			ArrayList<Player> expectedPlayerList = testGameObject.getAllPlayers(); // expected
			int getSizeOfExpectedList = expectedPlayerList.size();
			
			ArrayList<Player> actualPlayerList = gameObject.getAllPlayers(); // actual
			int getSizeOfActualList = actualPlayerList.size();
			
			assertEquals(getSizeOfExpectedList,getSizeOfActualList);
			
			// check for the army count from expected and actual list
			int expectedArmyCountSize[] = new int[getSizeOfExpectedList];
			int actualArmyCountSize[] = new int[getSizeOfActualList];
			int i = 0;
			for(Player expectedList : expectedPlayerList) {
				expectedArmyCountSize[i] = expectedList.getNumberOfReinforcedArmies();
				i++;
			}
			
			i = 0;
			for(Player actualList : actualPlayerList) {
				actualArmyCountSize[i] = actualList.getNumberOfReinforcedArmies();
				i++;
			}
			
			for (i = 0; i < actualArmyCountSize.length; i++) {
				assertEquals(expectedArmyCountSize[i], actualArmyCountSize[i]);
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	
	}
	
