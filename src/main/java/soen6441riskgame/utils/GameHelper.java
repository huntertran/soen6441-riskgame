package soen6441riskgame.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
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
    public static int nextRandomIntInRange(int min, int max) {
        Random random = new Random();
        //return random.nextInt((max - min) + 1) + min;
        if(max == min)
            return min;
        return (random.nextInt(max)>=min)? random.nextInt(max) : random.nextInt(max) + min;
    }

    /**
     * generate random int from that 1 <= result <= max
     * 
     * @param max max included
     * @return 1 <= result <= max
     */
    public static int nextRandomInt(int max) {
        return nextRandomIntInRange(1, max);
    }

    // Function select an element base on index and return
    // an element
    @SuppressWarnings("unchecked")
    public static ArrayList<Integer> getRandomElements(ArrayList<Integer> list, int totalItems) {
        Random random = new Random();
        ArrayList<Integer> originalList = (ArrayList<Integer>) list.clone();
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < totalItems; i++) {
            int randomIndex = random.nextInt(originalList.size());

            result.add(originalList.get(randomIndex));

            originalList.remove(randomIndex);
        }

        return result;
    }

    /*
     * Get the all the keys associated with given Value V from map
     */
    public static <K, V> List<K> getAllKeysForValue(Map<K, V> maps, V value) {
        List<K> listOfKeys = null;

        // Check if Map contains the given value
        if (maps.containsValue(value)) {
            // Create an Empty List
            listOfKeys = new ArrayList<>();

            // Iterate over each entry of map using entrySet
            for (Map.Entry<K, V> entry : maps.entrySet()) {
                // Check if value matches with given value
                if (entry.getValue().equals(value)) {
                    // Store the key from entry to the list
                    listOfKeys.add(entry.getKey());
                }
            }
        }

        // Return the list of keys whose value matches with given value.
        return listOfKeys;
    }

    public static <T> int countDistinct(T[] array) {
        // add all the elements to the HashSet
        HashSet<T> hashSet = new HashSet<T>(Arrays.asList(array));

        // return the size of hash set as it consists of all Unique elements
        return hashSet.size();
    }
}
