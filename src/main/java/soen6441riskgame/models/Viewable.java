package main.java.soen6441riskgame.models;

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