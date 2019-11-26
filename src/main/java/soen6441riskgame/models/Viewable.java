package soen6441riskgame.models;

import java.io.PrintStream;

import soen6441riskgame.singleton.GameBoard;

/**
 * indicate an object is printable to console with/without indentation
 */
public interface Viewable {

    // /**
    // * view with a number of indent
    // *
    // * @param indent number of indent
    // */
    // public void view(int indent);

    /**
     * view with a number of indent on custom printStream
     *
     * @param printStream the stream to print
     * @param indent      number of indent
     */
    void view(PrintStream printStream, int indent);

    /**
     * view without indent
     */
    default void view() {
        view(GameBoard.getInstance().standardPrintStream, 0);
        System.out.println(GameBoard.getInstance().standardPrintStream.toString());
    }

    default void view(int indent) {
        view(GameBoard.getInstance().standardPrintStream, indent);
    }

    /**
     * view without indent on custom printStream
     *
     * @param printStream the stream to print
     */
    default void view(PrintStream printStream) {
        view(printStream, 0);
    }

    /**
     * print the indent with 4 spaces
     *
     * @param indent number of indent
     */
    default void printIndent(int indent) {
        // for (int index = 0; index < indent; index++) {
        // System.out.print(" ");
        // }
        printIndent(GameBoard.getInstance().standardPrintStream, indent);
    }

    /**
     * print the indent with 4 spaces
     *
     * @param printStream the stream to print.
     * @param indent      number of indent
     */
    default void printIndent(PrintStream printStream, int indent) {
        for (int index = 0; index < indent; index++) {
            printStream.print("    ");
        }
    }
}
