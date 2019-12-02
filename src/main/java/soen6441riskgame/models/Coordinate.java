package soen6441riskgame.models;

import com.google.gson.annotations.Expose;

/**
 * Coordinate of an entity on bitmap file, not used in console application
 */
public class Coordinate {
    @SuppressWarnings("checkstyle:MemberName")
    @Expose
    private int x;

    @SuppressWarnings("checkstyle:MemberName")
    @Expose
    private int y;

    /**
     * constructor
     *
     * @param x horizontal value
     * @param y vertical value
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * get horizontal value
     *
     * @return horizontal value
     */
    public int getY() {
        return y;
    }

    /**
     * get vertical value
     *
     * @return vertical value
     */
    public int getX() {
        return x;
    }
}