package soen6441riskgame;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import soen6441riskgame.commands.MapEditorCommands;
import soen6441riskgame.controllers.GameController;
import soen6441riskgame.controllers.MapController;
import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.singleton.GameMap;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        System.out.println("SOEN 6441 - Risk Domination game");

        if (args.length == 0) {
            runFromBegining();
        } else {
            jumpToCommand(args);
        }
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
                FileWriter writer = new FileWriter(remainingArgs[0]);
				ArrayList<Continent> continents = GameMap.getInstance().getContinents();
				writer.write("[continents]");
				for (Continent continent : continents) {
				    writer.write(continent.getName() + " " + continent.getArmy() + "\n");
				}
				writer.write("\n");
				
				ArrayList<Country> countries = GameMap.getInstance().getCountries();
				writer.write("[countries]");
				for (Country country : countries) {
				    // int countryOrder = country.getOrder();
				    // String countryName = country.getName();
				    // int continentOrder = country.getContinent().getOrder();
				    // Coordinate location = country.getCoordinate();
				
				    writer.write(country.getOrder() + " " + country.getName() + " " + country.getArmyAmount()
				    + "\n");
				}
				
				writer.write("[borders]");
				
				for(Country country:GameMap.getInstance().getCountries()){
				    ArrayList<Country> neighbors = country.getNeighbors();
				
				    String neighborLine = Integer.toString(country.getOrder());
				
				    for(Country neighbor:neighbors){
				        neighborLine += " " + neighbor.getOrder();
				    }
				
				    writer.write(neighborLine);
				}
				
				writer.close();
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
