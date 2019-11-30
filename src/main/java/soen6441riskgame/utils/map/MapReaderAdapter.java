package soen6441riskgame.utils.map;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import soen6441riskgame.enums.ConquestMapPart;
import soen6441riskgame.enums.DominationMapPart;
import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Coordinate;
import soen6441riskgame.models.Country;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;

public class MapReaderAdapter implements DominationMapReadable, ConquestMapReadable {
    /**
     * load map from file
     *
     * @param fileName the exact path to map file, end with .map extension for example:
     *                 D://src/test/java/soen6441riskgame/maps/RiskEurope.map
     * @throws IOException exception
     */
    @Override
    public void loadMap(String fileName) throws IOException {
        List<String> lines = readMapFile(fileName);
        for (int index = 0; index < lines.size(); index++) {
            index = loadMapComponents(lines, index);
        }

        ConsolePrinter.printFormat("Map loaded");
    }

    @Override
    public int loadMapComponents(List<String> lines, int index) {
        String currentLine = lines.get(index);
        if (currentLine.startsWith(";")) {
            return index;
        }

        String firstWord = currentLine.split(" ")[0];
        DominationMapPart part = DominationMapPart.fromString(firstWord);

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
        return index;
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
        writer.write(DominationMapPart.CONTINENTS.getPart() + "\n");

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
        writer.write(DominationMapPart.BORDERS.getPart() + "\n");

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
        writer.write(DominationMapPart.COUNTRIES.getPart() + "\n");
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
        List<String> lines = readMapFile(fileName);
        for (int index = 0; index < lines.size(); index++) {
            index = loadConquestMapComponents(lines, index);
        }

        ConsolePrinter.printFormat("Map loaded");
    }

    @Override
    public int loadConquestMapComponents(List<String> lines, int index) {
        String currentLine = lines.get(index);
        String firstWord = currentLine.split(" ")[0];
        ConquestMapPart part = ConquestMapPart.fromString(firstWord);

        switch (part) {
            case MAP: {
                index = loadConquestMapInfo(lines, index);
                break;
            }
            case CONTINENTS: {
                index = loadContinentsFromConquestFile(index, lines);
                break;
            }
            case TERRITORIES: {
                index = loadCountriesFromConquestFile(index, lines);
                break;
            }
            case NONE: {
                break;
            }
        }
        return index;
    }

    @Override
    public int loadConquestMapInfo(List<String> lines, int currentLineIndex) {
        for (int index = currentLineIndex + 1; isStillInCurrentDataBlock(index, lines); index++) {
            currentLineIndex = index;
        }

        return currentLineIndex + 1;
    }

    @Override
    public int loadContinentsFromConquestFile(int currentLineIndex, List<String> lines) {
        int continentOrder = 1;
        for (int index = currentLineIndex + 1; isStillInCurrentDataBlock(index, lines); index++) {
            String currentLine = lines.get(index);
            String[] fragments = currentLine.split("=");
            String continentName = legalizeString(fragments[0]);
            int continentArmy = Integer.parseInt(fragments[1]);

            addContinent(continentName, Integer.toString(continentArmy), continentOrder);

            currentLineIndex = index;
            continentOrder++;
        }

        return currentLineIndex + 1;
    }

    @Override
    public int loadCountriesFromConquestFile(int currentLineIndex, List<String> lines) {
        // Cockpit01,658,355,Cockpit,Cockpit02,Territory33
        // Territory name,x,y,Continent,neighbor1,neighbor1
        int originalLineIndex = currentLineIndex;

        for (int index = currentLineIndex + 1; isStillInCurrentDataBlock(index, lines); index++) {
            String currentLine = lines.get(index);

            String[] fragments = currentLine.split(",");

            String countryName = legalizeString(fragments[0]);

            int x = Integer.parseInt(fragments[1]);
            int y = Integer.parseInt(fragments[2]);
            Coordinate coordinate = new Coordinate(x, y);

            String continentName = legalizeString(fragments[3]);

            int countryOrder = GameBoard.getInstance()
                                        .getGameBoardMap()
                                        .getCountries()
                                        .size()
                               + 1;

            int continentOrder = GameBoard.getInstance()
                                          .getGameBoardMap()
                                          .getContinentFromName(continentName)
                                          .getOrder();

            addCountry(countryOrder, countryName, continentOrder, coordinate);
        }

        originalLineIndex = loadBordersFromConquestFile(lines, originalLineIndex);

        return originalLineIndex + 1;
    }

    private int loadBordersFromConquestFile(List<String> lines, int originalLineIndex) {
        // load borders
        // Cockpit01,658,355,Cockpit,Cockpit02,Territory33
        // Territory name,x,y,Continent,neighbor1,neighbor1

        int numberOfCountry = GameBoard.getInstance().getGameBoardMap().getCountries().size();
        GameBoard.getInstance().getGameBoardMap().setBorders(new int[numberOfCountry][numberOfCountry]);

        for (int index = originalLineIndex + 1; isStillInCurrentDataBlock(index, lines); index++) {
            String currentLine = lines.get(index);

            String[] fragments = currentLine.split(",");

            int countryOrder = GameBoard.getInstance()
                                        .getGameBoardMap()
                                        .getCountryFromName(legalizeString(fragments[0]))
                                        .getOrder();

            // neighbors start from fragments[4]

            int[] borderWithCountries = new int[fragments.length - 4];

            for (int neighborIndex = 4; neighborIndex < fragments.length; neighborIndex++) {
                int neighborOrder = GameBoard.getInstance()
                                             .getGameBoardMap()
                                             .getCountryFromName(legalizeString(fragments[neighborIndex]))
                                             .getOrder();

                borderWithCountries[neighborIndex - 4] = neighborOrder;
            }

            addBorders(countryOrder, borderWithCountries);

            originalLineIndex = index;
        }
        return originalLineIndex;
    }

    @Override
    public void writeMapToConquestFile(String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);

        writeContinentsToConquestFile(writer);

        writeCountriesToConquestFile(writer);

        writer.close();
    }

    @Override
    public void writeContinentsToConquestFile(FileWriter writer) throws IOException {
        ArrayList<Continent> continents = GameBoard.getInstance().getGameBoardMap().getContinents();
        writer.write(ConquestMapPart.CONTINENTS.getPart() + "\n");

        for (Continent continent : continents) {
            writer.write(continent.getName() + "=" + continent.getArmy() + "\n");
        }

        writer.write("\n");
    }

    @Override
    public void writeCountriesToConquestFile(FileWriter writer) throws IOException {
        // Cockpit01,658,355,Cockpit,Cockpit02,Territory33
        ArrayList<Country> countries = GameBoard.getInstance().getGameBoardMap().getCountries();
        writer.write(ConquestMapPart.TERRITORIES.getPart() + "\n");
        for (Country country : countries) {

            String countryData = country.getName() + ","
                                 + country.getCoordinate().getX() + ","
                                 + country.getCoordinate().getY() + ","
                                 + country.getContinent().getName() + ",";

            ArrayList<Country> neighbors = country.getNeighbors();

            for (Country neighbor : neighbors) {
                countryData += neighbor.getName() + ",";
            }

            countryData = countryData.substring(0, countryData.length() - 1);

            writer.write(countryData + "\n");
        }

        writer.write("\n");
    }
}
