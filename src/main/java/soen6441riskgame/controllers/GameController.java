package soen6441riskgame.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import soen6441riskgame.models.Country;
import soen6441riskgame.commands.Game;
import soen6441riskgame.models.Player;

import soen6441riskgame.views.CardView;
import soen6441riskgame.views.WorldDominationView;
import soen6441riskgame.commands.MapEditorCommands;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.enums.PrintConsoleAndUserInput;

// TODO: Auto-generated Javadoc
/**
 * Game Controller initializes the game by calling the GameCommands.
 *
 * Class that achieves the action listener for the user input
 * 
 * @version 1.0.0
 */
public class GameController {

	/** The game. */
	Game game;

	WorldDominationView worldDominationViewObserver;

	/** The player. */
	Player player;

	/** The map model. */
	MapEditorCommands mapModel = new MapEditorCommands();

	/** The print. */
	PrintConsoleAndUserInput print = new PrintConsoleAndUserInput();

	/** The userinput. */
	Scanner userinput = new Scanner(System.in);

	/**
	 * This function is going to initializing the map by taking user input.
	 */
	public void initializeMap() {
		print.listofMapsinDirectory();
		print.consoleOut("\nWELCOME, Please Enter Map Name to load Map file:\n");
		String mapPath = mapModel.getMapNameByUserInput();
		File tempFile = new File(mapPath);
		boolean exists = tempFile.exists();
		if (exists) {
			mapModel.readMapFile(mapPath);
			mapModel.printMapValidOrNot();
			if (!mapModel.checkMapIsValid()) {
				// print.consoleErr("****Error!! Invalid map name. Please try again with the
				// valid name****");
			} else {
				initializeGame();
			}
		} else {
			print.consoleErr("File not found!!!. Please enter the correct name of map.");
		}
	}

	/**
	 * This method is setting up the board and game model It is intializing the
	 * observer for the gui also It is taking the the input from the user for
	 * creating number of players.
	 */
	public void initializeGame() {
		int j = 1;
		game = new Game(mapModel);

		worldDominationViewObserver = new WorldDominationView();
		game.addObserver(worldDominationViewObserver);

		// game.addObserver(cardView);
		print.consoleOut("\nPlease Enter the number of Players between 3-5:");
		int playerCount = Integer.parseInt(userinput.nextLine());

		if (playerCount < 3 || playerCount > 5) {
			print.consoleErr(" Error!!! Please enter the number of Players between 3-5. ");
		} else {
			for (int i = 0; i < playerCount; i++) {
				print.consoleOut("\nPlease Enter the name of Player " + j);
				String name = userinput.nextLine();
				Player player = new Player(i, name);
				game.addPlayer(player);
				j++;
			}
			game.startGame();
			game.initializeRiskCards();

			callListenerOnView();
		}
	}

	/**
	 * This function is used to call the listener functions.
	 * 
	 */
	private void callListenerOnView() {

		addActionListenerForWorldDominationView();

		skipExchangeListener();
		exchangeButtonListener();

	}

	/**
	 * This method is going to assign armies to the specific countries in initial
	 * phase and in reinforcement phase.
	 */
	public void mouseClicked(MouseEvent e) {
		JLabel jLabel = (JLabel) e.getSource();
		String country = jLabel.getToolTipText();
		if (game.getGamePhase() == GamePhase.Startup || game.getGamePhase() == GamePhase.Reinforcement) {
			game.addingCountryArmy(country);
		}
	}

