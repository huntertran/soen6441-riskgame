package soen6441riskgame;

import java.io.IOException;
import java.util.Scanner;

import soen6441riskgame.controllers.GameController;
import soen6441riskgame.controllers.MapController;
import soen6441riskgame.controllers.SaveLoadController;
import soen6441riskgame.controllers.TournamentController;
import soen6441riskgame.enums.MapCommands;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.models.commands.MapEditorCommands;
import soen6441riskgame.models.commands.TournamentCommands;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.ModelCommandsPair;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;

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
     * @param args the command arguments
     */
    public static void main(String[] args) {
        ConsolePrinter.printFormat("SOEN 6441 - Risk Domination game");

        ConsolePrinter.createWorldView();

        if (args.length == 0) {
            runFromBeginning();
        } else {
            String commands = String.join(" ", args);
            jumpToCommand(commands);
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

                boolean isConquestMapType = false;
                if (args.regularCommands.size() > 1) {
                    if (args.regularCommands.get(1).contains(MapEditorCommands.CONQUEST_MAP_TYPE)) {
                        isConquestMapType = true;
                    }
                }

                try {
                    mapController.saveMap(args.regularCommands.get(0), isConquestMapType);
                } catch (IOException e) {
                    ConsolePrinter.printFormat("Error: " + e.getClass().getName());
                }
                break;
            }
            case MapEditorCommands.EDITMAP: {
                try {
                    mapController.editMap(args.regularCommands.get(0));
                } catch (IOException e) {
                    ConsolePrinter.printFormat("Error: " + e.getClass().getName());
                }
                break;
            }
            case MapEditorCommands.VALIDATEMAP: {
                mapController.validateMap();
                break;
            }
            case MapEditorCommands.LOADMAP: {
                boolean isConquestMapType = false;
                if (args.regularCommands.size() > 1) {
                    if (args.regularCommands.get(1).contains(MapEditorCommands.CONQUEST_MAP_TYPE)) {
                        isConquestMapType = true;
                    }
                }

                try {
                    mapController.loadMap(args.regularCommands.get(0), isConquestMapType);
                } catch (IOException e) {
                    ConsolePrinter.printFormat("Error: " + e.getClass().getName());
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
                break;
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
            case GameCommands.SAVEGAME: {
                SaveLoadController saveLoadController = new SaveLoadController();
                saveLoadController.saveGame(args.regularCommands.get(0));
                break;
            }
            case GameCommands.LOADGAME: {
                SaveLoadController saveLoadController = new SaveLoadController();
                saveLoadController.loadGame(args.regularCommands.get(0));
                break;
            }
            case TournamentCommands.TOURNAMENT: {
                TournamentController tournamentController = new TournamentController();
                tournamentController.enterTournament(args.regularCommands);
                break;
            }
            default: {
                ConsolePrinter.printFormat("Command not exist!");
                break;
            }
        }
    }

    /**
     * Jump to a specific function to handle the command
     *
     * @param commands the command and it's arguments
     */
    public static void jumpToCommand(String commands) {
        MapCommands mapCommands = MapCommands.parseCommand(commands);

        MapController mapController = new MapController();

        switch (mapCommands) {
            case EDITCONTINENT: {
                mapController.editContinent(mapCommands.getCommandRoutines());
                return;
            }
            case NONE:
            default: {
                return;
            }
        }
    }

    /**
     * start the game from beginning, allow user to enter commands and arguments
     */
    public static void runFromBeginning() {
        Scanner scanner = new Scanner(System.in);

        ConsolePrinter.printFormat("GAME START");
        ConsolePrinter.printFormat(GameBoard.getInstance().standardPrintStream,
                                   false,
                                   "ENTER YOUR ACTION: ");
        String command = scanner.nextLine();

        while (!command.equals(GameCommands.EXIT)) {
            jumpToCommand(command);
            ConsolePrinter.printFormat(GameBoard.getInstance().standardPrintStream,
                                       false,
                                       "ENTER YOUR ACTION: ");
            command = scanner.nextLine();
        }

        scanner.close();
    }
}
