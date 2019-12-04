package soen6441riskgame.utils.map;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Coordinate;
import soen6441riskgame.models.Country;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;
import soen6441riskgame.utils.Parser;

/**
 * common interface for both conquest and domination map
 */
public interface MapReadable {
    /**
     * adds new continent
     *
     * @param continentName  name of the continent
     * @param continentValue the amount of army for the new continent
     * @param order          the continent other in the list (start with 1 as the map file structure
     *                       indicate)
     */
    default void addContinent(String continentName, String continentValue, int... order) {
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
    default void addCountry(int order,
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
    default void addBorders(int countryOrder, int... borderWithCountries) {
        int[][] graph = GameBoard.getInstance().getGameBoardMap().getBorders();
        for (int borderWithCountry : borderWithCountries) {
            graph[countryOrder - 1][borderWithCountry - 1] = 1;
            graph[borderWithCountry - 1][countryOrder - 1] = 1;
        }

        GameBoard.getInstance().getGameBoardMap().getWordView().update(null, null);
    }

    /**
     * The map file stores data in blocks. This function check if the current data line is still in a
     * block or not
     *
     * @param currentLineIndex the current line index
     * @param lines            all the lines in map file
     * @return if the current data line is still in a block or not
     */
    default boolean isStillInCurrentDataBlock(int currentLineIndex, List<String> lines) {
        if (currentLineIndex < lines.size()) {
            String currentLine = lines.get(currentLineIndex);
            return !currentLine.equals("")
                   && (currentLine.contains(" ")
                       || currentLine.contains("=")
                       || currentLine.contains(","));
        }

        return false;
    }

    /**
     * load map from file
     *
     * @param fileName the exact path to map file, end with .map extension for example:
     *                 D://src/test/java/soen6441riskgame/maps/RiskEurope.map
     * @return lines in file
     * @throws IOException exception
     */
    default List<String> readMapFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);

        List<String> lines = Files.lines(path).collect(Collectors.toList());
        GameBoard.getInstance().reset();

        return lines;
    }
}
