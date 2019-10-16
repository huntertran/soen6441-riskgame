package soen6441riskgame.models;

/**
 * indicate an object is printable to console with/without indentation
 */
public interface Viewable {
    public void view(int indent);

    public default void view() {
        view(0);
    }

    public default void printIndent(int indent) {
        for (int index = 0; index < indent; index++) {
            System.out.print("    ");
        }
    }
}