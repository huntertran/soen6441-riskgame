package soen6441riskgame.utils.map;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public interface ConquestMapReadable {
    void loadConquestMap(String fileName) throws IOException;

    int loadConquestMapComponents(List<String> lines, int index);

    int loadConquestMapInfo(List<String> lines, int index);

    int loadContinentsFromConquestFile(int currentLineIndex, List<String> lines);

    int loadCountriesFromConquestFile(int currentLineIndex, List<String> lines);

    void writeMapToConquestFile(String fileName) throws IOException;

    void writeContinentsToConquestFile(FileWriter writer) throws IOException;

    void writeCountriesToConquestFile(FileWriter writer) throws IOException;

    default String legalizeString(String illegalString){
        return illegalString.replace(" ", "_");
    }
}
