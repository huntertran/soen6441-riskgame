package main.java.soen6441riskgame.singleton;

import main.java.soen6441riskgame.models.Continent;
import java.util.List;

public class GameMap {
    private static final GameMap instance = new GameMap();

    private GameMap() {
    };

    public static GameMap getInstance() {
        return instance;
    }

    public List<Continent> Continents;

    public int[][] Graph;
}