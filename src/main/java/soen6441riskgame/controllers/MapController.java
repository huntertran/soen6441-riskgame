package main.java.soen6441riskgame.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import main.java.soen6441riskgame.models.Continent;
import main.java.soen6441riskgame.models.Coordinate;
import main.java.soen6441riskgame.models.Country;
import main.java.soen6441riskgame.enums.CommonCommandArgs;
import main.java.soen6441riskgame.enums.MapPart;
import main.java.soen6441riskgame.singleton.GameMap;

public final class MapController {
    private void addBorders(int countryOrder, int... borderWithCountries) {
        int[][] graph = GameMap.getInstance().getBorders();
        for (int index = 0; index < borderWithCountries.length; index++) {
            graph[countryOrder - 1][borderWithCountries[index] - 1] = 1;
            graph[borderWithCountries[index] - 1][countryOrder - 1] = 1;
        }
    }

    public void addContinent(String continentName, String continentValue, int... order) {
        if (!isContinentExisted(continentName)) {
            GameMap.getInstance().getContinents()
                    .add(new Continent(continentName, Integer.parseInt(continentValue), order));
        }
    }

    private void updateCountryContinent(Country country, Continent continent) {
        GameMap.getInstance().getCountries().add(country);
        continent.getCountries().add(country);
    }

    private void addCountryFromMapFile(int order, String name, int continentOrder, Coordinate coordinate) {
        for (Continent continent : GameMap.getInstance().getContinents()) {
            if (continent.getOrder() == continentOrder) {
                Country country = new Country(order, name, coordinate, continent);
                updateCountryContinent(country, continent);
            }
        }
    }

    public void addCountry(String countryName, String continentName) {
        Continent continent = getContinentFromName(continentName);
        Country country = getCountryFromName(countryName);

        if (continent == null) {
            System.out.format("Continent %s is not existed", continentName);
            return;
        }

        if (country == null) {
            createNewCountry(countryName, continent);
        } else {
            continent.getCountries().add(country);
        }

        System.out.format("Country %s is added to continent %s", countryName, continentName);
    }

    public void addNeighbor(String countryName, String neighborCountryName) {
        Country country = getCountryFromName(countryName);
        Country neighbor = getCountryFromName(neighborCountryName);

        if (country == null || neighbor == null) {
            System.out.println("The country name or neighbor country name is not existed!");
            return;
        }

        addBorders(country.getOrder(), neighbor.getOrder());
    }

    public boolean checkIfNeighboringCountries(String countryName, String neighborCountryName) {
        int countryOrder = -1, neighbouringCountryOrder = -1;
        for (Country country : GameMap.getInstance().getCountries()) {
            if (countryName.equals(country.getName())) {
                countryOrder = country.getOrder();
            } else if (neighborCountryName.equals(country.getName())) {
                neighbouringCountryOrder = country.getOrder();
            }
        }
        if (GameMap.getInstance().getBorders()[countryOrder - 1][neighbouringCountryOrder - 1] == 1
                && countryOrder != -1 && neighbouringCountryOrder != -1)
            return true;

        return false;
    }

    public void createNewCountry(String countryName, Continent continent) {
        if (getCountryFromName(countryName) != null) {
            return;
        }

        int newCountryOrder = GameMap.getInstance().getCountries().size() + 1;

        Country newCountry = new Country(newCountryOrder, countryName, new Coordinate(0, 0), continent);

        int[][] newBorders = Arrays.copyOf(GameMap.getInstance().getBorders(), newCountryOrder);
        GameMap.getInstance().setBorders(newBorders);

        updateCountryContinent(newCountry, continent);

        System.out.format("Country %s is created", countryName);
    }

    public void editContinent(String[] args) {
        CommonCommandArgs continentCommand = CommonCommandArgs.fromString(args[0]);

        switch (continentCommand) {
        case ADD: {
            addContinent(args[1], args[2]);
            break;
        }
        case REMOVE: {
            removeContinent(args[1]);
            break;
        }
        case NONE: {
            System.out.println("Incorrect command");
            break;
        }
        }
    }

    public void editCountry(String[] args) {
        CommonCommandArgs countryCommand = CommonCommandArgs.fromString(args[0]);

        switch (countryCommand) {
        case ADD: {
            addCountry(args[1], args[2]);
            break;
        }
        case REMOVE: {
            removeCountry(args[1]);
            break;
        }
        case NONE: {
            System.out.println("Incorrect command");
            break;
        }
        }
    }

    public void editMap(String fileName) {
        loadMap(fileName);
    }

