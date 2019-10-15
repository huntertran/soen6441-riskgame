package soen6441riskgame.controllers;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameMap;

public class GameControllerTest {
    MapController mapController;
    GameController gameController;

    @Before
    public void before() throws IOException {
        String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        mapController = new MapController();
        mapController.loadMap(filePath);

        gameController = new GameController();
    }

    @After
    public void after() {
        mapController.resetMap();
    }

    @Test
    public void handlePlayerAddCommandTest() {
        // Setup
        String[] args = new String[] { "-add", "TJ" };

        // Action
        gameController.handlePlayerAddAndRemoveCommand(args);

        // Assert
        Player tjPlayer = GameMap.getInstance().getPlayerFromName(args[1]);
        assertNotNull(tjPlayer);
    }
    
    @Test
    public void handlePlayerRemoveCommandTest(){
        // Setup
        String[] args= new String[]{"-add", "TJ"};
        String[] args1= new String[]{"-remove","TJ"};
        
        // Action
        gameController.handlePlayerAddAndRemoveCommand(args);
        gameController.handlePlayerAddAndRemoveCommand(args1);

        // Assert
        Player tjPlayer= GameMap.getInstance().getPlayerFromName(args[1]);
        assertNull(tjPlayer);
    }
}