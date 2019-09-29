package main.java.soen6441riskgame;

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

        MapController mapController = new MapController();
        mapController.loadMap("test");

        // if (args.length == 0) {
        //     runFromBegining();
        // } else {
        //     jumpToCommand(args);
        // }
    }

    public static void jumpToCommand(String[] args) {
        // Map Editor
        // editcontinent -add continentname continentvalue-remove continentname
        // editcountry -add countryname continentname-remove countryname
        // editneighbor-add countrynameneighborcountryname-remove
        // countrynameneighborcountryname
        // showmap(show all continentsandcountriesand their neighbors)
        // savemap filename
        // editmap filename
        // validatemap

        MapController mapController = new MapController();

        String command = args[0].toLowerCase();
        String[] remainingArgs = Arrays.copyOfRange(args, 1, args.length);

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
