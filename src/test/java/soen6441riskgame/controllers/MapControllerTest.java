package test.java.soen6441riskgame.controllers;

import org.junit.Assert;
import org.junit.Test;

import main.java.soen6441riskgame.controllers.MapController;
import main.java.soen6441riskgame.singleton.GameMap;

public class MapControllerTest {

    @Test
    public void loadMapTest() {
        // Setup
        String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        MapController mapController = new MapController();

        // Action
        mapController.loadMap(filePath);

        // Assert
        Assert.assertTrue(GameMap.getInstance().getContinents().get(0).getName() == "North_Africa");
    }

    public void editContinentTest() {
        // Setup
        MapController map = new MapController();
        String arguments[] = { "-add", "Asia", "10" };

        // Action
        map.editContinent(arguments);

        // Assert
        Assert.assertTrue(map.isContinentExisted("Asia"));
    }
}
