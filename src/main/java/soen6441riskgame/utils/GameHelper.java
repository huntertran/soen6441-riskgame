package soen6441riskgame.utils;

import java.util.Random;

public class GameHelper {
    /**
     * it executes the dice roll.
     *
     * @return int it returns the random number from 1 to 6 on the dice.
     */
    public static int rollDice() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }

    /**
     * it returns the maximum value and second max value from the array of dice values
     *
     * @param inputArray dice-values the array of the dice values rolled
     * @param secondMax secondMax if second max is true, then second max value is returned. else only the
     *                max value is returned.
     *
     * @return int it returns the maximum value or second max value based on the value of secondMax
     *         flag
     *
     */
    public static int getMax(int[] inputArray, boolean secondMax) {
        int maxValue = inputArray[0];
        int maxIndex = 0;
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i] > maxValue) {
                maxValue = inputArray[i];
                maxIndex = i;
            }
        }

        if (secondMax) {
            int secondMaxValue = -1;
            for (int i = 0; i < inputArray.length; i++) {
                if (inputArray[i] > secondMaxValue && maxIndex != i) {
                    secondMaxValue = inputArray[i];
                }
            }
            maxValue = secondMaxValue;
        }

        return maxValue;
    }
}
