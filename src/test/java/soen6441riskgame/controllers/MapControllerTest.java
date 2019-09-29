package test.java.soen6441riskgame.controllers;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import main.java.soen6441riskgame.models.Continent;
import main.java.soen6441riskgame.singleton.GameMap;

public class MapControllerTest {

	@Test
	public void editContinentTest() {
		// Setup
		GameMap.getInstance().Continents = new ArrayList<Continent>();

		// Action
		GameMap.getInstance().Continents.add(new Continent("Asia"));

		// Assert
		Assert.assertTrue(GameMap.getInstance().Continents.get(0).name == "Asia");
	}

}
