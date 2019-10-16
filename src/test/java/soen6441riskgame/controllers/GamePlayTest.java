package soen6441riskgame.controllers;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameMap;

public class GamePlayTest {
    MapController mapController;
    GameController gameController;

    @Before
    public void before() throws IOException {

        GameMap testingInstanceGameMap = new GameMap();
        GameMap.setTestingInstance(testingInstanceGameMap);

        String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        mapController = new MapController();
        mapController.resetMap();
        mapController.loadMap(filePath);

        gameController = new GameController();
    }

    @After
    public void after() {
        mapController.resetMap();
    }

    @Test
    public void playTest() {
        // validate map
        mapController.validateMap();

        // add players
        gameController.handlePlayerAddAndRemoveCommand(new String[] { "-add", "TJ" });
        gameController.handlePlayerAddAndRemoveCommand(new String[] { "-add", "hunter" });
        gameController.handlePlayerAddAndRemoveCommand(new String[] { "-add", "ben" });

        // populate countries
        gameController.populateCountries();

        // get current player
        gameController.showCurrentPlayer();
        Player player = gameController.getCurrentPlayer();

        // place army
        System.out.println("Country to place: " + player.getConqueredCountries().get(0).getName());
        gameController.handlePlaceArmyCommand(player.getConqueredCountries().get(0).getName());
        gameController.handlePlaceArmyCommand(player.getConqueredCountries().get(0).getName());
        gameController.handlePlaceArmyCommand(player.getConqueredCountries().get(0).getName());

        // place all
        gameController.handlePlaceAllCommand();

        // reinforce
        player = gameController.getCurrentPlayer();

        String[] reinforceArgs = new String[] { player.getConqueredCountries().get(0).getName(), "1" };

        gameController.handleReinforceCommand(reinforceArgs);



    }
}