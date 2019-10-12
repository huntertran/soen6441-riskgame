package soen6441riskgame.maps;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import soen6441riskgame.enums.Colors;
import soen6441riskgame.models.CountryViewModel;
// TODO: Auto-generated Javadoc
/**
 * Test class that tests the methods in CountryViewModel class in models package.
 * 
 * @version 1.0.0
 */
public class CountryViewModelTest {
	
	/** Declaring objects. */
	CountryViewModel objCVM = new CountryViewModel();
	
	/** The neighbours. */
	private ArrayList<String> neighbours = new ArrayList<>();

	/**
	 * Initializing objects and values for the test cases.
	 * @throws Exception if it is not setting the values at the starting
	 */
	@Before
	public void setUp() throws Exception{
		objCVM.setCountryId(1);
		objCVM.setCountryName("Canada");
		objCVM.setxCoordinate(120);
		objCVM.setyCoordinate(100);
		objCVM.setNumberOfArmies(4);
		objCVM.setPlayerID(1);
		objCVM.setColorOfCountry(Colors.GREEN);
		neighbours.add("USA");
		neighbours.add("Canada");
		objCVM.setNeighbours(neighbours);

	}


	/**
	 * Test get country id.
	 */
	@Test
	public void testGetCountryId() {
		assertEquals(1,objCVM.getCountryId());
	}


	/**
	 * Test get country name.
	 */
	@Test
	public void testGetCountryName() {
		assertEquals("Canada",objCVM.getCountryName());
	}


	/**
	 * Test getx coordinate.
	 */
	@Test
	public void testGetxCoordinate() {
		assertEquals(120,objCVM.getxCoordinate());
	}



	/**
	 * Test gety coordinate.
	 */
	@Test
	public void testGetyCoordinate() {
		assertEquals(100,objCVM.getyCoordinate());
	}



	/**
	 * Test getnumber of armies.
	 */
	@Test
	public void testGetnumberOfArmies() {
		assertEquals(4,objCVM.getNumberOfArmies());
	}

	/**
	 * Test get color of country.
	 */
	@Test
	public void testGetColorOfCountry() {
		assertEquals(Colors.GREEN,objCVM.getColorOfCountry());
	}


	/**
	 * Test get player id.
	 */
	@Test
	public void testGetPlayerId() {
		assertEquals(1,objCVM.getPlayerID());
	}


	/**
	 * Test get neighbours.
	 */
	@Test
	public void testGetNeighbours() {
		for(String s:neighbours) 
			System.out.println(s);

	}
}