package test.java.soen6441riskgame.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.Test;

import main.java.soen6441riskgame.controllers.MapController;
import main.java.soen6441riskgame.singleton.GameMap;

public class MapControllerTest {

	MapController mapController;
	
	/**
	 * This function will run before every test case
	 */
	@Before
	public void runBeforeEachTestCase() {
		mapController = new MapController();
		mapController.resetMap();
	}

	/**
	 * This function will run after all test cases have been executed
	 */
	@AfterClass
	public static void doAfterClass() {
		MapController mapController = new MapController();
		mapController.resetMap();
	}
	
    @Test
    public void loadMapTest() {
        // Setup
        String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";

        // Action
        mapController.loadMap(filePath);

        // Assert
        Assert.assertTrue(GameMap.getInstance().getContinents().get(0).getName().equals("North_Africa"));
    }
    
    @Test
    public void showMapTest() {
        // Setup
    	mapController.addContinent("Asia","15");
    	mapController.addCountry("Bangladesh", "Asia");
    	mapController.addCountry("Myanmmar", "Asia");
    	
        // Action
        mapController.showMap();
    }
    
    @Test
    public void addContinentTest() {
        // Action
        mapController.addContinent("Antartica", "10");

        // Assert
        Assert.assertTrue(mapController.isContinentExisted("Antartica"));
        Assert.assertEquals(10, mapController.getContinentFromName("Antartica").getArmy());
    }
    
    @Test
    public void removeContinentTest() {
    	//Setup
    	mapController.addContinent("Australia", "20");
    	
    	//Action
    	mapController.removeContinent("Australia");
    	
    	//Assert
    	Assert.assertNull(mapController.getContinentFromName("Australia"));
    	
    }
    @Test
    public void addCountryTest() {
    	//Setup
    	mapController.addContinent("Asia","15");
    	
    	//Action
    	mapController.addCountry("Bhutan", "Asia");
    	
    	//Assert
    	Assert.assertTrue(mapController.isCountryExisted("Bhutan"));
    }
    
    @Test
    public void removeCountryTest() {
    	//Setup
    	mapController.addContinent("Asia","15");
    	mapController.addCountry("Vietnam", "Asia");
    	
    	//Action
    	mapController.removeCountry("Vietnam");
    	
    	//Assert
    	Assert.assertFalse(mapController.isCountryExisted("Vietnam"));
    }
    
    @Test
    public void addNeighborTest() {
    	//Setup
    	mapController.addContinent("Asia","15");
    	mapController.addCountry("Bangladesh", "Asia");
    	mapController.addCountry("Myanmmar", "Asia");
    	
    	//Action
    	mapController.addNeighbor("Bangladesh", "Myanmmar");
    	
    	//Assert
    	Assert.assertTrue(mapController.isNeighboringCountries("Bangladesh", "Myanmmar"));
    }
    
    @Test
    public void editContinentTest1() {
        // Action
        String arguments[] = { "-add", "Asia", "10" };
        mapController.editContinent(arguments);

        // Assert
        Assert.assertTrue(mapController.isContinentExisted("Asia"));
    }
    
    @Test
    public void editContinentTest2() {
        // Action
        String arguments[] = { "-remove", "Asia" };
        mapController.editContinent(arguments);

        // Assert
        Assert.assertFalse( mapController.isContinentExisted("Asia") );
    }
    
    @Test
    public void editCountryTest1() {
    	//Setup
    	mapController.addContinent("Europe", "10");
 
        // Action
    	String arguments[] = { "-add", "Spain", "Europe" };
        mapController.editCountry(arguments);

        // Assert
        Assert.assertTrue(mapController.isCountryExisted("Spain"));
    }
    
    @Test
    public void editCountryTest2() {
    	//Setup
    	mapController.addContinent("Asia", "50");
    	mapController.addCountry("India", "Asia");
        // Action
    	String arguments[] = { "-remove", "India" };
        mapController.editCountry(arguments);

        // Assert
        Assert.assertFalse(mapController.isCountryExisted("India"));
    }
    
    @Test
    public void removeNeighborTest() {
    	//Setup
    	mapController.addContinent("Asia", "15");
    	mapController.addCountry("China", "Asia");
    	mapController.addCountry("Pakistan", "Asia");
    	mapController.addNeighbor("China", "Pakistan");
    	
    	//Action
    	mapController.removeNeighbor("China", "Pakistan");
    	
    	//Assert
    	Assert.assertFalse(mapController.isNeighboringCountries("China","Pakistan"));
    }
    
    @Test
    public void editNeighborTest1() {
    	//Setup
    	mapController.addContinent("Asia", "11");
    	mapController.addCountry("South Korea", "Asia");
    	mapController.addCountry("North Korea", "Asia");
    	String arguments[] = { "-add", "South Korea", "North Korea" };

        // Action
        mapController.editNeighbor(arguments);

        // Assert
        Assert.assertTrue(mapController.isNeighboringCountries("South Korea","North Korea"));
    }
    
    @Test
    public void editNeighborTest2() {
    	//Setup
    	mapController.addContinent("Asia", "11");
    	mapController.addCountry("South Korea", "Asia");
    	mapController.addCountry("North Korea", "Asia");
    	String arguments[] = { "-add", "South Korea", "North Korea" };

        // Action
        mapController.editNeighbor(arguments);

        // Assert
        Assert.assertTrue(mapController.isNeighboringCountries("South Korea","North Korea"));
    }
    
}
