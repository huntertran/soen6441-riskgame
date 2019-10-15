package soen6441riskgame.utils;

public class ConsolePrinter {
    public static void printFormat(String format, Object ... args){
        System.out.format(format, args);
        System.out.println();
    }
}