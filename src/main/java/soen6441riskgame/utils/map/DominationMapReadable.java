package soen6441riskgame.utils.map;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import soen6441riskgame.models.Coordinate;

public interface DominationMapReadable {
    void loadMap(String fileName) throws IOException;

    void resetMap();

    int loadContinentsFromFile(int currentLineIndex, List<String> lines);

    int loadCountriesFromFile(int currentLineIndex, List<String> lines);

    int loadBordersFromFile(int currentLineIndex, List<String> lines);

    boolean isStillInCurrentDataBlock(int currentLineIndex, List<String> lines);

    void writeMapToFile(String fileName) throws IOException;

    void writeContinentsToFile(FileWriter writer) throws IOException;

    void writeBordersToFile(FileWriter writer) throws IOException;

    void writeCountriesToFile(FileWriter writer) throws IOException;

    void addContinent(String continentName, String continentValue, int... order);

    void addCountry(int order,
                    String name,
                    int continentOrder,
                    Coordinate coordinate);

    void addBorders(int countryOrder, int... borderWithCountries);
}
