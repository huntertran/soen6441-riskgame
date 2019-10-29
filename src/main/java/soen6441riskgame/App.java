package soen6441riskgame;

import java.io.IOException;
import java.util.Scanner;

import soen6441riskgame.commands.GameCommands;
import soen6441riskgame.commands.MapEditorCommands;
import soen6441riskgame.controllers.GameController;
import soen6441riskgame.controllers.MapController;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.ModelCommandsPair;

public final class App {
    private App() {
    }

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

    // public static void jumpToCommand(String[] args) {
    //     String command = args[0].toLowerCase();
    //     String[] remainingArgs = Arrays.copyOfRange(args, 1, args.length);

    //     MapController mapController = new MapController();
    //     GameController gameController = new GameController();

    //     switch (command) {
    //     case MapEditorCommands.EDITCONTINENT: {
    //         mapController.editContinent(remainingArgs);
    //         break;
    //     }
    //     case MapEditorCommands.EDITCOUNTRY: {
    //         mapController.editCountry(remainingArgs);
    //         break;
    //     }
    //     case MapEditorCommands.EDITNEIGHBOR: {
    //         mapController.editNeighbor(remainingArgs);
    //         break;
    //     }
    //     case MapEditorCommands.SHOWMAP: {
    //         mapController.showMap();
    //         break;
    //     }
    //     case MapEditorCommands.SAVEMAP: {
    //         try {
    //             mapController.saveMap(remainingArgs[0]);
    //         } catch (IOException e) {
    //             System.out.println("Error: " + e.getClass().getName());
    //         }

    //         break;
    //     }
    //     case MapEditorCommands.EDITMAP: {
    //         try {
    //             mapController.editMap(remainingArgs[0]);
    //         } catch (IOException e) {
    //             System.out.println("Error: " + e.getClass().getName());
    //         }
    //         break;
    //     }
    //     case MapEditorCommands.VALIDATEMAP: {
    //         mapController.validateMap();
    //         break;
    //     }
    //     case MapEditorCommands.LOADMAP: {
    //         try {
    //             mapController.loadMap(remainingArgs[0]);

    //             if (mapController.isMapValid()) {
    //                 mapController.showMap();
    //             }
    //         } catch (IOException e) {
    //             System.out.println("Error: " + e.getClass().getName());
    //         }

    //         break;
    //     }
    //     case GameCommands.GAMEPLAYER: {
    //         gameController.handlePlayerAddAndRemoveCommand(remainingArgs);
    //         break;
    //     }
    //     case GameCommands.POPULATECOUNTRIES: {
    //         gameController.populateCountries();
    //         gameController.initPlayersUnplacedArmies();
    //         break;
    //     }
    //     case GameCommands.PLACEARMY: {
    //         // gameController.startRoundRobinPlayers();
    //         gameController.handlePlaceArmyCommand(remainingArgs[0]);
    //         break;
    //     }
    //     case GameCommands.PLACEALL: {
    //         gameController.handlePlaceAllCommand();
    //         break;
    //     }
    //     case GameCommands.REINFORCE: {
    //         gameController.enterReinforcement();
    //         gameController.handleReinforceCommand(remainingArgs);
    //         break;
    //     }
    //     case GameCommands.FORTIFY: {
    //         gameController.handleFortifyCommand(remainingArgs);
    //         break;
    //     }
    //     case GameCommands.CURRENTPLAYER:{
    //         gameController.showCurrentPlayer();
    //         break;
    //     }
    //     default: {
    //         System.out.println("Command not exist!");
    //         break;
    //     }
    //     }
    // }

    public static void jumpToCommand(ModelCommands args) {
        String command = args.cmd;
        //String[] remainingArgs = Arrays.copyOfRange(args, 1, args.length);

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

                    if (mapController.isMapValid()) {
                        mapController.showMap();
                    }
                } catch (IOException e) {
                    System.out.println("Error: " + e.getClass().getName());
                }

                break;
            }
            case GameCommands.GAMEPLAYER: {
                gameController.handlePlayerAddAndRemoveCommand(args.regularCommands.toArray(new String[args.regularCommands.size()]));
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
                gameController.enterReinforcement();
                gameController.handleReinforceCommand(args.regularCommands.toArray(new String[args.regularCommands.size()]));
                break;
            }
            case GameCommands.FORTIFY: {
                gameController.handleFortifyCommand(args.regularCommands.toArray(new String[args.regularCommands.size()]));
                break;
            }
            case GameCommands.CURRENTPLAYER:{
                gameController.showCurrentPlayer();
                break;
            }
            default: {
                System.out.println("Command not exist!");
                break;
            }
        }
    }


    public static void runFromBeginning() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("GAME START");
        System.out.print("Enter your action: ");
        String command = scanner.nextLine();

        while (!command.equals(GameCommands.EXIT)) {
            ModelCommands cmds = new ModelCommands(command);
            System.out.println(cmds.cmd);
            System.out.println(cmds.regularCommands);
            System.out.println(cmds.subRoutine);

            jumpToCommand(cmds);
            System.out.print("Enter your action: ");
            command = scanner.nextLine();
        }

        scanner.close();
    }
}
