package soen6441riskgame.utils.map;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public interface DominationMapReadable {
    void loadMap(String fileName) throws IOException;

    int loadMapComponents(List<String> lines, int index);

    int loadContinentsFromFile(int currentLineIndex, List<String> lines);

    int loadCountriesFromFile(int currentLineIndex, List<String> lines);

    int loadBordersFromFile(int currentLineIndex, List<String> lines);

    void writeMapToFile(String fileName) throws IOException;

    void writeContinentsToFile(FileWriter writer) throws IOException;

    void writeBordersToFile(FileWriter writer) throws IOException;

    void writeCountriesToFile(FileWriter writer) throws IOException;
}
