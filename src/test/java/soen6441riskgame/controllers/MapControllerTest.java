package test.java.soen6441riskgame.controllers;

import org.junit.Assert;
import org.junit.Before;

import java.util.Arrays;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import main.java.soen6441riskgame.controllers.MapController;
import main.java.soen6441riskgame.singleton.GameMap;

@RunWith(Parameterized.class)
public class MapControllerTest {

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { "Asia", "Vietnam", "Laos", "13" },
				{ "Europe", "Spain", "France", "12" }, { "South Africa", "Guinea", "Sierra Leone", "8" },
				{ "North America", "Canada", "USA", "11" } });
	}

	private String continent1;
	private String country1;
	private String country2;
	private String continent1_value;

	MapController mapController;

	public MapControllerTest(String continent1, String country1, String country2, String continent1_value) {
		this.continent1 = continent1;
		this.country1 = country1;
		this.country2 = country2;
		this.continent1_value = continent1_value;
	}

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

	/*
	 * @Test public void showMapTest() { // Setup
	 * mapController.addContinent(continent1, continent1_value);
	 * mapController.addCountry(country1, continent1);
	 * mapController.addCountry(country2, continent1);
	 * 
	 * // Action mapController.showMap(); }
	 */

	@Test
	public void addContinentTest() {
		// Action
		mapController.addContinent(continent1, continent1_value);

		// Assert
		Assert.assertTrue(mapController.isContinentExisted(continent1));
		Assert.assertEquals(continent1_value,
				Integer.toString(mapController.getContinentFromName(continent1).getArmy()));
	}

	@Test
	public void removeContinentTest() {
		// Setup
		mapController.addContinent(continent1, continent1_value);

		// Action
		mapController.removeContinent(continent1);

		// Assert
		Assert.assertNull(mapController.getContinentFromName(continent1));

	}

	@Test
	public void addCountryTest() {
		// Setup
		mapController.addContinent(continent1, continent1_value);

		// Action
		mapController.addCountry(country1, continent1);

		// Assert
		Assert.assertTrue(mapController.isCountryExisted(country1));
	}

	@Test
	public void removeCountryTest() {
		// Setup
		mapController.addContinent(continent1, continent1_value);
		mapController.addCountry(country1, continent1);

		// Action
		mapController.removeCountry(country1);

		// Assert
		Assert.assertFalse(mapController.isCountryExisted(country1));
	}

	@Test
	public void addNeighborTest() {
		// Setup
		mapController.addContinent(continent1, continent1_value);
		mapController.addCountry(country1, continent1);
		mapController.addCountry(country2, continent1);

		// Action
		mapController.addNeighbor(country1, country2);

		// Assert
		Assert.assertTrue(mapController.isNeighboringCountries(country1, country2));
	}

	@Test
	public void editContinentTest1() {
		// Action
		String arguments[] = { "-add", continent1, continent1_value };
		mapController.editContinent(arguments);

		// Assert
		Assert.assertTrue(mapController.isContinentExisted(continent1));
	}

	@Test
	public void editContinentTest2() {
		// Action
		String arguments[] = { "-remove", continent1 };
		mapController.editContinent(arguments);

		// Assert
		Assert.assertFalse(mapController.isContinentExisted(continent1));
	}

	@Test
	public void editCountryTest1() {
		// Setup
		mapController.addContinent(continent1, continent1_value);

		// Action
		String arguments[] = { "-add", country1, continent1 };
		mapController.editCountry(arguments);

		// Assert
		Assert.assertTrue(mapController.isCountryExisted(country1));
	}

	@Test
	public void editCountryTest2() {
		// Setup
		mapController.addContinent(continent1, continent1_value);
		mapController.addCountry(country1, continent1);
		// Action
		String arguments[] = { "-remove", country1 };
		mapController.editCountry(arguments);

		// Assert
		Assert.assertFalse(mapController.isCountryExisted(country1));
	}

	@Test
	public void removeNeighborTest() {
		// Setup
		mapController.addContinent(continent1, continent1_value);
		mapController.addCountry(country1, continent1);
		mapController.addCountry(country2, continent1);
		mapController.addNeighbor(country1, country2);

		// Action
		mapController.removeNeighbor(country1, country2);

		// Assert
		Assert.assertFalse(mapController.isNeighboringCountries(country1, country2));
	}

	@Test
	public void editNeighborTest1() {
		// Setup
		mapController.addContinent(continent1, continent1_value);
		mapController.addCountry(country1, continent1);
		mapController.addCountry(country2, continent1);
		String arguments[] = { "-add", country1, country2 };

		// Action
		mapController.editNeighbor(arguments);

		// Assert
		Assert.assertTrue(mapController.isNeighboringCountries(country1, country2));
	}

	@Test
	public void editNeighborTest2() {
		// Setup
		mapController.addContinent(continent1, continent1_value);
		mapController.addCountry(country1, continent1);
		mapController.addCountry(country2, continent1);
		String arguments[] = { "-add", country1, country2 };

		// Action
		mapController.editNeighbor(arguments);

		// Assert
		Assert.assertTrue(mapController.isNeighboringCountries(country1, country2));
	}

}
