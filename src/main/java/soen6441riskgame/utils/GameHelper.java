package soen6441riskgame.utils;

import java.util.ArrayList;
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

    /**
     * A function to generate a random number with minimum and maximum range.
     *
     * @param min minmum random value generator
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
     * @param ArrayList<Country> all country list of a player
     *
     * @return ArrayList<Country> return ArrayList of countries with army count more than one.
     *
     */
    public static ArrayList<Country> filterAttackableCountries(ArrayList<Country> lstCountries) {
        ArrayList<Country> filteredList = new ArrayList<>();
        
        for (Country country : lstCountries) {
            if(country.getArmyAmount() > 1) {
                filteredList.add(country);
            }
        }

        return filteredList;
    }
}