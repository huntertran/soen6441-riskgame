package soen6441riskgame.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import soen6441riskgame.helpers.IntArrayConverter;
import soen6441riskgame.helpers.StringArrayConverter;

public class GameHelperTest {
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
}
