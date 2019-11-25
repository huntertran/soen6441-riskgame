package soen6441riskgame.models;

/**
 * for checking a value is in boundary or not
 */
public class Boundary {
    private int lower;
    private int upper;

    /**
     * constructor
     * 
     * @param lower lower value
     * @param upper upper value
     */
    public Boundary(int lower, int upper) {
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * check if the value is inside the boundary exclusively
     * 
     * @param value the value to check
     * @return is inside the boundary exclusively
     */
    private boolean isInBoundaryExclusive(int value) {
        return lower <= value && value <= upper;
    }

    /**
     * check if the value is inside the boundary
     * 
     * @param value the value to check
     * @return is inside the boundary
     */
    private boolean isInBoundary(int value) {
        return lower < value && value < upper;
    }

    /**
     * check if the value is inside the boundary
     * 
     * @param value       the value to check
     * @param isExclusive true if the value can be equal to it's boundary
     * @return is inside the boundary
     */
    public boolean isInBoundary(int value, boolean isExclusive) {
        if (isExclusive) {
            return isInBoundaryExclusive(value);
        } else {
            return isInBoundary(value);
        }
    }
}