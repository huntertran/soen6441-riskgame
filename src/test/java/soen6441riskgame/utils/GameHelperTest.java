package soen6441riskgame.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import soen6441riskgame.helpers.IntArrayConverter;
import soen6441riskgame.helpers.StringArrayConverter;

import java.util.ArrayList;

/**
 * test GameHelper functions
 */
public class GameHelperTest {
    /**
     * test get max
     * 
     * @param inputArray     int array
     * @param isGetSecondMax is get second max
     * @param expectedResult expected result
     */
    @ParameterizedTest
    @CsvSource({
                 "1;2;3;4;5;6;7,False,7",
                 "1;2;3;4;5;6;7,True,6",
                 ",True,0",
                 ",False,0"
    })
    public void getMaxTest(@ConvertWith(IntArrayConverter.class) int[] inputArray,
                           boolean isGetSecondMax,
                           int expectedResult) {
        // action
        int actualResult = GameHelper.getMax(inputArray, isGetSecondMax);

        // assert
        assertEquals(expectedResult, actualResult);
    }

    /**
     * test count distinct value from array
     * 
     * @param inputArray            input array
     * @param expectedDistinctValue expected result
     */
    @ParameterizedTest
    @CsvSource({
                 "map1;map2;map3,3",
                 "map1;map2;map1,2",
                 ",0"
    })
    public void countDistinct(@ConvertWith(StringArrayConverter.class) String[] inputArray,
                              int expectedDistinctValue) {
        int actualDistinctValue = GameHelper.countDistinct(inputArray);

        assertEquals(expectedDistinctValue, actualDistinctValue);
    }

    /**
     * test get random int in range
     * 
     * @param min min value
     * @param max max value
     */
    @ParameterizedTest
    @CsvSource({
                 "1,1",
                 "1,2",
                 "1,6",
                 "0,0"
    })
    public void nextRandomIntInRangeTest(int min, int max) {
        int result = GameHelper.nextRandomIntInRange(min, max);

        assertTrue(min <= result && result <= max);
    }

    /**
     * test get random elements in an array
     * 
     * @param list                  list to get elements from
     * @param numberOfItemToPick    number of elements to pick
     * @param expectedSizeAfterPick the list should not be modified after picked
     */
    @ParameterizedTest
    @CsvSource({
                 "1;2;3;4;5;6,3,6",
                 "1,1,1"
    })
    public void getRandomElementsTest(@ConvertWith(IntArrayConverter.class) int[] list,
                                      int numberOfItemToPick,
                                      int expectedSizeAfterPick) {
        ArrayList<Integer> originalList = new ArrayList<>();
        for (Integer item : list) {
            originalList.add(item);
        }

        ArrayList<Integer> picked = GameHelper.getRandomElements(originalList, numberOfItemToPick);
        for (Integer item : picked) {
            assertTrue(originalList.contains(item));
        }

        assertEquals(expectedSizeAfterPick, originalList.size());
    }
}
