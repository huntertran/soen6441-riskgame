package main.java.soen6441riskgame.singleton;

import main.java.soen6441riskgame.models.Continent;
import main.java.soen6441riskgame.models.Country;

import java.util.ArrayList;

public class GameMap {
    private static final GameMap instance = new GameMap();
    private String MapName;
    private ArrayList<Continent> Continents = new ArrayList<Continent>();
    private ArrayList<Country> countries = new ArrayList<Country>();
    private int[][] Graph;

    private GameMap() {
    };

    public int[][] getGraph() {
        return Graph;
    }

    public void setGraph(int[][] graph) {
        this.Graph = graph;
    }

    public ArrayList<Continent> getContinents() {
        return Continents;
    }

    public void setContinents(ArrayList<Continent> continents) {
        this.Continents = continents;
    }

    public String getMapName() {
        return MapName;
    }

    public void setMapName(String mapName) {
        this.MapName = mapName;
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
}