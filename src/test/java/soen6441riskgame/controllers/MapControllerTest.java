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
        mapController.resetMap();

        // Action
        mapController.loadMap(filePath);

        // Assert
        Assert.assertTrue(GameMap.getInstance().getContinents().get(0).getName().equals("North_Africa"));
    }

    @Test
    public void editContinentTest1() {
        // Setup
        MapController mapController = new MapController();
        mapController.resetMap();
        String arguments[] = { "-add", "Asia", "10" };

        // Action
        mapController.editContinent(arguments);

        // Assert
        Assert.assertTrue(mapController.isContinentExisted("Asia"));
    }
    
    @Test
    public void editContinentTest2() {
        // Setup
        MapController mapController = new MapController();
        mapController.resetMap();
        String arguments[] = { "-remove", "Asia" };

        // Action
        mapController.editContinent(arguments);

        // Assert
        Assert.assertTrue( (mapController.isContinentExisted("Asia") == false) );
    }
    
    @Test
    public void editCountryTest() {
    	//Setup
    	MapController mapController = new MapController();
    	mapController.resetMap();
    	String arguments[] = { "-add", "Japan", "12" };

        // Action
        mapController.editCountry(arguments);

        // Assert
        Assert.assertTrue(mapController.isCountryExisted("Japan"));
    }
    
    @Test
    public void editNeighborTest() {
    	//Setup
    	MapController mapController = new MapController();
    	mapController.resetMap();
    	String arguments[] = { "-add", "Japan", "China" };

        // Action
        mapController.editNeighbor(arguments);

        // Assert
        Assert.assertTrue(mapController.checkIfNeighboringCountries("Japan","China"));
    }
    
    @Test
    public void addContinentTest() {
    	//Setup
    	MapController mapController = new MapController();
    	mapController.resetMap();
    	String arguments[] = { "Antartica", "10" };

        // Action
        mapController.editNeighbor(arguments);

        // Assert
        Assert.assertTrue(mapController.checkIfNeighboringCountries("Japan","China"));
    }
    
}
