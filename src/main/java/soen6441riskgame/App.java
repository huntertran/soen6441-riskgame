package soen6441riskgame;

import java.io.IOException;
import java.util.Scanner;

import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.models.commands.MapEditorCommands;
import soen6441riskgame.controllers.GameController;
import soen6441riskgame.controllers.MapController;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.ModelCommandsPair;


/**
 * This is a main class to run the game.
 *
 *
 *
 */


/**
	 * This is a main method to run the game.
	 * This function is used to enter the user input and call the functions to create or edit the map, start, load the game
	 * and user can exit if he wants to exit the game.
	 * This function also displays the error message to select valid user input.
	 *
	 * @param args the arguments
	 */

public final class App {
    private App() {
    }


    /**
	 * This is the method for entering or initializing the main commands for game.
	 * This function is used to show the action entered and the user input to create or edit the map, start, load the game
	 *
	 * @return userIntInput
	 */
    public static void main(String[] args) {
        System.out.println("SOEN 6441 - Risk Domination game");

        if (args.length == 0) {
            runFromBeginning();
        } else {
            String commands = String.join(" ", args);
            ModelCommands cmds = new ModelCommands(commands);
            jumpToCommand(cmds);
        }
    }

    public static void jumpToCommand(ModelCommands args) {
        String command = args.cmd;

        MapController mapController = new MapController();
        GameController gameController = new GameController();
        try {
            switch (command) {
                case MapEditorCommands.EDITCONTINENT: {
                    for (ModelCommandsPair sub : args.subRoutine) {
                        mapController.editContinent(sub.toStringArray());
                    }
                    break;
                }
                case MapEditorCommands.EDITCOUNTRY: {
                    for (ModelCommandsPair sub : args.subRoutine) {
                        mapController.editCountry(sub.toStringArray());
                    }
                    break;
                }
                case MapEditorCommands.EDITNEIGHBOR: {
                    for (ModelCommandsPair sub : args.subRoutine) {
                        mapController.editNeighbor(sub.toStringArray());
                    }
                    break;
                }
                case MapEditorCommands.SHOWMAP: {
                    mapController.showMap();
                    break;
                }
                case MapEditorCommands.SAVEMAP: {
                    try {
                        mapController.saveMap(args.regularCommands.get(0));
                    } catch (IOException e) {
                        System.out.println("Error: " + e.getClass().getName());
                    }
                    break;
                }
                case MapEditorCommands.EDITMAP: {
                    try {
                        mapController.editMap(args.regularCommands.get(0));
                    } catch (IOException e) {
                        System.out.println("Error: " + e.getClass().getName());
                    }
                    break;
                }
                case MapEditorCommands.VALIDATEMAP: {
                    mapController.validateMap();
                    break;
                }
                case MapEditorCommands.LOADMAP: {
                    try {
                        mapController.loadMap(args.regularCommands.get(0));

                        // if (mapController.isMapValid()) {
                        //     mapController.showMap();
                        // }
                    } catch (IOException e) {
                        System.out.println("Error: " + e.getClass().getName());
                    }

                    break;
                }
                case GameCommands.GAMEPLAYER: {
                    for (ModelCommandsPair sub : args.subRoutine) {
                        gameController.handleGamePlayerCommand(sub.toStringArray());
                    }
                    break;
                }
                case GameCommands.POPULATECOUNTRIES: {
                    gameController.populateCountries();
                    gameController.initPlayersUnplacedArmies();
                    break;
                }
                case GameCommands.PLACEARMY: {
                    // gameController.startRoundRobinPlayers();
                    gameController.handlePlaceArmyCommand(args.regularCommands.get(0));
                    break;
                }
                case GameCommands.PLACEALL: {
                    gameController.handlePlaceAllCommand();
                    break;
                }
                case GameCommands.REINFORCE: {
                    boolean isReinforcementEntered = gameController.enterReinforcement();

                    if (isReinforcementEntered) {
                        gameController.handleReinforceCommand(args.regularCommands.toArray(new String[args.regularCommands.size()]));
                    }

                    break;
                }
                case GameCommands.EXCHANGECARDS:{
                    gameController.exchangeCard(args.regularCommands.toArray(new String[args.regularCommands.size()]));
                }
                case GameCommands.ATTACK: {
                    boolean isAttackEntered = gameController.enterAttackPhase();

                    if (isAttackEntered) {
                        gameController.handleAttackCommand(args.regularCommands.toArray(new String[args.regularCommands.size()]));
                    }

                    break;
                }
                case GameCommands.DEFEND: {
                    boolean isAttackEntered = gameController.enterAttackPhase();

                    if (isAttackEntered) {
                        gameController.handleDefendCommand(args.regularCommands.toArray(new String[args.regularCommands.size()]));
                    }

                    break;
                }
                case GameCommands.ATTACKMOVE: {
                    boolean isAttackEntered = gameController.enterAttackPhase();

                    if (isAttackEntered) {
                        gameController.handleAttackMoveCommand(args.regularCommands.toArray(new String[args.regularCommands.size()]));
                    }

                    break;
                }
                case GameCommands.FORTIFY: {
                    boolean isFortifyPhase = gameController.enterFortifyPhase();

                    if(isFortifyPhase) {
                        gameController.handleMultipleFortificationCommand(args.regularCommands.toArray(new String[args.regularCommands.size()]));
                    }

                    break;
                }
                case GameCommands.CURRENTPLAYER: {
                    gameController.showCurrentPlayer();
                    break;
                }
                default: {
                    System.out.println("Command not exist!");
                    break;
                }
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    /**
	 * This method is used to Enter an action to Start the game.
	 */
    public static void runFromBeginning() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("GAME START");
        System.out.print("Enter your action: ");
        String command = scanner.nextLine();

        while (!command.equals(GameCommands.EXIT)) {
            ModelCommands cmds = new ModelCommands(command);
            jumpToCommand(cmds);
            System.out.print("Enter your action: ");
            command = scanner.nextLine();
        }
        scanner.close();
    }
}
