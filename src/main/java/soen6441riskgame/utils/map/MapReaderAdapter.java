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

public class MapReaderAdapter implements DominationMapReadable, ConquestMapReadable, MapReadable {

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

            addCountry(countryOrder, countryName, continentOrder, coordinate);

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

    @Override
    public void loadConquestMap(String fileName) throws IOException {
        // TODO Auto-generated method stub
    }

    @Override
    public int loadContinentsFromConquestFile(int currentLineIndex, List<String> lines) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int loadCountriesFromConquestFile(int currentLineIndex, List<String> lines) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int loadBordersFromConquestFile(int currentLineIndex, List<String> lines) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeMapToConquestFile(String fileName) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeContinentsToConquestFile(FileWriter writer) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeBordersToConquestFile(FileWriter writer) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeCountriesToConquestFile(FileWriter writer) throws IOException {
        // TODO Auto-generated method stub

    }

}
