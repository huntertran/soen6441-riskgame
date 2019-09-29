package main.java.soen6441riskgame.singleton;

import main.java.soen6441riskgame.models.Continent;

import java.util.ArrayList;

public class GameMap {
    private static final GameMap instance = new GameMap();

    private GameMap() {
    };

    public static GameMap getInstance() {
        return instance;
    }

    public ArrayList<Continent> Continents;

    public int[][] Graph;
    
    public boolean continentExists(String continentName) {
    	
    	for(Continent continent : Continents) {
    		if(continent.name == continentName)
    			return true;
    	}
    	return false;
    }
    
}