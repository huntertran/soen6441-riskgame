package soen6441riskgame.utils;

/**
 * Helper to parse
 */
public class Parser {

    /**
     * Parse int with default value
     *
     * @param number       string to parse
     * @param defaultValue default return value if parse error
     * @return return default value if parse error
     */
    public static int parseWithDefault(String number, int defaultValue) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * check if input is a valid number
     *
     * @param value the string to check
     * @return false if the string is double, float, string, negative or very large (long and
     *         Double.isInfinite())
     */
    public static boolean checkValidInputNumber(String value) {
        boolean flag = true;
        try {
            // check for double, float, string
            int num = Integer.parseInt(value);

            // check negative value
            if (num < 0) {
                flag = false;
            }

            // check long value
            if (!((num == Math.floor(num)) && !Double.isInfinite(num))) {
                flag = false;
            }
            return flag;
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input. (Cannot be negative, decimal or string)");
            return false;
        }
    }
}