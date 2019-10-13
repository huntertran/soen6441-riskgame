package soen6441riskgame;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import soen6441riskgame.commands.MapEditorCommands;
import soen6441riskgame.controllers.GameController;
import soen6441riskgame.controllers.MapController;

public final class App {
    private App() {
    }

    public static void main(String[] args) throws IOException {
        System.out.println("SOEN 6441 - Risk Domination game");

        if (args.length == 0) {
            runFromBegining();
        } else {
            jumpToCommand(args);
        }
    }

    public static void jumpToCommand(String[] args) throws IOException {
        String command = args[0].toLowerCase();
        String[] remainingArgs = Arrays.copyOfRange(args, 1, args.length);

        MapController mapController = new MapController();
        GameController gameController = new GameController();

        switch (command) {
        case MapEditorCommands.EDITCONTINENT: {
            mapController.editContinent(remainingArgs);
            break;
        }
        case MapEditorCommands.EDITCOUNTRY: {
            mapController.editCountry(remainingArgs);
            break;
        }
        case MapEditorCommands.EDITNEIGHBOR: {
            mapController.editNeighbor(remainingArgs);
            break;
        }
        case MapEditorCommands.SHOWMAP: {
            mapController.showMap();
            break;
        }
        case MapEditorCommands.SAVEMAP: {
            mapController.saveMap(remainingArgs[0]);
        break;
        }
        case MapEditorCommands.EDITMAP: {
            mapController.editMap(remainingArgs[0]);
            break;
        }
        case MapEditorCommands.VALIDATEMAP: {
            mapController.validateMap();
            break;
        }
        case MapEditorCommands.LOADMAP: {
            mapController.loadMap(remainingArgs[0]);
            break;
        }
        case GameCommands.GAMEPLAYER: {
            // TODO
            break;
        }
        case GameCommands.POPULATECOUNTRIES: {
            // TODO
            break;
        }
        case GameCommands.PLACEARMY: {
            // TODO
            break;
        }
        case GameCommands.PLACEALL: {
            // TODO
            break;
        }
        case GameCommands.REINFORCE: {
            // TODO
            break;
        }
        case GameCommands.FORTIFY: {
            // TODO
            break;
        }
        }
    }

    public static void runFromBegining() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("GAME START");
        System.out.print("Enter your action: ");
        String command = scanner.nextLine();

        while (command != GameCommands.EXIT) {
            jumpToCommand(command.split(" "));
            System.out.print("Enter your action: ");
            command = scanner.nextLine();
        }

        scanner.close();
    }
}
