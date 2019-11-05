package soen6441riskgame.singleton;

/**
 * classes that implement this interface should support clearing data they're holding
 */
public interface Resettable {
    /**
     * clear data
     */
    void reset();
}