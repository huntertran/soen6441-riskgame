package soen6441riskgame.models;

/**
 * indicate an object is printable to console with/without indentation
 */
public interface Viewable {

    /**
     * view with a number of indent
     * @param indent number of indent
     */
    public void view(int indent);

    /**
     * view without indent
     */
    public default void view() {
        view(0);
    }

    /**
     * print the indent with 4 spaces
     * @param indent number of indent
     */
    public default void printIndent(int indent) {
        for (int index = 0; index < indent; index++) {
            System.out.print("    ");
        }
    }
}