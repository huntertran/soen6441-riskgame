package soen6441riskgame.controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Coordinate;
import soen6441riskgame.models.Country;
import soen6441riskgame.enums.CommonCommandArgs;
import soen6441riskgame.enums.MapPart;
import soen6441riskgame.singleton.GameMap;

public final class MapController {
    private static final int MINIMUM_AMOUNT_OF_COUNTRIES = 6;

    /**
     * add border from country to others country
     *
     * @param countryOrder        the location of first country in the border graph
     * @param borderWithCountries the location of other countries in the border
     *                            graph
     */
    private void addBorders(int countryOrder, int... borderWithCountries) {
        int[][] graph = GameMap.getInstance().getBorders();
        for (int index = 0; index < borderWithCountries.length; index++) {
            graph[countryOrder - 1][borderWithCountries[index] - 1] = 1;
            graph[borderWithCountries[index] - 1][countryOrder - 1] = 1;
        }
    }

    /**
     * add new continent
     *
     * @param continentName
     * @param continentValue the amount of army for the new continent
     * @param order          the continent other in the list (start with 1 as the
     *                       map file structure indicate)
     */
    public void addContinent(String continentName, String continentValue, int... order) {
        if (!isContinentExisted(continentName)) {
            GameMap.getInstance().getContinents()
                    .add(new Continent(continentName, Integer.parseInt(continentValue), order));
        }
    }

