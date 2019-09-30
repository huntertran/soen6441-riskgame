package main.java.soen6441riskgame.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import main.java.soen6441riskgame.models.Continent;
import main.java.soen6441riskgame.models.Coordinate;
import main.java.soen6441riskgame.models.Country;
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

    public void resetMap(){
        GameMap.getInstance().reset();
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
                    index = loadContinentsFromFile(index, lines);
                    break;
                }
                case "[countries]": {
                    index = loadCountriesFromFile(index, lines);
                    break;
                }
                case "[borders]": {
                    index = loadBordersFromFile(index, lines);
                    break;
                }
                }

            }

            System.out.println("Map loaded");
            showMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isStillInCurrentDataBlock(int index, List<String> lines) {
        if (index < lines.size()) {
            String currentLine = lines.get(index);
            return currentLine != "" && currentLine.contains(" ");
        }

        return false;
    }

    private int loadContinentsFromFile(int currentLineIndex, List<String> lines) {
        int continentOrder = 1;
        for (int index = currentLineIndex + 1; isStillInCurrentDataBlock(index, lines); index++) {
            String currentLine = lines.get(index);
            String[] fragments = currentLine.split(" ");
            String continentName = fragments[0];
            int continentArmy = Integer.parseInt(fragments[1]);

            // String continentColor = fragments[2];

            addContinent(continentName, Integer.toString(continentArmy), continentOrder);

            currentLineIndex = index;
            continentOrder++;
        }

        return currentLineIndex + 1;
    }

    private int loadCountriesFromFile(int currentLineIndex, List<String> lines) {
        for (int index = currentLineIndex + 1; isStillInCurrentDataBlock(index, lines); index++) {
            String currentLine = lines.get(index);

            String[] fragments = currentLine.split(" ");

            int countryOrder = Integer.parseInt(fragments[0]);
            String countryName = fragments[1];
            int continentOrder = Integer.parseInt(fragments[2]);
            int x = Integer.parseInt(fragments[3]);
            int y = Integer.parseInt(fragments[4]);
            Coordinate coordinate = new Coordinate(x, y);

            addCountry(countryOrder, countryName, continentOrder, coordinate);

            currentLineIndex = index;
        }

        return currentLineIndex + 1;
    }

    public boolean isContinentExisted(String continentName) {
        for (Continent continent : GameMap.getInstance().getContinents()) {
            if (continent.getName() == continentName)
                return true;
        }

        return false;
    }

    public void addContinent(String continentName, String continentValue, int... order) {
        if (!isContinentExisted(continentName)) {
            GameMap.getInstance().getContinents()
                    .add(new Continent(continentName, Integer.parseInt(continentValue), order));
        }
    }

    public void removeContinent(String continentName) {

    }

    public void addCountry(String countryName, String continentName) {

    }

    private int loadBordersFromFile(int currentLineIndex, List<String> lines) {
        int numberOfCountry = GameMap.getInstance().getCountries().size();
        GameMap.getInstance().setGraph(new int[numberOfCountry][numberOfCountry]);

        for (int index = currentLineIndex + 1; isStillInCurrentDataBlock(index, lines); index++) {
            String currentLine = lines.get(index);

            String[] fragments = currentLine.split(" ");

            int countryOrder = Integer.parseInt(fragments[0]);

            int[] borderWithCountries = new int[fragments.length - 1];
            for (int borderIndex = 0; borderIndex < borderWithCountries.length; borderIndex++) {
                borderWithCountries[borderIndex] = Integer.parseInt(fragments[borderIndex + 1]);
            }

            addBorders(countryOrder, borderWithCountries);

            currentLineIndex = index;
        }

        return currentLineIndex + 1;
    }

    private void addBorders(int countryOrder, int... borderWithCountries) {
        int[][] graph = GameMap.getInstance().getGraph();
        for (int index = 0; index < borderWithCountries.length; index++) {
            graph[countryOrder - 1][borderWithCountries[index] - 1] = 1;
            graph[borderWithCountries[index] - 1][countryOrder - 1] = 1;
        }
    }

    private void addCountry(int order, String name, int continentOrder, Coordinate coordinate) {
        for (Continent continent : GameMap.getInstance().getContinents()) {
            if (continent.getOrder() == continentOrder) {
                Country country = new Country(order, name, coordinate, continent);

                GameMap.getInstance().getCountries().add(country);
                continent.getCountries().add(country);
            }
        }
    }

    public void removeCountry(String countryName) {

    }

    public void addNeighbor(String countryName, String neighborCountryName) {

    }

    public void removeNeighbor(String countryName) {

    }
}