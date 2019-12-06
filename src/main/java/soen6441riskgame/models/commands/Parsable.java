package soen6441riskgame.models.commands;

public interface Parsable<T> {
    T parse(String argument);
}