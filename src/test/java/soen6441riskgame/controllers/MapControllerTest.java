package soen6441riskgame.controllers;

import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import soen6441riskgame.controllers.MapController;
import soen6441riskgame.singleton.GameMap;

@RunWith(Parameterized.class)
public class MapControllerTest {

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { "Russia", "Asia", "Vietnam", "Laos", "India", "Pakistan", "13", "12" },
				{ "North America", "Europe", "Spain", "France", "India", "Pakistan", "12", "13" },
				{ "Asia", "South Africa", "Guinea", "Sierra Leone", "India", "Pakistan", "8", "20" },
				{ "South Africa", "North America", "Canada", "USA", "India", "Pakistan", "11", "14" } });
	}

	private String continent1;
	private String continent2;
	private String country1;
	private String country2;
	private String country3;
	private String country4;
	private String continent1_value;
	private String continent2_value;

	MapController mapController = new MapController();

	public MapControllerTest(String continent1, String continent2, String country1, String country2, String country3,
			String country4, String continent1_value, String continent2_value) {
		this.continent1 = continent1;
		this.continent2 = continent2;
		this.country1 = country1;
		this.country2 = country2;
		this.country3 = country3;
		this.country4 = country4;
		this.continent1_value = continent1_value;
		this.continent2_value = continent2_value;
	}

	/**
	 * This function will run before every test case
	 */
	@Before
	public void runBeforeEachTestCase() {
		mapController.resetMap();
	}

	/**
	 * This loadMapTest function tests if the map loaded from a file is working
	 * properly.
	 *
	 * @throws IOException
	 */
	@Test
	public void loadMapTest() throws IOException {
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

	/**
	 * Tests add Continent method It calls addContinent method and tests whether the
	 * continent is created and has the same control value as passed.
	 */
	@Test
	public void addContinentTest() {
		// Action
		mapController.addContinent(continent1, continent1_value);
		mapController.addContinent(continent2, continent2_value);

		// Assert
		Assert.assertTrue(mapController.isContinentExisted(continent1));
		Assert.assertEquals(continent1_value,
				Integer.toString(mapController.getContinentFromName(continent1).getArmy()));
		Assert.assertTrue(mapController.isContinentExisted(continent2));
		Assert.assertEquals(continent2_value,
				Integer.toString(mapController.getContinentFromName(continent2).getArmy()));
	}

	/**
	 * Tests remove continent method And tests whether the continent has been
	 * removed And the countries in that continent are now existing without
	 * continent
	 */
	@Test
	public void removeContinentTest() {
		// Setup
		mapController.addContinent(continent1, continent1_value);
		mapController.addCountry(country1, continent1);
		mapController.addCountry(country2, continent1);

		// Action
		mapController.removeContinent(continent1);

		// Assert
		Assert.assertNull(mapController.getContinentFromName(continent1));
		Assert.assertNotEquals(continent1, GameMap.getInstance().getCountryFromName(country1).getContinent());
		Assert.assertNotEquals(continent1, GameMap.getInstance().getCountryFromName(country2).getContinent());

	}

	/**
	 * Tests add Country method It calls the add country method and tests whether
	 * that country has been created.
	 */
	@Test
	public void addCountryTest() {
		// Setup
		mapController.addContinent(continent1, continent1_value);

		// Action
		mapController.addCountry(country1, continent1);
		mapController.addCountry(country2, continent1);

		// Assert
		Assert.assertTrue(mapController.isCountryExisted(country1));
		Assert.assertTrue(mapController.isCountryExisted(country2));
	}

	/**
	 * Tests remove Country method It calls the remove country method and tests
	 * whether that country has been removed.
	 */
	@Test
	public void removeCountryTest() {
		// Setup
		mapController.addContinent(continent1, continent1_value);
		mapController.addCountry(country1, continent1);
		mapController.addCountry(country2, continent1);

		// Action
		mapController.removeCountry(country1);
		mapController.removeCountry(country2);

		// Assert
		Assert.assertFalse(mapController.isCountryExisted(country1));
		Assert.assertFalse(mapController.isCountryExisted(country2));
	}

	/**
	 * Tests add Neighbor method It calls the add neighbor method and tests whether
	 * the 2 countries are neighbors.
	 */
	@Test
	public void addNeighborTest() {
		// Setup
		mapController.addContinent(continent1, continent1_value);
		mapController.addCountry(country1, continent1);
		mapController.addCountry(country2, continent1);
		mapController.addCountry(country3, continent1);
		mapController.addCountry(country4, continent1);

		// Action
		mapController.addNeighbor(country1, country2);
		mapController.addNeighbor(country2, country3);
		mapController.addNeighbor(country3, country4);

		// Assert
		Assert.assertTrue(GameMap.getInstance().isNeighboringCountries(country1, country2));
		Assert.assertTrue(GameMap.getInstance().isNeighboringCountries(country3, country4));
		Assert.assertTrue(GameMap.getInstance().isNeighboringCountries(country2, country3));
	}

	/**
	 * Tests edit Continent method It adds continent using edit continent method and
	 * tests whether that continent exists.
	 */
	@Test
	public void editContinentTest1() {
		// Action
		String arguments[] = { "-add", continent1, continent1_value };
		mapController.editContinent(arguments);

		// Assert
		Assert.assertTrue(mapController.isContinentExisted(continent1));
		Assert.assertEquals(continent1_value,
				Integer.toString(mapController.getContinentFromName(continent1).getArmy()));
	}

	/**
	 * Tests edit Continent method It removes continent using edit continent method
	 * and tests whether that continent does not exist.
	 */
	@Test
	public void editContinentTest2() {
		// Action
		String arguments[] = { "-remove", continent1 };
		mapController.editContinent(arguments);

		// Assert
		Assert.assertFalse(mapController.isContinentExisted(continent1));
	}

	/**
	 * Tests edit Country method It adds country using edit country method and tests
	 * whether that country exists.
	 */
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

	/**
	 * Tests edit Country method It removes country using edit country method and
	 * tests whether that country does not exist.
	 */
	@Test
	public void editCountryTest2() {
		// Setup
		mapController.addContinent(continent1, continent1_value);
		mapController.addCountry(country1, continent1);
		mapController.addCountry(country2, continent1);

		// Action
		String arguments[] = { "-remove", country1 };
		mapController.editCountry(arguments);
		String arguments1[] = { "-remove", country2 };
		mapController.editCountry(arguments1);

		// Assert
		Assert.assertFalse(mapController.isCountryExisted(country1));
		Assert.assertFalse(mapController.isCountryExisted(country2));
	}

	/**
	 * Tests remove Neighbor method It calls the remove neighbor method and tests
	 * whether the 2 countries are not neighbors.
	 */
	@Test
	public void removeNeighborTest() {
		// Setup
		mapController.addContinent(continent1, continent1_value);
		mapController.addCountry(country1, continent1);
		mapController.addCountry(country2, continent1);
		mapController.addCountry(country3, continent1);
		mapController.addCountry(country4, continent1);
		mapController.addNeighbor(country1, country2);
		mapController.addNeighbor(country2, country3);
		mapController.addNeighbor(country3, country4);

		// Action
		mapController.removeNeighbor(country1, country2);
		mapController.removeNeighbor(country2, country3);

		// Assert
		Assert.assertFalse(GameMap.getInstance().isNeighboringCountries(country1, country2));
		Assert.assertFalse(GameMap.getInstance().isNeighboringCountries(country2, country3));
		Assert.assertTrue(GameMap.getInstance().isNeighboringCountries(country3, country4));
	}

	/**
	 * Tests edit Neighbor method It adds neighbor using edit neighbor method and
	 * tests whether the 2 countries are neighbors.
	 */
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
		Assert.assertTrue(GameMap.getInstance().isNeighboringCountries(country1, country2));
	}

	/**
	 * Tests edit Neighbor method It removes neighbor using edit neighbor method and
	 * tests whether the 2 countries are not neighbors.
	 */
	@Test
	public void editNeighborTest2() {
		// Setup
		mapController.addContinent(continent1, continent1_value);
		mapController.addCountry(country1, continent1);
		mapController.addCountry(country2, continent1);
		String arguments[] = { "-remove", country1, country2 };

		// Action
		mapController.editNeighbor(arguments);

		// Assert
		Assert.assertFalse(GameMap.getInstance().isNeighboringCountries(country1, country2));
	}

	@Test
	public void saveMapTest() throws IOException {
		// setup
		String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
		mapController.loadMap(filePath);

		String savedMapFilePath = "./src/test/java/soen6441riskgame/maps/SavedRiskEurope.map";

		// action
		try {
			mapController.saveMap(savedMapFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// assert
		Assert.assertTrue(true);
	}

}
