package soen6441riskgame.singleton;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;

import java.util.ArrayList;

public class GameMap {
    private static final GameMap instance = new GameMap();
    private String mapName;
    private ArrayList<Continent> continents = new ArrayList<Continent>();
    private ArrayList<Country> countries = new ArrayList<Country>();
    private int[][] borders;

    private GameMap() {
    };

    public int[][] getBorders() {
        return borders;
    }

    public void setBorders(int[][] graph) {
        this.borders = graph;
    }

    public ArrayList<Continent> getContinents() {
        return continents;
    }

    public void setContinents(ArrayList<Continent> continents) {
        this.continents = continents;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }

    public static GameMap getInstance() {
        return instance;
    }

    public void reset() {
        mapName = "";
        continents = new ArrayList<Continent>();
        countries = new ArrayList<Country>();
        borders = new int[1][1];
    }

    public void showContinents() {
        for (Continent continent : continents) {
            continent.view();
        }
    }

    public void showCountries() {
        for (Country country : countries) {
            country.view();
        }
    }
}