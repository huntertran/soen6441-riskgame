package main.java.soen6441riskgame.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import main.java.soen6441riskgame.singleton.GameMap;

public final class MapController {
    public void start(String[]... args) {
        Scanner scanner = new Scanner(System.in);
        scanner.close();
    }

    public void editContinent(String[] args) {
        // -add and -remove
        String continentCommand = args[0].toLowerCase();

        switch (continentCommand) {
        case "-add": {
            addContinent(args[1], args[2]);
            break;
        }
        case "-remove": {
            removeContinent(args[1]);
            break;
        }
        }
    }

    public void editCountry(String[] args) {
        // -add and -remove
        String countryCommand = args[0].toLowerCase();

        switch (countryCommand) {
        case "-add": {
            addCountry(args[1], args[2]);
            break;
        }
        case "-remove": {
            removeCountry(args[1]);
            break;
        }
        }
    }

    public void editNeighbor(String[] args) {
        // -add and -remove
        String neighborCommand = args[0].toLowerCase();

        switch (neighborCommand) {
        case "-add": {
            addNeighbor(args[1], args[2]);
            break;
        }
        case "-remove": {
            removeNeighbor(args[1]);
            break;
        }
        }
    }

    public void showMap() {

    }

    public void saveMap(String fileName) {

    }

    public void editMap(String fileName) {

    }

    public boolean validateMap() {
        boolean result = false;

        return result;
    }

    public void loadMap(String fileName) {
        String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        Path path = Paths.get(filePath);

        List<String> lines;

        try {
            lines = Files.lines(path).collect(Collectors.toList());
            for (int index = 0; index < lines.size(); index++) {
                String currentLine = lines.get(index);
                if (currentLine.startsWith(";")) {
                    continue;
                }

                String firstWord = currentLine.split(" ")[0];

                switch (firstWord) {
                case "name": {
                    GameMap.getInstance().setMapName(currentLine.split("name")[1].trim());
                    break;
                }
                case "[files]": {
                    break;
                }
                case "[continents]": {
                    loadContinentFromFile(index, lines);
                    break;
                }
                case "[countries]": {
                    break;
                }
                case "[borders]": {
                    break;
                }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int loadContinentFromFile(int currentLineIndex, List<String> lines) {
        for (int index = currentLineIndex + 1; index < lines.size(); index++) {
            String currentLine = lines.get(index);
            String[] fragments = currentLine.split(" ");
            String continentName = fragments[0];
            int continentArmy = Integer.parseInt(fragments[1]);

            // String continentColor = fragments[2];

            addContinent(continentName, Integer.toString(continentArmy));

            currentLineIndex = index;
        }

        return currentLineIndex + 1;
    }

    public void addContinent(String continentName, String continentValue) {
        // GameMap.getInstance()
    }

    public void removeContinent(String continentName) {

    }

    public void addCountry(String countryName, String continentName) {

    }

    public void removeCountry(String countryName) {

    }

    public void addNeighbor(String countryName, String neighborCountryName) {

    }

    public void removeNeighbor(String countryName) {

    }
}