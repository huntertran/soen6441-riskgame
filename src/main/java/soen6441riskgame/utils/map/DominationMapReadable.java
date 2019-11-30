package soen6441riskgame.utils.map;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * domination map reader interface, for adapter pattern
 */
public interface DominationMapReadable extends MapReadable {
    /**
     * load the domination map from file
     * 
     * @param fileName path to file
     * @throws IOException if file not found or read file error
     */
    void loadMap(String fileName) throws IOException;

    /**
     * load domination map components
     * 
     * @param lines lines in file
     * @param index current line
     * @return end of the current block
     */
    int loadMapComponents(List<String> lines, int index);

    /**
     * load continents
     * 
     * @param lines            lines in file
     * @param currentLineIndex current line
     * @return end of the current block
     */
    int loadContinentsFromFile(int currentLineIndex, List<String> lines);

    /**
     * load countries
     * 
     * @param lines            lines in file
     * @param currentLineIndex current line
     * @return end of the current block
     */
    int loadCountriesFromFile(int currentLineIndex, List<String> lines);

    /**
     * load borders from file
     * 
     * @param lines            lines in file
     * @param currentLineIndex current line
     * @return end of the current block
     */
    int loadBordersFromFile(int currentLineIndex, List<String> lines);

    /**
     * write map to domination file
     * 
     * @param fileName path to file
     * @throws IOException if file not found
     */
    void writeMapToFile(String fileName) throws IOException;

    /**
     * write continents to domination map file
     * 
     * @param writer the file writer
     * @throws IOException if cannot write to file
     */
    void writeContinentsToFile(FileWriter writer) throws IOException;

    /**
     * write borders to file
     * 
     * @param writer the file writer
     * @throws IOException if cannot write to file
     */
    void writeBordersToFile(FileWriter writer) throws IOException;

    /**
     * write countries to domination map files
     * 
     * @param writer the file writer
     * @throws IOException if cannot write to file
     */
    void writeCountriesToFile(FileWriter writer) throws IOException;
}
