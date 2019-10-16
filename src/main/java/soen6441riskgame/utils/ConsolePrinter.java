package soen6441riskgame.utils;

public class ConsolePrinter {
    public static void printFormat(String format, Object... args) {
        System.out.format(format, args);
        System.out.println();
    }

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