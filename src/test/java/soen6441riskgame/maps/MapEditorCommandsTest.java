package soen6441riskgame.maps;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import soen6441riskgame.commands.MapEditorCommands;
import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

// TODO: Auto-generated Javadoc
/**
 * The Class MapModelTest.
 * 
 * 
 * @version 1.0.0
 */
public class MapEditorCommandsTest {
   
   /** The string for valid. */
   private StringBuffer stringForValid;
    
    /** The string for inValid. */
    private StringBuffer stringForInValid;
    
    /** The continents list test. */
    private ArrayList<String> continentsListTest = new ArrayList<>();
    
    /** The territories list test. */
    private ArrayList<String> territoriesListTest = new ArrayList<>();
    
    /** The map model. */
    private MapEditorCommands mapModel;

    /**
     * Sets the up.
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        stringForValid = new StringBuffer();
        stringForInValid = new StringBuffer();
        mapModel = new MapEditorCommands();

        String validMapString = "[Map]\n"
                + "author=Zakiya-Test\n"
                + "[Continents]\n"
                + "Continent1=10\n"
                + "Continent2=20\n"
                + "[Territories]\n"
                + "Country1,127,46,Continent1,Country2,Country3\n"
                + "Country2,193,85,Continent1,Country1,Country3,Country4\n"
                + "Country3,252,33,Continent1,Country5,Country1,Country2\n"
                + "Country4,314,94,Continent2,Country2,Country5\n"
                + "Country5,415,99,Continent2,Country4,Country3";
        stringForValid.append(validMapString);

        String inValidMapString = "[Map]\n"
                + "author=Zakiya-Test\n"
                + "[Continents]\n"
                + "Continent1=10\n"
                + "Continent2=20\n"
                + "[Territories]\n"
                + "Country1,127,46,Continent1,Country2,Country3\n"
                + "Country2,193,85,Continent1,Country1,Country3,Country4\n"
                + "Country3,252,33,Continent1,Country5,Country1,Country2\n"
                + "Country4,314,94,Continent2,Country2,Country5\n";
        stringForInValid.append(inValidMapString);

        continentsListTest.add("Continent1");
        continentsListTest.add("Continent2");
        territoriesListTest.add("Country1");
        territoriesListTest.add("Country2");
        territoriesListTest.add("Country3");
        territoriesListTest.add("Country4");
        territoriesListTest.add("Country5");

        Collections.sort(continentsListTest);
        Collections.sort(territoriesListTest);

    }


    /**
     * This method runs at the end of each test case.
     * @throws Exception if not
     */
    @After
    public void tearDown() throws Exception {
        stringForValid = null;
        stringForInValid = null;
        mapModel = null;
    }

    /**
     * This method tests the valid map file while reading.
     */
    @Test
    public void readMapFileTest() {
        ArrayList<String> continentsListFromFile = new ArrayList<String>();
        ArrayList<String> territoriesListFromFile = new ArrayList<String>();
        mapModel.readMapFile("src/main/java/soen6441riskgame/mapFiles/validMapTest.map");
        for (Continent conObj : mapModel.getContinentList()) {
            continentsListFromFile.add(conObj.getContinentName());
            for (Country terObj : conObj.getCountryList()) {
                if (!(territoriesListFromFile.contains(terObj.getCountryName()))) {
                    territoriesListFromFile.add(terObj.getCountryName());
                }
            }
        }
        Collections.sort(continentsListFromFile);
        Collections.sort(territoriesListFromFile);
        assertEquals(continentsListFromFile, continentsListTest);
        assertEquals(territoriesListFromFile, territoriesListTest);
    }


    /**
     * This method tests the valid map is created.
     * @throws Exception if not
     */
    @Test
    public void checkIfValidMapCreated() throws Exception {
        String mapName = "validMapTestCreated";
        boolean isMapCreated = mapModel.createValidateAndSaveMap(stringForValid, mapName);
        assertTrue(isMapCreated);
    }

    /**
     * This method tests the valid map is created.
     * @throws Exception if not
     */
    @Test
    public void checkIfInValidMapCreated() throws Exception {
        String mapName = "inValidMapNoTCreated";
        boolean isMapCreated = mapModel.createValidateAndSaveMap(stringForInValid, mapName);
        assertFalse(isMapCreated);
    }

    /**
     * This method tests the invalid map is created.
     * @throws Exception if not
     */
    @Test
    public void checkMapIsValidTest() throws Exception {
        mapModel.readMapFile("src/main/java/soen6441riskgame/mapFiles/inValidContinentNoCountry.map");
        assertFalse(mapModel.checkMapIsValid());
    }

    /**
     * This method tests the invalid map can be loaded or not.
     * @throws Exception if not
     */
    @Test
    public void checkContinentSubGraph() throws Exception {
        mapModel.readMapFile("src/main/java/soen6441riskgame/mapFiles/inValidDisconnectedContinentTestCase.map");
        assertFalse(mapModel.checkMapIsValid());
    }

}
