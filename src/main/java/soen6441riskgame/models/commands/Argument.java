package soen6441riskgame.models.commands;

import soen6441riskgame.utils.Parser;

public class Argument {
    private Class<?> type;
    private String unparsedValue;

    public Argument(Class<?> type, String unparsedValue) {
        this.type = type;
        this.unparsedValue = unparsedValue;
    }

    public Class<?> getType() {
        return type;
    }

    public Parsable getValue() {
        return Parser.parseObject(unparsedValue, type);
    }

    public int getValueAsInt() {
        return Integer.parseInt(unparsedValue);
    }

    public String getUnparsedValue() {
        return unparsedValue;
    }
}