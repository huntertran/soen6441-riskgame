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

    public boolean checkValidInput(String value) {
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
        }
        catch(NumberFormatException e) {
            System.out.println("Invalid Input. (Cannot be negative, decimal or string)");
            return false;
        }
    }
}