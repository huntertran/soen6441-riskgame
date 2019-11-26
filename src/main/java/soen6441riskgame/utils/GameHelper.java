package soen6441riskgame.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
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
     * @param secondMax  secondMax if second max is true, then second max value is returned. else only
     *                   the max value is returned.
     *
     * @return int it returns the maximum value or second max value based on the value of secondMax flag
     *
     */
    public static int getMax(int[] inputArray, boolean secondMax) {
        if (inputArray == null || inputArray.length == 0) {
            return 0;
        }

        IntSummaryStatistics stat = Arrays.stream(inputArray).summaryStatistics();
        int max = stat.getMax();

        if (secondMax) {
            int secondMaxValue = inputArray[0];
            for (int value : inputArray) {
                if (value < max && value >= secondMaxValue) {
                    secondMaxValue = value;
                }
            }

            return secondMaxValue;
        } else {
            return max;
        }
    }

    /**
     * A function to generate a random number with minimum and maximum range.
     *
     * @param min minimum random value generator
     * @param max maximum range of random value
     *
     * @return int random value
     *
     */
    public static int randomNumberGenerator(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    public static <T> int countDistinct(T[] array) {
        // add all the elements to the HashSet
        HashSet<T> hashSet = new HashSet<T>(Arrays.asList(array));

        // return the size of hash set as it consists of all Unique elements
        return hashSet.size();
    }
}
