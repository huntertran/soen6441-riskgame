package test.java.soen6441riskgame.controllers;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import main.java.soen6441riskgame.controllers.MapController;
import main.java.soen6441riskgame.models.Continent;
import main.java.soen6441riskgame.singleton.GameMap;

public class MapControllerTest {

	@Test
	/*public void editContinentTest() {
		// Setup
		GameMap.getInstance().Continents = new ArrayList<Continent>();

		// Action
		GameMap.getInstance().Continents.add(new Continent("Asia"));

		// Assert
		Assert.assertTrue(GameMap.getInstance().Continents.get(0).name == "Asia");
	}
	*/
	
	/*public boolean continentExists(String continentName) {
    	
    	for(Continent continent : Continents) {
    		if(continent.name == continentName)
    			return true;
    	}
    	return false;
    }
    */
	
	
	public void editContinentTest() {
		//Setup
		MapController map = new MapController();
		String arguments[] = {"-add" , "Asia" , "10"};
		
		//Action
		map.editContinent(arguments);
		
		//Assert
		//ArrayList<Continent> continents = GameMap.getInstance().continentExists("Asia");
		
		//Assert.assertTrue(GameMap.continentExists("Asia") == true);
		
		
		
	}
	
	

}
