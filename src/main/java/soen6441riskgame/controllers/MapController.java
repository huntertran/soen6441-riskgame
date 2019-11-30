package soen6441riskgame.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import soen6441riskgame.enums.CommonCommandArgs;
import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Coordinate;
import soen6441riskgame.models.Country;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;
import soen6441riskgame.utils.GraphChecker;
import soen6441riskgame.utils.map.ConquestMapReadable;
import soen6441riskgame.utils.map.DominationMapReadable;
import soen6441riskgame.utils.map.MapReaderAdapter;

/**
 * handle the operations to generate, edit and control the map.
 */
public final class MapController {
    private static final int MINIMUM_AMOUNT_OF_COUNTRIES = 6;
    private DominationMapReadable dominationMapReader = new MapReaderAdapter();
    private ConquestMapReadable conquestMapReader = new MapReaderAdapter();

    public void loadMap(String fileName, boolean isConquestMapType) throws IOException {
        if (isConquestMapType) {
            conquestMapReader.loadConquestMap(fileName);
        } else {
            dominationMapReader.loadMap(fileName);
        }
    }

    public void loadMap(String fileName) throws IOException {
        loadMap(fileName, false);
    }

    public void addContinent(String continentName, String continentValue, int... order) {
        dominationMapReader.addContinent(continentName, continentValue, order);
    }

    /**
     * add new country to an existed continent OR add existing country to an existed continent
     *
     * @param countryName   the new country name
     * @param continentName the existed continent name
     */
    public void addCountry(String countryName, String continentName) {
        Continent continent = GameBoard.getInstance().getGameBoardMap().getContinentFromName(continentName);
        Country country = GameBoard.getInstance().getGameBoardMap().getCountryFromName(countryName);

        if (continent == null) {
            ConsolePrinter.printFormat("Continent %s is not existed", continentName);
            return;
        }

        if (country == null) {
            createNewCountry(countryName, continent);
        } else {
            continent.getCountries().add(country);
        }

        ConsolePrinter.printFormat("Country %s is added to continent %s", countryName, continentName);
    }

    /**
     * connect 2 countries with each other on the borderGraph
     *
     * @param countryName         name of the country
     * @param neighborCountryName name of the neighbor country
     */
    public void addNeighbor(String countryName, String neighborCountryName) {
        Country country = GameBoard.getInstance().getGameBoardMap().getCountryFromName(countryName);
        Country neighbor = GameBoard.getInstance().getGameBoardMap().getCountryFromName(neighborCountryName);

        if (country == null || neighbor == null) {
            ConsolePrinter.printFormat("The country name or neighbor country name is not existed!");
            return;
        }

        dominationMapReader.addBorders(country.getOrder(), neighbor.getOrder());
    }

    /**
     * create new country, add it to the borderGraph
     *
     * @param countryName name of the country
     * @param continent   the name of continent that country belong to
     */
    public void createNewCountry(String countryName, Continent continent) {
        if (GameBoard.getInstance().getGameBoardMap().getCountryFromName(countryName) != null) {
            return;
        }

        int newBorderSize = GameBoard.getInstance().getGameBoardMap().getCountries().size() + 1;

        Country newCountry = new Country(newBorderSize, countryName, new Coordinate(0, 0), continent);

        increaseBorder(newBorderSize);

        GameBoard.getInstance().getGameBoardMap().updateCountryContinent(newCountry, continent);

        ConsolePrinter.printFormat("Country %s is created", countryName);
    }

