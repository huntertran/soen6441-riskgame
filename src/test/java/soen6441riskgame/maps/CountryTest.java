package soen6441riskgame.maps;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import soen6441riskgame.enums.Colors;
import soen6441riskgame.models.Country;

// TODO: Auto-generated Javadoc
/**
 * This test case tested the country class functions.
 * 
 * 
 * @version 1.0.0
 *
 */
public class CountryTest {
	
	/** The country. */
	Country country;  
	
	/** The neighbours string. */
	private ArrayList<String> neighboursString = new ArrayList<>();

	/**
	 * this function is going to run before every function executes. It is initiating some predefined data
	 * @throws Exception if it is not setting the values at the starting 
	 */
	@Before
	public void setUp() throws Exception {
		country = new Country(1, "Bangladesh");
		country.setPlayerId(1);
		neighboursString.add("Canada");
		neighboursString.add("Alberta");
		country.addNeighborString("Canada");
		country.addNeighborString("Alberta");
		country.setContinentID(1);
		country.setxCoordinate(120);
		country.setyCoordinate(100);
		country.setCountryColor(Colors.BLUE);
	}

	/**
	 * Test the country name is same or not.
	 */
	@Test
	public void testCountryName() {
		assertEquals("Bangladesh", country.getCountryName());
	}

	/**
	 * test the country id is same or not.
	 */
	@Test
	public void testCountryId() {
		assertEquals(1, country.getCountryId());
	}

	/**
	 * tests the neighbour countries.
	 */
	@Test
	public void testCountryNeighbours() {
		assertEquals(neighboursString, country.getNeighboursString());
	}
	
	/**
	 * test the continent name is same or not.
	 */
	@Test
	public void testCountryContinent() {
		assertEquals(1, country.getContinentID());
	}

	/**
	 * it is testing the coordinate is same or not.
	 */
	@Test
	public void testCountryCo0rdinate() {
		assertEquals(120, country.getxCoordinate());
		assertEquals(100, country.getyCoordinate());
	}
}