	/**
	 * Add action listener for the world domination view.
	 *
	 */
	public void addActionListenerForWorldDominationView() {

		game.dominationViewOn = true;
		DecimalFormat countryPercentFormat = new DecimalFormat(".####");
		ArrayList<Player> playerList = game.getAllPlayers();

		// Get players from the above arraylist and add in the other arraylist.
		int x = 0;
		ArrayList<String> newPlayerNameList = new ArrayList<>();
		for (Player playerData : playerList) {
			String name = playerData.getPlayerName();
			newPlayerNameList.add(name);
			x++;
		}

		// print Player name in tabular columns(Ist row heading)
		String[] playerNamesInTableColumns = new String[newPlayerNameList.size()];
		int y = 0;
		for (String nameOfPlayer : newPlayerNameList) {

			playerNamesInTableColumns[y] = "Player name : " + nameOfPlayer;
			y++;
		}
		int size = newPlayerNameList.size();

		// Get the Percentage of the map controlled by every player
		Float[] mapPercentage = new Float[size];
		HashMap<Integer, Float> findPercentageOfMap = game.getPercentageOfMapControlledByEveryPlayer();
		int z = 0;
		for (Map.Entry<Integer, Float> entry : findPercentageOfMap.entrySet()) {
			// System.out.println(entry.getKey()+" : "+entry.getValue());
			float value = entry.getValue();
			mapPercentage[z] = value;
			z++;
		}

		// Get the continents controlled by every player
		String[] continentsAcquired = new String[size];
		HashMap<Integer, String> findContinentsAcquired = game.getContinentsControlledByEachPlayer();
		int l = 0;
		for (Map.Entry<Integer, String> entry : findContinentsAcquired.entrySet()) {
			String value = entry.getValue();
			continentsAcquired[l] = value;
			l++;
		}

		// Get the number of armies owned by each player
		int[] numberOfArmies = new int[size];
		HashMap<Integer, Integer> armiesMap = game.getNumberOfArmiesForEachPlayer();
		int i = 0;
		for (Map.Entry<Integer, Integer> entry : armiesMap.entrySet()) {
			int value = entry.getValue();
			numberOfArmies[i] = value;
			i++;
		}

		// To print data in a table
		String[][] dataInTableRows = new String[3][size];
		for (int percentColumn = 0; percentColumn < dataInTableRows[0].length; percentColumn++) {
			String formattedPercent = countryPercentFormat.format(mapPercentage[percentColumn]);
			dataInTableRows[0][percentColumn] = formattedPercent + " %";
		}
		for (int continentColumn = 0; continentColumn < dataInTableRows[0].length; continentColumn++) {
			dataInTableRows[1][continentColumn] = continentsAcquired[continentColumn];
		}
		for (int armyColumn = 0; armyColumn < dataInTableRows[0].length; armyColumn++) {
			dataInTableRows[2][armyColumn] = Integer.toString(numberOfArmies[armyColumn]);
		}

		worldDominationViewObserver.createJframeForWorldDominationView(dataInTableRows, playerNamesInTableColumns);
	}

	/**
	 * This function is used to exchange button listener.
	 */
	public void exchangeButtonListener() {
		CardView.exchange_actionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (CardView.list_cardsOwnedByThePlayer.getSelectedValuesList() != null
						&& CardView.list_cardsOwnedByThePlayer.getSelectedValuesList().size() > 0) {
					// This list holds the cards selected by the user
					ArrayList<String> selectedCards = (ArrayList<String>) CardView.list_cardsOwnedByThePlayer
							.getSelectedValuesList();

					boolean success = game.exchangeRiskCards(selectedCards);
					if (success) {
						CardView.closeTheWindow();

						game.updateReinforcementValue();
					} else {
						// Nothing implemented
					}
				}
			}
		});
	}

	/**
	 * This function is going to close/skip if number of card is less than 5.
	 */
	public void skipExchangeListener() {
		CardView.exit_actionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int temp_forNumberOfCardsPlayerHolds = (game.getCurrentPlayer().getCards()).size();
				if (temp_forNumberOfCardsPlayerHolds >= 5) {
					JOptionPane.showMessageDialog(null, "You Cannot skip Exchange. Perform the Exchange operation!");
				} else {

					CardView.closeTheWindow();
				}
			}
		});
	}

}