package test.java.soen6441riskgame.controllers;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import main.java.soen6441riskgame.controllers.MapController;
import main.java.soen6441riskgame.models.Continent;
import main.java.soen6441riskgame.singleton.GameMap;

public class MapControllerTest {

    @Test
    public void editContinentTest() {
        // Setup
        GameMap.getInstance().setContinents(new ArrayList<Continent>());

        // Action
        GameMap.getInstance().getContinents().add(new Continent("Asia"));

        // Assert
        Assert.assertTrue(GameMap.getInstance().getContinents().get(0).getName() == "Asia");
    }

    @Test
    public void loadMapTest(){
        // Setup
        String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        MapController mapController = new MapController();

        // Action
        mapController.loadMap(filePath);

        // Assert
        Assert.assertTrue(GameMap.getInstance().getContinents().get(0).getName() == "North_Africa");
    }

}
