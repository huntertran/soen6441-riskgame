package soen6441riskgame.utils;

/**
 * Helper to parse
 */
public class Parser {

    /**
     * Parse int with default value
     * @param number string to parse
     * @param defaultVal default return value if parse error
     * @return
     */
    public static int parseWithDefault(String number, int defaultVal) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}