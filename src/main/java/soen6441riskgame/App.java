package main.java.soen6441riskgame;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import main.java.soen6441riskgame.commands.MapEditorCommands;
import main.java.soen6441riskgame.controllers.GameController;
import main.java.soen6441riskgame.controllers.MapController;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        System.out.println("SOEN 6441 - Risk Domination game");

        // TODO: change back after done testing/integration

        MapController mapController = new MapController();
        mapController.loadMap("test");
        mapController.showMap();

        // if (args.length == 0) {
        // runFromBegining();
        // } else {
        // jumpToCommand(args);
        // }
    }

    public static void jumpToCommand(String[] args) {
        String command = args[0].toLowerCase();
        String[] remainingArgs = Arrays.copyOfRange(args, 1, args.length);

        MapController mapController = new MapController();

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
            try {
                mapController.saveMap(remainingArgs[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        }
    }

    public static void runFromBegining() {
        System.out.println("1: Use map editor");
        System.out.println("2: Play the game");

        System.out.print("Please choose what you want to do: ");

        Scanner scanner = new Scanner(System.in);
        int chosenOption = scanner.nextInt();

        switch (chosenOption) {
        case 1: {
            MapController mapController = new MapController();
            mapController.start();
            break;
        }
        case 2: {
            GameController gameController = new GameController();
            gameController.start();
        }
        }

        scanner.close();
    }
}
