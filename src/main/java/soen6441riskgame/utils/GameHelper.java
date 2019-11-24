package soen6441riskgame.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Random;

import soen6441riskgame.models.Country;

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

    /**
     * A function to filter ArrayList of Countries which has army less than two
     *
     * @param countries all country list of a player
     *
     * @return return ArrayList of countries with army count more than one.
     *
     */
    public static ArrayList<Country> filterAttackableCountries(ArrayList<Country> countries) {
        ArrayList<Country> filteredList = new ArrayList<>();

        for (Country country : countries) {
            if (country.getArmyAmount() > 1) {
                filteredList.add(country);
            }
        }

        return filteredList;
    }
}