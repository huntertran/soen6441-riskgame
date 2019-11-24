package soen6441riskgame.helpers;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public class IntArrayConverter extends SimpleArgumentConverter {

    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
        if (source == null) {
            return new int[0];
        }
        if (source instanceof String && int[].class.isAssignableFrom(targetType)) {
            String[] intString = source.toString().split("\\s*;\\s*");
            int[] result = new int[intString.length];
            for (int index = 0; index < intString.length; index++) {
                result[index] = Integer.parseInt(intString[index]);
            }
            return result;
        } else {
            throw new IllegalArgumentException("Conversion from "
                                               + source.getClass()
                                               + " to "
                                               + targetType
                                               + " not supported.");
        }
    }
}