package soen6441riskgame.models;

/**
 * Coordinate of an entity on bitmap file, not used in console application
 */
public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}