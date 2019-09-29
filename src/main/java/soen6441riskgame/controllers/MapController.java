package main.java.soen6441riskgame.controllers;

import java.util.Scanner;

import main.java.soen6441riskgame.singleton.GameMap;

public final class MapController {
    public void start(String[]... args) {
        Scanner scanner = new Scanner(System.in);
        scanner.close();
    }

    public void editContinent(String[] args) {
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

    public void editCountry(String[] args) {
        // -add and -remove
        String countryCommand = args[0].toLowerCase();

        switch (countryCommand) {
        case "-add": {
            addCountry(args[1], args[2]);
            break;
        }
        case "-remove": {
            removeCountry(args[1]);
            break;
        }
        }
    }

    public void editNeighbor(String[] args) {
        // -add and -remove
        String neighborCommand = args[0].toLowerCase();

        switch (neighborCommand) {
        case "-add": {
            addNeighbor(args[1], args[2]);
            break;
        }
        case "-remove": {
            removeNeighbor(args[1]);
            break;
        }
        }
    }

    public void showMap() {

    }

    public void saveMap(String fileName) {

    }

    public void editMap(String fileName) {

    }

    public boolean validateMap() {
        boolean result = false;

        return result;
    }

    public void loadMap(String fileName) {
        GameMap.getInstance().Continents.size();
    }

    public void addContinent(String continentName, String continentValue) {

    }

    public void removeContinent(String continentName) {

    }

    public void addCountry(String countryName, String continentName) {

    }

    public void removeCountry(String countryName) {

    }

    public void addNeighbor(String countryName, String neighborCountryName) {

    }

    public void removeNeighbor(String countryName) {

    }
}