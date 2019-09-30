package main.java.soen6441riskgame.singleton;

import main.java.soen6441riskgame.models.Continent;
import main.java.soen6441riskgame.models.Country;

import java.util.ArrayList;

public class GameMap {
    private static final GameMap instance = new GameMap();
    private String mapName;
    private ArrayList<Continent> continents = new ArrayList<Continent>();
    private ArrayList<Country> countries = new ArrayList<Country>();
    private int[][] graph;

    private GameMap() {
    };

    public int[][] getGraph() {
        return graph;
    }

    public void setGraph(int[][] graph) {
        this.graph = graph;
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

    public void reset(){
        mapName = "";
        continents = new ArrayList<Continent>();
        countries = new ArrayList<Country>();
        graph = new int[1][1];
    }

    public void showContinents(){
        for(Continent continent : continents){

        }
    }
}