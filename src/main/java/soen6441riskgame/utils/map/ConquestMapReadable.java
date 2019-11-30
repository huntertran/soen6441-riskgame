package soen6441riskgame.utils.map;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public interface ConquestMapReadable {
    void loadConquestMap(String fileName) throws IOException;

    int loadContinentsFromConquestFile(int currentLineIndex, List<String> lines);

    int loadCountriesFromConquestFile(int currentLineIndex, List<String> lines);

    int loadBordersFromConquestFile(int currentLineIndex, List<String> lines);

    void writeMapToConquestFile(String fileName) throws IOException;

    void writeContinentsToConquestFile(FileWriter writer) throws IOException;

    void writeBordersToConquestFile(FileWriter writer) throws IOException;

    void writeCountriesToConquestFile(FileWriter writer) throws IOException;
}
