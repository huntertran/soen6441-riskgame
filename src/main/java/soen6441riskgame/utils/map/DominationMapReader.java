package soen6441riskgame.utils.map;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import soen6441riskgame.enums.MapPart;
import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Coordinate;
import soen6441riskgame.models.Country;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;
import soen6441riskgame.utils.Parser;

public class DominationMapReader {

    /**
     * load map from file
     *
     * @param fileName the exact path to map file, end with .map extension for example:
     *                 D://src/test/java/soen6441riskgame/maps/RiskEurope.map
     * @throws IOException exception
     */
    public void loadMap(String fileName) throws IOException {
        Path path = Paths.get(fileName);

        List<String> lines = Files.lines(path).collect(Collectors.toList());
        resetMap();
        for (int index = 0; index < lines.size(); index++) {
            String currentLine = lines.get(index);
            if (currentLine.startsWith(";")) {
                continue;
            }

            String firstWord = currentLine.split(" ")[0];
            MapPart part = MapPart.fromString(firstWord);

            switch (part) {
                case NAME: {
                    GameBoard.getInstance()
                             .getGameBoardMap()
                             .setMapName(currentLine.split("name")[1].trim());
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
                case FILES:
                case NONE: {
                    break;
                }
            }
        }

        ConsolePrinter.printFormat("Map loaded");
    }

    /**
     * Reset the map, clear all continents, countries and borders
     */
    public void resetMap() {
        GameBoard.getInstance().reset();
    }

    /**
     * Load all continent data from map file
     *
     * @param currentLineIndex the current line index
     * @param lines            all the line in map file
     * @return the line index that end the continent block in map file
     */
    public int loadContinentsFromFile(int currentLineIndex, List<String> lines) {
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
    public int loadCountriesFromFile(int currentLineIndex, List<String> lines) {
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

    /**
     * Load all border data from map file
     *
     * @param currentLineIndex the current line index
     * @param lines            all the line in map file
     * @return the line index that end the border block in map file
     */
    public int loadBordersFromFile(int currentLineIndex, List<String> lines) {
        int numberOfCountry = GameBoard.getInstance().getGameBoardMap().getCountries().size();
        GameBoard.getInstance().getGameBoardMap().setBorders(new int[numberOfCountry][numberOfCountry]);

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
     * The map file stores data in blocks. This function check if the current data line is still in a
     * block or not
     *
     * @param currentLineIndex the current line index
     * @param lines            all the lines in map file
     * @return if the current data line is still in a block or not
     */
    public boolean isStillInCurrentDataBlock(int currentLineIndex, List<String> lines) {
        if (currentLineIndex < lines.size()) {
            String currentLine = lines.get(currentLineIndex);
            return !currentLine.equals("") && currentLine.contains(" ");
        }

        return false;
    }

    /**
     * save the map to a file
     *
     * @param fileName path to file
     * @throws IOException exception
     */
    public void writeMapToFile(String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);

        writeContinentsToFile(writer);

        writeCountriesToFile(writer);

        writeBordersToFile(writer);

        writer.close();
    }

    /**
     * Write continent information to file
     *
     * @param writer file writer
     * @throws IOException file not found or not exist
     */
    public void writeContinentsToFile(FileWriter writer) throws IOException {
        ArrayList<Continent> continents = GameBoard.getInstance().getGameBoardMap().getContinents();
        writer.write("[continents]\n");

        for (Continent continent : continents) {
            writer.write(continent.getName() + " " + continent.getArmy() + "\n");
        }

        writer.write("\n");
    }

    /**
     * Write borders information to file
     *
     * @param writer file writer
     * @throws IOException file not found or not exist
     */
    public void writeBordersToFile(FileWriter writer) throws IOException {
        writer.write("[borders]\n");

        for (Country country : GameBoard.getInstance().getGameBoardMap().getCountries()) {
            ArrayList<Country> neighbors = country.getNeighbors();

            StringBuilder neighborLine = new StringBuilder(Integer.toString(country.getOrder()));

            for (Country neighbor : neighbors) {
                neighborLine.append(" ").append(neighbor.getOrder());
            }

            writer.write(neighborLine + "\n");
        }
    }

    /**
     * Write countries information to file
     *
     * @param writer file writer
     * @throws IOException file not found or not exist
     */
    public void writeCountriesToFile(FileWriter writer) throws IOException {
        ArrayList<Country> countries = GameBoard.getInstance().getGameBoardMap().getCountries();
        writer.write("[countries]\n");
        for (Country country : countries) {
            writer.write(country.getOrder() + " "
                         + country.getName() + " "
                         + country.getContinent().getOrder() + " "
                         + country.getCoordinate().getX() + " "
                         + country.getCoordinate().getY() + "\n");
        }
        writer.write("\n");
    }

    /**
     * adds new continent
     *
     * @param continentName  name of the continent
     * @param continentValue the amount of army for the new continent
     * @param order          the continent other in the list (start with 1 as the map file structure
     *                       indicate)
     */
    public void addContinent(String continentName, String continentValue, int... order) {
        if (!Parser.checkValidInputNumber(continentValue)) {
            ConsolePrinter.printFormat("Invalid Input");
        }

        if (!GameBoard.getInstance().getGameBoardMap().isContinentExisted(continentName)) {
            GameBoard.getInstance()
                     .getGameBoardMap()
                     .getContinents()
                     .add(new Continent(continentName,
                                        Integer.parseInt(continentValue)));

            ConsolePrinter.printFormat("New continent added: %s with %s armies",
                                       continentName,
                                       continentValue);
        } else {
            ConsolePrinter.printFormat("Continent with name %s existed", continentName);
        }
    }

    /**
     * add new country from map file
     *
     * @param order          the order of country in the list, start with 1
     * @param name           the country name (no space allowed)
     * @param continentOrder the other of the continent that new country belongs to
     * @param coordinate     the position of the country on a visual map (not used)
     */
    public void addCountryFromMapFile(int order,
                                      String name,
                                      int continentOrder,
                                      Coordinate coordinate) {
        for (Continent continent : GameBoard.getInstance().getGameBoardMap().getContinents()) {
            if (continent.getOrder() == continentOrder) {
                Country country = new Country(order, name, coordinate, continent);
                GameBoard.getInstance().getGameBoardMap().updateCountryContinent(country, continent);
            }
        }
    }

    /**
     * add border from country to others country
     *
     * @param countryOrder        the location of first country in the border graph
     * @param borderWithCountries the location of other countries in the border graph
     */
    public void addBorders(int countryOrder, int... borderWithCountries) {
        int[][] graph = GameBoard.getInstance().getGameBoardMap().getBorders();
        for (int borderWithCountry : borderWithCountries) {
            graph[countryOrder - 1][borderWithCountry - 1] = 1;
            graph[borderWithCountry - 1][countryOrder - 1] = 1;
        }
    }

}
