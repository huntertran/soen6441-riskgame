package soen6441riskgame.maps;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;

// TODO: Auto-generated Javadoc
/**
 * Test class for Continent class that checks the values for the continent.
 *
 *
 * @version 1.0.0
 */

public class ContinentTest {
	
	/** Declaring objects. */
	private ArrayList<Country> countriesOfTheContinent= new ArrayList<>();
	
	/** Continent class object. */
	Continent objContinent;
	
	/** Country class object. */
	Country objCountry;


	/**
	 * Initializing objects and values for the test cases.
	 */
	@Before
	public void setUp()  {
		objContinent = new Continent(1,"Asia",3);
		objCountry = new Country(1, "India");
		countriesOfTheContinent.add(objCountry);
	}

	/**
	 * test method to check if it returns correct continent id.
	 */

	@Test
	public void testGetContinentId() {
		assertEquals(1,objContinent.getContinentID());

	}

	/**
	 * test method to check if it returns correct continent Name.
	 */
	@Test
	public void testGetContinentName() {
		assertEquals("Asia",objContinent.getContinentName());

	}

	/**
	 * test method to check if it returns correct control value for the continent.
	 */
	@Test
	public void testGetControlValue() {
		assertEquals(3,objContinent.getControlValue());

	}

	/**
	 * test method to check whether the country is being added to the continent.
	 */
	@Test
	public void testGetCountryList() {

		objContinent.addCountriesToTheContinentList(objCountry);
		countriesOfTheContinent = objContinent.getCountryList();
		assertEquals(countriesOfTheContinent,objContinent.getCountryList());
	}

}