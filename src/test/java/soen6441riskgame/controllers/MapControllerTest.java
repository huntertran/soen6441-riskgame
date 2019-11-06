package soen6441riskgame.controllers;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.Parser;

/**
 * Tests the Map Controller methods.
 */
public class MapControllerTest {
    MapController mapController = new MapController();

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
     * This loadMapTest function tests if the map loaded from a file is working properly.
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
        Assertions.assertTrue(GameBoard.getInstance()
                                       .getGameBoardMap()
                                       .getContinents()
                                       .get(0)
                                       .getName()
                                       .equals("North_Africa"));
    }

    /**
     * Tests add Continent method It calls addContinent method and tests whether the continent is
     * created and has the same control value as passed.
     */
    @ParameterizedTest
    @CsvSource({ "Russia, 11",
                 "Asia, 14" })
    public void addContinentTest(String continentName, String continentValue) {
        // Action
        mapController.addContinent(continentName, continentValue);

        // Assertions
        Assertions.assertTrue(mapController.isContinentExisted(continentName));
        Assertions.assertEquals(continentValue,
                                Integer.toString(mapController.getContinentFromName(continentName).getArmy()));
    }

    /**
     * Tests remove continent method And tests whether the continent has been removed And the countries
     * in that continent are now existing without continent
     */
    @ParameterizedTest
    @CsvSource({ "Russia, 11, Vietnam, Laos",
                 "Asia, 14, India, Pakistan" })
    public void removeContinentTest(String continent,
                                    String continentValue,
                                    String country1,
                                    String country2) {
        // Setup
        mapController.addContinent(continent, continentValue);
        mapController.addCountry(country1, continent);
        mapController.addCountry(country2, continent);

        // Action
        mapController.removeContinent(continent);

        // Assertions
        Assertions.assertNull(mapController.getContinentFromName(continent));
        Assertions.assertFalse(mapController.isCountryExisted(country1));
        Assertions.assertFalse(mapController.isCountryExisted(country2));
    }

    /**
     * Tests add Country method It calls the add country method and tests whether that country has been
     * created.
     */
    @ParameterizedTest
    @CsvSource({ "Russia, 11, Vietnam, Laos",
                 "Asia, 14, India, Pakistan" })
    public void addCountryTest(String continent1,
                               String continent1_value,
                               String country1,
                               String country2) {
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
     * Tests remove Country method It calls the remove country method and tests whether that country has
     * been removed.
     */
    @ParameterizedTest
    @CsvSource({ "Russia, 11, Vietnam, Laos",
                 "Asia, 14, India, Pakistan" })
    public void removeCountryTest(String continent1,
                                  String continent1_value,
                                  String country1,
                                  String country2) {
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
     * Tests add Neighbor method It calls the add neighbor method and tests whether the 2 countries are
     * neighbors.
     */
    @ParameterizedTest
    @CsvSource({ "Vietnam, Laos, Cambodia, Thailand" })
    public void addNeighborTest(String country1,
                                String country2,
                                String country3,
                                String country4) {
        // Setup
        mapController.addContinent("Russia", "14");
        mapController.addCountry(country1, "Russia");
        mapController.addCountry(country2, "Russia");
        mapController.addCountry(country3, "Russia");
        mapController.addCountry(country4, "Russia");

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
     * Tests edit Continent method It removes continent using edit continent method and tests whether
     * that continent does not exist.
     */
    @ParameterizedTest
    @ValueSource(strings = { "Russia",
                             "Asia",
                             "Europe" })
    public void removeContinentTest(String continent1) {
        // Action
        String arguments[] = { "-remove", continent1 };
        mapController.editContinent(arguments);

        // Assertions
        Assertions.assertFalse(mapController.isContinentExisted(continent1));
    }

    /**
     * Tests edit Country method It adds country using edit country method and tests whether that
     * country exists.
     */
    @ParameterizedTest
    @CsvSource({ "-add, Vietnam, 1",
                 "-add, India, 1" })
    public void editCountryCommandTest(String arg, String country1, String countryExisted) {
        // Setup
        String continent = "Russia";
        mapController.addContinent(continent, "11");
        boolean isCountryExisted = Parser.parseWithDefault(countryExisted, 0) == 1;

        // Action
        mapController.editCountry(new String[] { arg, country1, continent });
        boolean actualIsCountryExisted = mapController.isCountryExisted(country1);

        // Assertions
        Assertions.assertEquals(isCountryExisted, actualIsCountryExisted);
    }

    /**
     * Tests edit Country method It removes country using edit country method and tests whether that
     * country does not exist.
     */
    @ParameterizedTest
    @ValueSource(strings = { "Vietnam",
                             "Thailand" })
    public void removeCountryCommand(String country) {
        // Setup
        String continent = "Russia";
        mapController.addContinent(continent, "11");
        mapController.addCountry(country, continent);

        // Action
        String arguments[] = { "-remove", country };
        mapController.editCountry(arguments);

        // Assertions
        Assertions.assertFalse(mapController.isCountryExisted(country));
    }

    /**
     * Tests remove Neighbor method It calls the remove neighbor method and tests whether the 2
     * countries are not neighbors.
     */
    @ParameterizedTest
    @CsvSource({ "c1,c2,c3,c4",
                 "b1,b2,b3,b4",
                 "a1,a2,a3,a4" })
    public void removeNeighborTest(String country1,
                                   String country2,
                                   String country3,
                                   String country4) {
        // Setup
        String continent = "Asia";
        mapController.addContinent(continent, "14");
        mapController.addCountry(country1, continent);
        mapController.addCountry(country2, continent);
        mapController.addCountry(country3, continent);
        mapController.addCountry(country4, continent);
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
     * Tests edit Neighbor method It adds/remove neighbor using edit neighbor method and tests whether
     * the 2 countries are neighbors.
     */
    @ParameterizedTest
    @CsvSource({ "-add, c1,c2, 1",
                 "-add, b1,b2,1",
                 "-add, a1,a2, 1",
                 "-remove, c1,c2,0",
                 "-remove, b1,b2,0",
                 "-remove, a1,a2, 0" })
    public void editNeighborTest(String arg,
                                 String country1,
                                 String country2,
                                 String neighboringCountry) {
        // Setup
        String continent = "Asia";
        mapController.addContinent(continent, "14");
        mapController.addCountry(country1, continent);
        mapController.addCountry(country2, continent);

        boolean isNeighbor = Parser.parseWithDefault(neighboringCountry, 0) == 1;

        String arguments[] = { arg, country1, country2 };

        // Action
        mapController.editNeighbor(arguments);

        Country countryObject1 = GameBoard.getInstance().getGameBoardMap().getCountryFromName(country1);
        Country countryObject2 = GameBoard.getInstance().getGameBoardMap().getCountryFromName(country2);

        // Assertions
        Assertions.assertEquals(isNeighbor, countryObject1.isNeighboringCountries(countryObject2));
    }

    /**
     * it tests the saveMap method and checks whether the map is saved correctly or not.
     * @throws IOException
     */
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
/**
     * it tests if the map is valid or not by checking if sufficient
     * number of countries are added.
     * @throws IOException
     */
    @Test
    public void validateMapNotEnoughCountryTest() throws IOException {
        // setup
        String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        mapController.loadMap(filePath);

        // action: make the map invalid
        while (GameBoard.getInstance()
                        .getGameBoardMap()
                        .getCountries()
                        .size() > 5) {
            mapController.removeCountry(GameBoard.getInstance()
                                                 .getGameBoardMap()
                                                 .getCountries()
                                                 .get(0)
                                                 .getName());
        }

        // assert
        Assertions.assertFalse(mapController.isMapValid());
    }

    /**
     * it tests if there is an isolated country on the map, which if true, will make map invalid.
     * @throws IOException
     */
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

    /**
     * it tests for the validity of the map by after removal of countries from a continent.
     * @throws IOException
     */
    @Test
    public void validateMapEmptyContinentTest() throws IOException {
        // setup
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

    /**
     * Test for un-connected graph. The explanation can be found at
     * https://github.com/huntertran/soen6441-riskgame/wiki/Connected-Graph-Validation-Unit-Test
     * 
     * @throws IOException
     */
    @Test
    public void validateMapNotConnectedContinentTest() throws IOException {
        // setup
        String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        mapController.loadMap(filePath);

        // making the map invalid
        // remove border of Tunisia and Algeria
        GameBoard.getInstance().getGameBoardMap().getBorders()[2][1] = 0;
        GameBoard.getInstance().getGameBoardMap().getBorders()[1][2] = 0;

        // add border for Malta and Spain
        GameBoard.getInstance().getGameBoardMap().getBorders()[4][0] = 1;
        GameBoard.getInstance().getGameBoardMap().getBorders()[0][4] = 1;

        // assert
        Assertions.assertFalse(mapController.isMapValid());
    }
}
