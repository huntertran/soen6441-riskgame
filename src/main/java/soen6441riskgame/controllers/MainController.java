package soen6441riskgame.controllers;



import soen6441riskgame.controllers.MapController;
import soen6441riskgame.enums.PrintConsoleAndUserInput;
import soen6441riskgame.views.CardView;
import soen6441riskgame.views.CardView;
import soen6441riskgame.views.CardView;
// TODO: Auto-generated Javadoc
/**
 * This is a main class to run the game.
 *
 *
 * @version 1.0.0
 */
public class MainController {
	
	/**
	 * This is a main method to run the game.
	 * This function is used to enter the user input and call the functions to create or edit the map, start, load the game
	 * and user can exit if he wants to exit the game.
	 * This function also displays the error message to select valid user input.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		startMenu();	
	}

	
	/**
	 * This method is used to choose an option for the start menu.
	 */
	public static void startMenu() {
		MapController mapController = new MapController();
		PrintConsoleAndUserInput print = new PrintConsoleAndUserInput();
		GameController gameController = new GameController();

		int selectMainMenuOption = 0;
		boolean checkMapStatus = false;
		do {
			selectMainMenuOption = displaymainMenu();
			switch (selectMainMenuOption)
			{
			case 1:
				mapController.generateMap();
				break;
			case 2:
				gameController.initializeMap();
				break;
			case 3:
				print.consoleErr("Thank you for playing this Game, We hope you had fun.");
				System.exit(0);
			default :
				System.err.println("\n\t Error! Select option from the menu list (1 to 5):");
				break;
			}

		}
		while (selectMainMenuOption != 5);
		System.exit(0);		
	}
	
	/**
	 * This is the method for Displaying main menu for game. 
	 * This function is used to show the user input to create or edit the map, start, load the game
	 * and user can exit if he wants to exit the game.
	 * @return userIntInput
	 */
	public static int displaymainMenu() {
		PrintConsoleAndUserInput print = new PrintConsoleAndUserInput();
		print.consoleOut("\t WELCOME\t");
		print.consoleOut("\tRISK GAME\t");
		print.consoleOut("1.Map Editor");
		print.consoleOut("2.Start Playing the Game");
		print.consoleOut("3.Exit Game");
		print.consoleOut("\nENJOY");
		print.consoleOut("Please Enter Your Choice from the list: ");
		return print.userIntInput();
	}
}
