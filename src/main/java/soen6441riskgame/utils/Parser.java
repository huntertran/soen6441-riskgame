package soen6441riskgame.utils;

/**
 * Helper to parse
 */
public class Parser {

    /**
     * Parse int with default value
     * @param number string to parse
     * @param defaultValue default return value if parse error
     * @return
     */
    public static int parseWithDefault(String number, int defaultValue) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}