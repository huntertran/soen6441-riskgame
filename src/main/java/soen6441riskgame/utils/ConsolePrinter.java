package soen6441riskgame.utils;

/**
 * Helper to print out to console with proper format
 */
public class ConsolePrinter {
    /**
     * print the string with format and args replaced, end with a linebreak
     *
     * @param format string with format
     * @param args   args in string
     */
    public static void printFormat(String format, Object... args) {
        System.out.format(format, args);
        System.out.println();
    }

    /**
     * print the array in matrix style, with header
     * @param array
     * @param headers if header is empty or null, the index will be printed
     */
    public static void print2dArray(int[][] array, String[] headers) {
        if (headers == null || headers.length == 0) {
            if (array.length > 0) {
                headers = new String[array[0].length];

                for (int index = 0; index < headers.length; index++) {
                    headers[index] = "[" + index + "]";
                }
            }
        }

        System.out.print("\t");

        for (String header : headers) {
            System.out.print(header + "\t");
        }

        System.out.println();

        for (int row = 0; row < array.length; row++) {
            System.out.print(headers[row] + "\t");
            for (int col = 0; col < array[row].length; col++) {
                System.out.print(array[row][col] + "\t");
            }
            System.out.println();
        }
    }
}