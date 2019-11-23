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
 * The main class of the game
 */
public final class App {
    private App() {
    }

    /**
     * main method of the game. User can run a command directly from console or start the game and enter
     * commands
     *
     * @param args the command ang it's arguments
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

    /**
     * Jump to a specific function to handle the command
     *
     * @param args the command and it's arguments parsed in set
     */
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
                    if (gameController.isNumberOfPlayerValid()) {
                        gameController.populateCountries();
                        gameController.initPlayersUnplacedArmies();
                    }
                    break;
                }
                case GameCommands.PLACEARMY: {
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
                case GameCommands.EXCHANGECARDS: {
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

                    if (isFortifyPhase) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("exception on line " + e.getStackTrace()[0].getLineNumber());
        }
    }

    /**
     * start the game from beginning, allow user to enter commands and arguments
     */
    public static void runFromBeginning() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("GAME START");
        System.out.print("ENTER YOUR ACTION: ");
        String command = scanner.nextLine();

        while (!command.equals(GameCommands.EXIT)) {
            ModelCommands cmds = new ModelCommands(command);
            jumpToCommand(cmds);
            System.out.print("ENTER YOUR ACTION: ");
            command = scanner.nextLine();
        }

        scanner.close();
    }
}
