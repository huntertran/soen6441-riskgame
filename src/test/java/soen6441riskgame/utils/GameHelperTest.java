package soen6441riskgame.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import soen6441riskgame.helpers.IntArrayConverter;

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
}