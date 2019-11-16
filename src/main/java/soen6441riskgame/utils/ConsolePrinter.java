package soen6441riskgame.utils;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.presenter.WindowPane;

/**
 * Helper to print out to console with proper format
 */
public class ConsolePrinter {

    private static boolean isJUnitTest = true;

    static {
        setJUnitTest();
    }

    public static boolean isJUnitTest() {
        return isJUnitTest;
    }

    private static void setJUnitTest() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        List<StackTraceElement> list = Arrays.asList(stackTrace);
        for (StackTraceElement element : list) {
            if (element.getClassName().startsWith("org.junit.")) {
                isJUnitTest = true;
                return;
            }
        }

        isJUnitTest = false;
    }

    /**
     * print the string with format and args replaced, end with a line break
     *
     * @param format string with format
     * @param args   args in string
     */
    public static void printFormat(String format, Object... args) {
        // System.out.format(format, args);
        // System.out.println();
        printFormat(GameBoard.getInstance().standardPrintStream, format, args);
    }

    public static void printFormat(PrintStream customPrintStream, String format, Object... args) {
        customPrintStream.format(format, args);
        customPrintStream.println();
    }

    /**
     * print the array in matrix style, with header
     * 
     * @param array   array to print
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

    public static WindowPane createWindowPane(String title, int width, int height) {
        WindowPane windowPane = new WindowPane();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(windowPane);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle(title);
        return windowPane;
    }

    public static void printToNewWindow(PrintStream printStream, String content) {
        System.setOut(printStream);
        System.out.println(content);
    }
}