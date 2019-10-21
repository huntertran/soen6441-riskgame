package soen6441riskgame.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.singleton.GameBoard;

public class MapControllerTest {

    @ParameterizedTest
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { "Russia", "Asia", "Vietnam", "Laos", "India", "Pakistan", "13", "12" },
                { "North America", "Europe", "Spain", "France", "India", "Pakistan", "12", "13" },
                { "Asia", "South Africa", "Guinea", "Sierra Leone", "India", "Pakistan", "8", "20" },
                { "South Africa", "North America", "Canada", "USA", "India", "Pakistan", "11", "14" } });
    }

    // private String continent1;
    // private String continent2;
    // private String country1;
    // private String country2;
    // private String country3;
    // private String country4;
    // private String continent1_value;
    // private String continent2_value;

    MapController mapController = new MapController();

    // public MapControllerTest(String continent1, String continent2, String
    // country1, String country2, String country3,
    // String country4, String continent1_value, String continent2_value) {
    // this.continent1 = continent1;
    // this.continent2 = continent2;
    // this.country1 = country1;
    // this.country2 = country2;
    // this.country3 = country3;
    // this.country4 = country4;
    // this.continent1_value = continent1_value;
    // this.continent2_value = continent2_value;
    // }

    /**
     * This function will run before every test case
     */
    @BeforeEach
    public void runBeforeEachTestCase() {
        GameBoard testingInstanceGameMap = new GameBoard();
        GameBoard.setTestingInstance(testingInstanceGameMap);
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

        // Assertions
        Assertions.assertTrue(
                GameBoard.getInstance().getGameBoardMap().getContinents().get(0).getName().equals("North_Africa"));
    }

    /**
     * Tests add Continent method It calls addContinent method and tests whether the
     * continent is created and has the same control value as passed.
     */
    @ParameterizedTest
    @CsvSource({
        "Russia, 11",
        "Asia, 14"
    })
    public void addContinentTest(String continentName, String continentValue) {
        // Action
        mapController.addContinent(continentName, continentValue);

        // Assertions
        Assertions.assertTrue(mapController.isContinentExisted(continentName));
        Assertions.assertEquals(continentValue,
                Integer.toString(mapController.getContinentFromName(continentName).getArmy()));
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

        // Assertions
        Assertions.assertNull(mapController.getContinentFromName(continent1));
        Assertions.assertFalse(mapController.isCountryExisted(country1));
        Assertions.assertFalse(mapController.isCountryExisted(country2));
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

        // Assertions
        Assertions.assertTrue(mapController.isCountryExisted(country1));
        Assertions.assertTrue(mapController.isCountryExisted(country2));
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

        // Assertions
        Assertions.assertFalse(mapController.isCountryExisted(country1));
        Assertions.assertFalse(mapController.isCountryExisted(country2));
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

        Country countryObject1 = GameBoard.getInstance().getGameBoardMap().getCountryFromName(country1);
        Country countryObject2 = GameBoard.getInstance().getGameBoardMap().getCountryFromName(country2);
        Country countryObject3 = GameBoard.getInstance().getGameBoardMap().getCountryFromName(country3);
        Country countryObject4 = GameBoard.getInstance().getGameBoardMap().getCountryFromName(country4);

        // Assertions
        Assertions.assertTrue(countryObject1.isNeighboringCountries(countryObject2));
        Assertions.assertTrue(countryObject2.isNeighboringCountries(countryObject3));
        Assertions.assertTrue(countryObject3.isNeighboringCountries(countryObject4));
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

        // Assertions
        Assertions.assertTrue(mapController.isContinentExisted(continent1));
        Assertions.assertEquals(continent1_value,
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

        // Assertions
        Assertions.assertFalse(mapController.isContinentExisted(continent1));
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

        // Assertions
        Assertions.assertTrue(mapController.isCountryExisted(country1));
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

        // Assertions
        Assertions.assertFalse(mapController.isCountryExisted(country1));
        Assertions.assertFalse(mapController.isCountryExisted(country2));
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

        Country countryObject1 = GameBoard.getInstance().getGameBoardMap().getCountryFromName(country1);
        Country countryObject2 = GameBoard.getInstance().getGameBoardMap().getCountryFromName(country2);
        Country countryObject3 = GameBoard.getInstance().getGameBoardMap().getCountryFromName(country3);
        Country countryObject4 = GameBoard.getInstance().getGameBoardMap().getCountryFromName(country4);

        // Assertions
        Assertions.assertFalse(countryObject1.isNeighboringCountries(countryObject2));
        Assertions.assertFalse(countryObject2.isNeighboringCountries(countryObject3));
        Assertions.assertTrue(countryObject3.isNeighboringCountries(countryObject4));
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

        Country countryObject1 = GameBoard.getInstance().getGameBoardMap().getCountryFromName(country1);
        Country countryObject2 = GameBoard.getInstance().getGameBoardMap().getCountryFromName(country2);

        // Assertions
        Assertions.assertTrue(countryObject1.isNeighboringCountries(countryObject2));
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

        Country countryObject1 = GameBoard.getInstance().getGameBoardMap().getCountryFromName(country1);
        Country countryObject2 = GameBoard.getInstance().getGameBoardMap().getCountryFromName(country2);

        // Assertions
        Assertions.assertFalse(countryObject1.isNeighboringCountries(countryObject2));
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
        Assertions.assertTrue(true);
    }

    @Test
    public void validateMapNotEnoughCountryTest() throws IOException {
        // setup
        String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        mapController.loadMap(filePath);

        // action: make the map invalid
        while (GameBoard.getInstance().getGameBoardMap().getCountries().size() > 5) {
            mapController.removeCountry(GameBoard.getInstance().getGameBoardMap().getCountries().get(0).getName());
        }

        // assert
        Assertions.assertFalse(mapController.isMapValid());
    }

    @Test
    public void validateMapIsolatedCountryTest() throws IOException {
        // setup
        String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        mapController.loadMap(filePath);

        // action: make the map invalid
        Country targetCountry = GameBoard.getInstance().getGameBoardMap().getCountries().get(0);

        while (targetCountry.getNeighbors().size() > 0) {
            mapController.removeNeighbor(targetCountry.getName(), targetCountry.getNeighbors().get(0).getName());
        }

        // assert
        Assertions.assertFalse(mapController.isMapValid());
    }

    @Test
    public void validateMapEmptyContinentTest() throws IOException {
        // setup
        // String filePath = "C:/Users/t_tuan/Downloads/test.map";
        String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        mapController.loadMap(filePath);

        // action: make the map invalid
        Continent targetContinent = GameBoard.getInstance().getGameBoardMap().getContinents().get(0);

        System.out.println("Before remove countries:");
        GameBoard.getInstance().getGameBoardMap().printBorders();

        while (targetContinent.getCountries().size() > 0) {
            mapController.removeCountry(targetContinent.getCountries().get(0).getName());
        }

        System.out.println("After remove countries:");
        GameBoard.getInstance().getGameBoardMap().printBorders();

        // assert
        Assertions.assertFalse(mapController.isMapValid());
    }
}
