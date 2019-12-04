package soen6441riskgame.utils;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.io.PrintStream;

import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.presenter.WindowPane;

/**
 * Helper to print out to console with proper format
 */
public class ConsolePrinter {

    private static boolean isJUnitTest = true;

    private static boolean isDebug = false;

    /*
     * determine if current program is run from a test runner
     */
    static {
        setJUnitTest();
        setIsDebug();
    }

    /**
     * is current program run from a test runner
     *
     * @return is current program run from a test runner
     */
    public static boolean isJUnitTest() {
        return isJUnitTest;
    }

    /**
     * check if current program run from a test runner
     */
    private static void setJUnitTest() {
        StackTraceElement[] list = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : list) {
            if (element.getClassName().startsWith("org.junit.")) {
                isJUnitTest = true;
                return;
            }
        }

        isJUnitTest = false;
    }

    /**
     * check if is debug or not
     */
    private static void setIsDebug() {
        isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean()
                                                        .getInputArguments()
                                                        .toString()
                                                        .indexOf("-Xdebug") == 1;
    }

    /**
     * print the string with format and args replaced, end with a line break
     *
     * @param format string with format
     * @param args   args in string
     */
    public static void printFormat(String format, Object... args) {
        printFormat(GameBoard.getInstance().standardPrintStream, format, args);
    }

    /**
     * print to custom print stream
     *
     * @param customPrintStream the PrintStream to print
     * @param format            string with format
     * @param args              args in string
     */
    public static void printFormat(PrintStream customPrintStream, String format, Object... args) {
        printFormat(customPrintStream, true, format, args);
    }

    /**
     * print to custom print stream
     *
     * @param customPrintStream the PrintStream to print
     * @param isPrintNewLine    is print new line at then end
     * @param format            string with format
     * @param args              args in string
     */
    public static void printFormat(PrintStream customPrintStream,
                                   boolean isPrintNewLine,
                                   String format,
                                   Object... args) {
        if (isJUnitTest && !isDebug) {
            return;
        }

        customPrintStream.format(format, args);
        if (isPrintNewLine) {
            customPrintStream.println();
        }
    }

    /**
     * print the array in matrix style, with header
     *
     * @param array   array to print
     * @param headers if header is empty or null, the index will be printed
     */
    public static void print2dArray(int[][] array, String[] headers) {
        PrintStream standardPrintStream = GameBoard.getInstance().standardPrintStream;

        if (array.length == 0) {
            printFormat(standardPrintStream, "Empty array");
            return;
        }

        if (headers == null || headers.length == 0) {
            headers = new String[array[0].length];

            for (int index = 0; index < headers.length; index++) {
                headers[index] = "[" + index + "]";
            }
        }

        printFormat(standardPrintStream, false, "\t");

        for (String header : headers) {
            printFormat(standardPrintStream, false, header + "\t");
        }

        printFormat(standardPrintStream, "");

        for (int row = 0; row < array.length; row++) {
            printFormat(standardPrintStream, false, headers[row] + "\t");
            for (int col = 0; col < array[row].length; col++) {
                printFormat(standardPrintStream, false, array[row][col] + "\t");
            }
            printFormat(standardPrintStream, "");
        }
    }

    /**
     * create a new window for separated output
     *
     * @param title  title of new window
     * @param width  width of new window
     * @param height height of new window
     * @return the new window
     */
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

    /**
     * create world view with countries
     */
    public static void createWorldView() {
        if (isJUnitTest && !isDebug) {
            return;
        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container content = frame.getContentPane();
        content.add(GameBoard.getInstance().getGameBoardMap().getWordView());
        frame.setSize(600, 600);
        frame.setVisible(true);
    }
}