    /**
     * add new country to an existed continent OR add existing country to an existed
     * continent
     *
     * @param countryName   the new country name
     * @param continentName the existed continent name
     */
    public void addCountry(String countryName, String continentName) {
        Continent continent = getContinentFromName(continentName);
        Country country = GameMap.getInstance().getCountryFromName(countryName);

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

    /**
     * add new country from map file
     *
     * @param order          the order of country in the list, start with 1
     * @param name           the country name (no space allowed)
     * @param continentOrder the other of the continent that new country belongs to
     * @param coordinate     the position of the country on a visual map (not used)
     */
    private void addCountryFromMapFile(String name, int continentOrder, Coordinate coordinate) {
        for (Continent continent : GameMap.getInstance().getContinents()) {
            if (continent.getOrder() == continentOrder) {
                Country country = new Country(name, coordinate, continent);
                updateCountryContinent(country, continent);
            }
        }
    }

    /**
     * connect 2 countries with each other on the borderGraph
     *
     * @param countryName
     * @param neighborCountryName
     */
    public void addNeighbor(String countryName, String neighborCountryName) {
        Country country = GameMap.getInstance().getCountryFromName(countryName);
        Country neighbor = GameMap.getInstance().getCountryFromName(neighborCountryName);

        if (country == null || neighbor == null) {
            System.out.println("The country name or neighbor country name is not existed!");
            return;
        }

        addBorders(country.getOrder(), neighbor.getOrder());
    }

    /**
     * create new country, add it to the borderGraph
     *
     * @param countryName
     * @param continent   the name of continent that country belong to
     */
    public void createNewCountry(String countryName, Continent continent) {
        if (GameMap.getInstance().getCountryFromName(countryName) != null) {
            return;
        }

        int newBorderSize = GameMap.getInstance().getCountries().size() + 1;

        Country newCountry = new Country(countryName, new Coordinate(0, 0), continent);

        increaseBorder(newBorderSize);

        updateCountryContinent(newCountry, continent);

        System.out.format("Country %s is created", countryName);
    }

    private void increaseBorder(int newBorderSize) {
        int[][] originalBorder = GameMap.getInstance().getBorders();
        int[][] newBorders = new int[newBorderSize][newBorderSize];

        for (int row = 0; row < newBorderSize - 1; row++) {
            newBorders[row] = Arrays.copyOf(originalBorder[row], newBorderSize);
            newBorders[newBorderSize - 1][row] = 0;
        }

        for (int col = 0; col < newBorderSize; col++) {
            newBorders[newBorderSize - 1][col] = 0;
        }

        GameMap.getInstance().setBorders(newBorders);
    }

    /**
     * handle 'editcontient' command from console
     *
     * @param args -add continentName continentValue -remove continentName
     */
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

    /**
     * handle 'editcountry' command
     *
     * @param args -add countryName continentName -remove countryName
     */
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

    /**
     * handle 'editneighbor' command
     *
     * @param args -add countryName neighborCountryName -remove countryName
     *             neighborCountryName
     */
    public void editNeighbor(String[] args) {
        CommonCommandArgs neighborCommand = CommonCommandArgs.fromString(args[0]);

        switch (neighborCommand) {
        case ADD: {
            addNeighbor(args[1], args[2]);
            break;
        }
        case REMOVE: {
            removeNeighbor(args[1], args[2]);
            break;
        }
        case NONE: {
            System.out.println("Incorrect command");
            break;
        }
        }
    }

    /**
     * get continent object from name
     *
     * @param continentName
     * @return null if continent name is not existed in map
     */
    public Continent getContinentFromName(String continentName) {
        for (Continent continent : GameMap.getInstance().getContinents()) {
            if (continent.getName() == continentName)
                return continent;
        }

        return null;
    }

    // /**
    // * get country object from name
    // *
    // * @param countryName
    // * @return null if country name is not existed in map
    // */
    // public Country getCountryFromName(String countryName) {
    // for (Country country : GameMap.getInstance().getCountries()) {
    // if (country.getName().equals(countryName)) {
    // return country;
    // }
    // }

    // return null;
    // }

    /**
     * get all continents that have no country
     *
     * @return a list of empty continent, or an empty list if all continent have
     *         countries inside
     */
    private ArrayList<Continent> getEmptyContinents() {
        ArrayList<Continent> result = new ArrayList<Continent>();

        for (Continent continent : GameMap.getInstance().getContinents()) {
            if (continent.getCountries().size() == 0) {
                result.add(continent);
            }
        }

        return result;
    }

    /**
     * get countries that not connected to any other countries
     *
     * @return a list of isolated country, or an empty list if all country is
     *         connected
     */
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

    /**
     * check if continent with the specified name existed in map
     *
     * @param continentName
     */
    public boolean isContinentExisted(String continentName) {
        Continent continent = getContinentFromName(continentName);

        return continent != null;
    }

    /**
     * check if country with the specified name existed in map
     *
     * @param countryName
     * @return
     */
    public boolean isCountryExisted(String countryName) {
        Country country = GameMap.getInstance().getCountryFromName(countryName);

        return country != null;
    }

    /**
     * check if the number of country is lower than the minimum amount of country
     * required currently the minimum required is {@MINIMUM_AMOUNT_OF_COUNTRIES}
     *
     * @param minimumNumberOfCountries
     * @return
     */
    private boolean isNotEnoughCountries(int minimumNumberOfCountries) {
        int numberOfCountry = GameMap.getInstance().getCountries().size();
        boolean isNotEnoughCountries = numberOfCountry < minimumNumberOfCountries;

        if (isNotEnoughCountries) {
            System.out.format("Not enough countries. Created: %d - Minimum required: %s", numberOfCountry,
                    minimumNumberOfCountries);
        }

        return isNotEnoughCountries;
    }

    /**
     * The map file stores data in blocks. This function check if the current data
     * line is still in a block or not
     *
     * @param currentLineIndex the current line index
     * @param lines            all the lines in map file
     * @return
     */
    private boolean isStillInCurrentDataBlock(int currentLineIndex, List<String> lines) {
        if (currentLineIndex < lines.size()) {
            String currentLine = lines.get(currentLineIndex);
            return currentLine != "" && currentLine.contains(" ");
        }

        return false;
    }

    /**
     * Load all border data from map file
     *
     * @param currentLineIndex the current line index
     * @param lines            all the line in map file
     * @return the line index that end the border block in map file
     */
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

    /**
     * Load all continent data from map file
     *
     * @param currentLineIndex the current line index
     * @param lines            all the line in map file
     * @return the line index that end the continent block in map file
     */
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

    /**
     * Load all country data from map file
     *
     * @param currentLineIndex the current line index
     * @param lines            all the line in map file
     * @return the line index that end the country block in map file
     */
    private int loadCountriesFromFile(int currentLineIndex, List<String> lines) {
        for (int index = currentLineIndex + 1; isStillInCurrentDataBlock(index, lines); index++) {
            String currentLine = lines.get(index);

            String[] fragments = currentLine.split(" ");

            // int countryOrder = Integer.parseInt(fragments[0]);
            String countryName = fragments[1];
            int continentOrder = Integer.parseInt(fragments[2]);
            int x = Integer.parseInt(fragments[3]);
            int y = Integer.parseInt(fragments[4]);
            Coordinate coordinate = new Coordinate(x, y);

            addCountryFromMapFile(countryName, continentOrder, coordinate);

            currentLineIndex = index;
        }

        return currentLineIndex + 1;
    }

    /**
     * load map from file
     *
     * @param fileName the exact path to map file, end with .map extension for
     *                 example:
     *                 D://src/test/java/soen6441riskgame/maps/RiskEurope.map
     */
    public void loadMap(String fileName) {
        Path path = Paths.get(fileName);

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove a continent from map. Remove continent will make all country inside
     * that continent invalid, thus make the map invalid.
     *
     * @param continentName name of the continent
     */
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

    /**
     * remove a country from map, this including remove it border info and remove it
     * from continent
     *
     * @param countryName name of the country to remove
     */
    public void removeCountry(String countryName) {
        Country country = GameMap.getInstance().getCountryFromName(countryName);

        if (country == null) {
            System.out.format("Country %s is not existed", countryName);
            return;
        }

        // Remove it border
        int countryOrder = country.getOrder();
        removeBorder(countryOrder - 1);

        // Remove it from continent
        country.getContinent().getCountries().remove(country);
        // Remove it from list country
        GameMap.getInstance().getCountries().remove(country);
    }

    private void removeBorder(int borderLocation) {
        int[][] originalBorder = GameMap.getInstance().getBorders();
        int size = originalBorder[0].length - 1;

        int[][] newBorders = new int[size][size];

        for (int row = 0; row < borderLocation; row++) {
            newBorders[row] = Arrays.copyOfRange(originalBorder[row], 0, borderLocation - 1);
        }

        for (int row = borderLocation + 1; row < size; row++) {
            newBorders[row] = Arrays.copyOfRange(originalBorder[row], borderLocation + 1, size - 1);
        }

        GameMap.getInstance().setBorders(newBorders);
    }

    /**
     * remove connection between 2 countries in the borderGraph
     *
     * @param countryName
     * @param neighborCountryName
     */
    public void removeNeighbor(String countryName, String neighborCountryName) {
        Country country = GameMap.getInstance().getCountryFromName(countryName);
        Country neighbor = GameMap.getInstance().getCountryFromName(neighborCountryName);

        if (country == null || neighbor == null) {
            System.out.println("One or both countries is not existed");
            return;
        }

        GameMap.getInstance().getBorders()[country.getOrder() - 1][neighbor.getOrder() - 1] = 0;
        GameMap.getInstance().getBorders()[country.getOrder() - 1][neighbor.getOrder() - 1] = 0;
    }

    /**
     * Reset the map, clear all continents, countries and borders
     */
    public void resetMap() {
        GameMap.getInstance().reset();
    }

    public void saveMap(String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);

        writeContinentsToFile(writer);

        writeCountriesToFile(writer);

        writeBordersToFile(writer);

        writer.close();

    }

    private void writeBordersToFile(FileWriter writer) throws IOException {
        writer.write("[borders]\n");

        for (Country country : GameMap.getInstance().getCountries()) {
            ArrayList<Country> neighbors = country.getNeighbors();

            String neighborLine = Integer.toString(country.getOrder());

            for (Country neighbor : neighbors) {
                neighborLine += " " + neighbor.getOrder();
            }

            writer.write(neighborLine + "\n");
        }
    }

    private void writeCountriesToFile(FileWriter writer) throws IOException {
        ArrayList<Country> countries = GameMap.getInstance().getCountries();
        writer.write("[countries]\n");
        for (Country country : countries) {
            writer.write(country.getOrder() + " " + country.getName() + " " + country.getContinent().getOrder() + " "
                    + country.getArmyAmount() + "\n");
        }
        writer.write("\n");
    }

    private void writeContinentsToFile(FileWriter writer) throws IOException {
        ArrayList<Continent> continents = GameMap.getInstance().getContinents();
        writer.write("[continents]\n");
        for (Continent continent : continents) {
            writer.write(continent.getName() + " " + continent.getArmy() + "\n");
        }
        writer.write("\n");
    }

    /**
     * Display all continents and it's countries to console
     */
    public void showMap() {
        GameMap.getInstance().showContinents();
    }

    /**
     * add the country to country list in GameMap, and add to continent's country
     * list in GameMap this function should be remove when Dependency Injection
     * implemented
     *
     * @param country
     * @param continent
     */
    private void updateCountryContinent(Country country, Continent continent) {
        GameMap.getInstance().getCountries().add(country);
        continent.getCountries().add(country);
    }

    /**
     * validate the map Types of errors: 1. less than 6 countries 2. some countries
     * are isolated from the rest 3. empty continents 4. one country is linked to
     * another but no link back There is no need to check for the last one, because
     * not happened in our implementation
     *
     * @return
     */
    public boolean validateMap() {
        boolean isNotEnoughCountries = isNotEnoughCountries(MINIMUM_AMOUNT_OF_COUNTRIES);

        ArrayList<Country> isolatedCountries = getIsolatedCountries();
        boolean isIsolatedCountryExisted = isolatedCountries.size() > 0;

        ArrayList<Continent> emptyContinents = getEmptyContinents();
        boolean isEmptyContinentExisted = emptyContinents.size() > 0;

        if (isIsolatedCountryExisted) {
            System.out.println("Isolated countries existed:");
            for (Country country : isolatedCountries) {
                country.view(1);
            }
        }

        if (isEmptyContinentExisted) {
            System.out.println("Empty continent existed:");
            for (Continent continent : emptyContinents) {
                continent.view(1);
            }
        }

        return isNotEnoughCountries && isIsolatedCountryExisted && isEmptyContinentExisted;
    }
}