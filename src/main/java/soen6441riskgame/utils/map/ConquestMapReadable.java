package soen6441riskgame.utils.map;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * conquest map reader interface, for adapter pattern
 */
public interface ConquestMapReadable extends MapReadable {
    /**
     * load the conquest map from file
     * 
     * @param fileName path to file
     * @throws IOException if file not found or read file error
     */
    void loadConquestMap(String fileName) throws IOException;

    /**
     * load conquest map components
     * 
     * @param lines lines in file
     * @param index current line
     * @return end of the current block
     */
    int loadConquestMapComponents(List<String> lines, int index);

    /**
     * load conquest map info
     * 
     * @param lines lines in file
     * @param index current line
     * @return end of the current block
     */
    int loadConquestMapInfo(List<String> lines, int index);

    /**
     * load continents
     * 
     * @param lines            lines in file
     * @param currentLineIndex current line
     * @return end of the current block
     */
    int loadContinentsFromConquestFile(int currentLineIndex, List<String> lines);

    /**
     * load countries
     * 
     * @param lines            lines in file
     * @param currentLineIndex current line
     * @return end of the current block
     */
    int loadCountriesFromConquestFile(int currentLineIndex, List<String> lines);

    /**
     * write map to conquest file
     * 
     * @param fileName path to file
     * @throws IOException if file not found
     */
    void writeMapToConquestFile(String fileName) throws IOException;

    /**
     * write continents to conquest map file
     * 
     * @param writer the file writer
     * @throws IOException if cannot write to file
     */
    void writeContinentsToConquestFile(FileWriter writer) throws IOException;

    /**
     * write countries to conquest map files
     * 
     * @param writer the file writer
     * @throws IOException if cannot write to file
     */
    void writeCountriesToConquestFile(FileWriter writer) throws IOException;

    /**
     * convert string that contains space character to underscore
     * 
     * @param illegalString the original string
     * @return string that doesn't have space
     */
    default String legalizeString(String illegalString) {
        return illegalString.replace(" ", "_");
    }
}