    /**
     * increase border
     *
     * @param newBorderSize location of the border to insert
     */
    private void increaseBorder(int newBorderSize) {
        int[][] originalBorder = GameBoard.getInstance().getGameBoardMap().getBorders();
        int[][] newBorders = new int[newBorderSize][newBorderSize];

        for (int row = 0; row < newBorderSize - 1; row++) {
            newBorders[row] = Arrays.copyOf(originalBorder[row], newBorderSize);
            newBorders[newBorderSize - 1][row] = 0;
        }

        for (int col = 0; col < newBorderSize; col++) {
            newBorders[newBorderSize - 1][col] = 0;
        }

        GameBoard.getInstance().getGameBoardMap().setBorders(newBorders);
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
                dominationMapReader.addContinent(args[1], args[2]);
                break;
            }
            case REMOVE: {
                removeContinent(args[1]);
                break;
            }
            case INVALID:
            case NONE: {
                ConsolePrinter.printFormat("Incorrect command");
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
            case INVALID:
            case NONE: {
                ConsolePrinter.printFormat("Incorrect command");
                break;
            }
        }
    }

    /**
     * edit an existing map from file
     *
     * @param fileName the map file
     * @throws IOException exception
     */
    public void editMap(String fileName) throws IOException {
        try {
            dominationMapReader.loadMap(fileName);
        } catch (IOException e) {
            // file not existed. Create new map
            saveMap(fileName);
        }
    }

    /**
     * save the map to a file
     *
     * @param fileName          path to file
     * @param isConquestMapType true to save map as conquest map type
     * @throws IOException exception
     */
    public void saveMap(String fileName, boolean isConquestMapType) throws IOException {
        if (!isMapValid()) {
            ConsolePrinter.printFormat("Invalid map. Map not saved");
            return;
        }

        if (isConquestMapType) {
            conquestMapReader.writeMapToConquestFile(fileName);
        } else {
            dominationMapReader.writeMapToFile(fileName);
        }
    }

    /**
     * save the map to a file
     *
     * @param fileName path to file
     * @throws IOException exception
     */
    public void saveMap(String fileName) throws IOException {
        saveMap(fileName, false);
    }

    /**
     * handle 'editneighbor' command
     *
     * @param args -add countryName neighborCountryName -remove countryName neighborCountryName
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
            case INVALID:
            case NONE: {
                ConsolePrinter.printFormat("Incorrect command");
                break;
            }
        }
    }

    /**
     * get all continents that have no country
     *
     * @return a list of empty continent, or an empty list if all continent have countries inside
     */
    private ArrayList<Continent> getEmptyContinents() {
        ArrayList<Continent> result = new ArrayList<Continent>();

        for (Continent continent : GameBoard.getInstance().getGameBoardMap().getContinents()) {
            if (continent.getCountries().size() == 0) {
                result.add(continent);
            }
        }

        return result;
    }

    /**
     * get countries that not connected to any other countries
     *
     * @return a list of isolated country, or an empty list if all country is connected
     */
    private ArrayList<Country> getIsolatedCountries() {
        int[][] borders = GameBoard.getInstance().getGameBoardMap().getBorders();
        int borderSize = borders[0].length;
        ArrayList<Country> result = new ArrayList<Country>();

        for (int row = 0; row < borderSize; row++) {
            int rowSum = 0;

            for (int col = 0; col < borderSize; col++) {
                rowSum += borders[row][col];
            }

            if (rowSum == 0) {
                result.add(GameBoard.getInstance().getGameBoardMap().getCountries().get(row));
            }
        }

        return result;
    }

    /**
     * check if the number of country is lower than the minimum amount of country required currently the
     * minimum required is {@MINIMUM_AMOUNT_OF_COUNTRIES}
     *
     * @return false if the number of country is lower than the minimum amount of country required
     */
    private boolean isNotEnoughCountries() {
        int numberOfCountry = GameBoard.getInstance().getGameBoardMap().getCountries().size();
        boolean isNotEnoughCountries = numberOfCountry < MapController.MINIMUM_AMOUNT_OF_COUNTRIES;

        if (isNotEnoughCountries) {
            ConsolePrinter.printFormat("Not enough countries. Created: %d - Minimum required: %s",
                                       numberOfCountry,
                                       MapController.MINIMUM_AMOUNT_OF_COUNTRIES);
        }

        return isNotEnoughCountries;
    }

    /**
     * Remove a continent from map. Remove continent will make all country inside that continent
     * invalid, thus make the map invalid.
     *
     * @param continentName name of the continent
     */
    public void removeContinent(String continentName) {
        ConsolePrinter.printFormat("Remove continent will remove all country inside that continent");

        Continent continentToRemove = GameBoard.getInstance().getGameBoardMap().getContinentFromName(continentName);

        if (continentToRemove != null) {
            String[] countriesToRemove = new String[continentToRemove.getCountries().size()];

            for (int index = 0; index < countriesToRemove.length; index++) {
                countriesToRemove[index] = continentToRemove.getCountries().get(index).getName();
            }

            for (String country : countriesToRemove) {
                removeCountry(country);
            }

            GameBoard.getInstance()
                     .getGameBoardMap()
                     .getContinents()
                     .remove(continentToRemove);

            ConsolePrinter.printFormat("Continent %s is removed", continentToRemove.getName());
        } else {
            ConsolePrinter.printFormat("Continent %s is not existed", continentName);
        }
    }

    /**
     * remove a country from map, this including remove it border info and remove it from continent
     *
     * @param countryName name of the country to remove
     */
    public void removeCountry(String countryName) {
        Country country = GameBoard.getInstance().getGameBoardMap().getCountryFromName(countryName);

        if (country == null) {
            ConsolePrinter.printFormat("Country %s is not existed", countryName);
            return;
        }

        // Remove it border
        int countryOrder = country.getOrder();
        removeBorder(countryOrder - 1);

        // Remove it from continent
        country.getContinent().getCountries().remove(country);
        // Remove it from list country
        GameBoard.getInstance().getGameBoardMap().getCountries().remove(country);
    }

    /**
     * Remove border
     *
     * @param borderLocation location of the border to remove
     */
    private void removeBorder(int borderLocation) {
        int[][] originalBorder = GameBoard.getInstance().getGameBoardMap().getBorders();
        int size = originalBorder[0].length - 1;

        int[][] newBorders = new int[size][size];

        for (int row = 0; row < borderLocation; row++) {
            for (int col = 0; col < borderLocation; col++) {
                newBorders[row][col] = originalBorder[row][col];
            }
        }

        for (int row = size - 1; row >= borderLocation; row--) {
            for (int col = size - 1; col >= borderLocation; col--) {
                newBorders[row][col] = originalBorder[row + 1][col + 1];
            }
        }

        GameBoard.getInstance().getGameBoardMap().setBorders(newBorders);
    }

    /**
     * remove connection between 2 countries in the borderGraph
     *
     * @param countryName         name of the country
     * @param neighborCountryName name of the neighbor country
     */
    public void removeNeighbor(String countryName, String neighborCountryName) {
        Country country = GameBoard.getInstance().getGameBoardMap().getCountryFromName(countryName);
        Country neighbor = GameBoard.getInstance().getGameBoardMap().getCountryFromName(neighborCountryName);

        if (country == null || neighbor == null) {
            ConsolePrinter.printFormat("One or both countries is not existed");
            return;
        }

        GameBoard.getInstance().getGameBoardMap().getBorders()[country.getOrder() - 1][neighbor.getOrder() - 1] = 0;
        GameBoard.getInstance().getGameBoardMap().getBorders()[country.getOrder() - 1][neighbor.getOrder() - 1] = 0;
    }

    /**
     * Display all continents and it's countries to console
     */
    public void showMap() {
        GameBoard.getInstance().getGameBoardMap().showContinents();
    }

    /**
     * get the country that not belong to any continent
     *
     * @return list of countries
     */
    private ArrayList<Country> getCountriesHaveNoContinent() {
        ArrayList<Country> countriesWithNoContinent = new ArrayList<Country>();
        ArrayList<Country> countries = GameBoard.getInstance().getGameBoardMap().getCountries();

        for (Country country : countries) {
            if (country.getContinent() == null) {
                countriesWithNoContinent.add(country);
            }
        }

        return countriesWithNoContinent;
    }

    /**
     * validate the map Types of errors: 1. less than 6 countries 2. some countries are isolated from
     * the rest 3. empty continents 4. one country is linked to another but no link back There is no
     * need to check for the last one, because not happened in our implementation
     *
     * @return is map valid or not
     */
    public boolean isMapValid() {
        if (isNotEnoughCountries()) {
            return false;
        }

        ArrayList<Country> isolatedCountries = getIsolatedCountries();
        boolean isIsolatedCountryExisted = isolatedCountries.size() > 0;

        ArrayList<Continent> emptyContinents = getEmptyContinents();
        boolean isEmptyContinentExisted = emptyContinents.size() > 0;

        ArrayList<Country> countriesWithNoContinent = getCountriesHaveNoContinent();
        boolean isCountryWithNoContinentExisted = countriesWithNoContinent.size() > 0;

        if (isIsolatedCountryExisted) {
            ConsolePrinter.printFormat("Isolated countries existed:");
            for (Country country : isolatedCountries) {
                country.view(1);
            }
        }

        if (isEmptyContinentExisted) {
            ConsolePrinter.printFormat("Empty continent existed:");
            for (Continent continent : emptyContinents) {
                continent.view(1);
            }
        }

        if (isCountryWithNoContinentExisted) {
            ConsolePrinter.printFormat("Country that not belong to any continent existed:");
            for (Country country : countriesWithNoContinent) {
                country.viewWithoutNeighbors(1);
            }
        }

        // check if country in continents are connected
        boolean isCountriesInContinentConnected = true;
        ArrayList<Continent> continents = GameBoard.getInstance().getGameBoardMap().getContinents();
        for (Continent continent : continents) {
            if (!continent.isContinentConnected()) {
                isCountriesInContinentConnected = false;
                break;
            }
        }

        boolean isMapConnected = GraphChecker.isCountriesConnected(GameBoard.getInstance()
                                                                            .getGameBoardMap()
                                                                            .getCountries());

        return !isIsolatedCountryExisted
               && !isEmptyContinentExisted
               && !isCountryWithNoContinentExisted
               && isCountriesInContinentConnected
               && isMapConnected;
    }

    /**
     * handle the map validation command
     */
    public void validateMap() {
        if (isMapValid()) {
            ConsolePrinter.printFormat("Map valid");
        } else {
            ConsolePrinter.printFormat("Invalid map");
        }
    }
}