    public void editNeighbor(String[] args) {
        CommonCommandArgs neighborCommand = CommonCommandArgs.fromString(args[0]);

        switch (neighborCommand) {
        case ADD: {
            addNeighbor(args[1], args[2]);
            break;
        }
        case REMOVE: {
            removeNeighbor(args[1]);
            break;
        }
        case NONE: {
            System.out.println("Incorrect command");
            break;
        }
        }
    }

    public Continent getContinentFromName(String continentName) {
        for (Continent continent : GameMap.getInstance().getContinents()) {
            if (continent.getName() == continentName)
                return continent;
        }

        return null;
    }

    private Country getCountryFromName(String countryName) {
        for (Country country : GameMap.getInstance().getCountries()) {
            if (country.getName().equals(countryName)) {
                return country;
            }
        }

        return null;
    }

    private ArrayList<Continent> getEmptyContinents() {
        ArrayList<Continent> result = new ArrayList<Continent>();

        for (Continent continent : GameMap.getInstance().getContinents()) {
            if (continent.getCountries().size() == 0) {
                result.add(continent);
            }
        }

        return result;
    }

    private ArrayList<Country> getIsolatedCountries() {
        int[][] borders = GameMap.getInstance().getBorders();
        int borderSize = borders[0].length;
        ArrayList<Country> result = new ArrayList<Country>();

        for (int row = 0; row < borderSize; row++) {
            int rowSum = 0;

            for (int col = 0; col < borderSize; col++) {
                rowSum += borders[row][col];
            }

            if (rowSum == 0) {
                result.add(GameMap.getInstance().getCountries().get(row));
            }
        }

        return result;
    }

    public boolean isContinentExisted(String continentName) {
        Continent continent = getContinentFromName(continentName);

        return continent != null;
    }

    public boolean isCountryExisted(String countryName) {
        for (Country country : GameMap.getInstance().getCountries()) {
            if (country.getName() == countryName)
                return true;
        }

        return false;
    }

    private boolean isNotEnoughCountries(int minimumNumberOfCountries) {
        return GameMap.getInstance().getCountries().size() < minimumNumberOfCountries;
    }

    private boolean isStillInCurrentDataBlock(int index, List<String> lines) {
        if (index < lines.size()) {
            String currentLine = lines.get(index);
            return currentLine != "" && currentLine.contains(" ");
        }

        return false;
    }

    private int loadBordersFromFile(int currentLineIndex, List<String> lines) {
        int numberOfCountry = GameMap.getInstance().getCountries().size();
        GameMap.getInstance().setBorders(new int[numberOfCountry][numberOfCountry]);

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

            addCountryFromMapFile(countryOrder, countryName, continentOrder, coordinate);

            currentLineIndex = index;
        }

        return currentLineIndex + 1;
    }

    public void loadMap(String fileName) {
        // TODO: change back when done testing/integration
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
                MapPart part = MapPart.fromString(firstWord);

                switch (part) {
                case NAME: {
                    GameMap.getInstance().setMapName(currentLine.split("name")[1].trim());
                    break;
                }
                case FILES: {
                    break;
                }
                case CONTINENTS: {
                    index = loadContinentsFromFile(index, lines);
                    break;
                }
                case COUNTRIES: {
                    index = loadCountriesFromFile(index, lines);
                    break;
                }
                case BORDERS: {
                    index = loadBordersFromFile(index, lines);
                    break;
                }
                case NONE: {
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

    public void removeContinent(String continentName) {
        System.out.println(
                "Remove continent will make all country inside that continent invalid, thus make the map invalid.");

        Continent continentToRemove = getContinentFromName(continentName);

        if (continentToRemove != null) {
            for (Country country : continentToRemove.getCountries()) {
                country.setContinent(null);
            }

            GameMap.getInstance().getContinents().remove(continentToRemove);

            System.out.format("Continent %s is removed", continentToRemove.getName());
        } else {
            System.out.format("Continent %s is not existed", continentName);
        }
    }

    public void removeCountry(String countryName) {

    }

    public void removeNeighbor(String countryName) {

    }

    public void resetMap() {
        GameMap.getInstance().reset();
    }

    public void saveMap(String fileName) {

    }

    public void showMap() {
        GameMap.getInstance().showContinents();
        // GameMap.getInstance().showCountries();
    }

    public void start(String[]... args) {
        Scanner scanner = new Scanner(System.in);
        scanner.close();
    }

    public boolean validateMap() {
        boolean result = false;

        // Types of errors:
        // 1. less than 6 countries
        // 2. some countries are isolated from the rest
        // 3. empty continents
        // 4. one country is linked to another but no link back
        // There is no need to check for the last one, because not happened in our
        // implementation

        result = isNotEnoughCountries(6) && getIsolatedCountries().size() > 0 && getEmptyContinents().size() > 0;

        return result;
    }
}