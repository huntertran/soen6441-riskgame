package main.java.soen6441riskgame.controllers;

import java.util.Scanner;

public final class MapController {
    public void start(String[]... args) {
        Scanner scanner = new Scanner(System.in);
        scanner.close();
    }

    public void editContinents(String[] args) {
        // -add and -remove
        String continentCommand = args[0].toLowerCase();

        switch (continentCommand) {
        case "-add": {
            addContinent(args[1], args[2]);
            break;
        }
        case "-remove": {
            removeContinent(args[1]);
            break;
        }
        }
    }

    public void addContinent(String continentName, String continentValue) {

    }

    public void removeContinent(String continentName) {

    }
}