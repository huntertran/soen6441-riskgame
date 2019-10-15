package soen6441riskgame.controllers;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import soen6441riskgame.models.Country;
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
        String[] tjAddArgs = new String[] { "-add", "TJ" };
        String[] hunterAddArgs = new String[] { "-add", "hunter" };
        String[] benAddArgs = new String[] { "-add", "ben" };

        // Action
        gameController.handlePlayerAddAndRemoveCommand(tjAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(hunterAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(benAddArgs);

        Player tjPlayer = GameMap.getInstance().getPlayerFromName(tjAddArgs[1]);
        Player hunterPlayer = GameMap.getInstance().getPlayerFromName(hunterAddArgs[1]);
        Player benPlayer = GameMap.getInstance().getPlayerFromName(benAddArgs[1]);

        Player tjNextPlayer = tjPlayer.getNextPlayer();
        Player tjPreviousPlayer = tjPlayer.getPreviousPlayer();
        Player hunterNextPlayer = hunterPlayer.getNextPlayer();

        // Assert
        assertNotNull(tjPlayer);
        assertNotNull(hunterPlayer);
        assertNotNull(benPlayer);

        assertSame(hunterPlayer, tjNextPlayer);
        assertSame(benPlayer, hunterNextPlayer);
        assertSame(benPlayer, tjPreviousPlayer);
    }

    @Test
    public void handlePlayerRemoveCommandTest() {
        // Setup
        String[] tjAddArgs = new String[] { "-add", "TJ" };
        String[] hunterAddArgs = new String[] { "-add", "hunter" };
        String[] benAddArgs = new String[] { "-add", "ben" };
        String[] hunterRemoveArgs = new String[] { "-remove", "hunter" };

        // Action
        gameController.handlePlayerAddAndRemoveCommand(tjAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(hunterAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(benAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(hunterRemoveArgs);
        Player tjPlayer = GameMap.getInstance().getPlayerFromName(tjAddArgs[1]);
        Player hunterPlayer = GameMap.getInstance().getPlayerFromName(hunterAddArgs[1]);
        Player benPlayer = GameMap.getInstance().getPlayerFromName(benAddArgs[1]);
        Player tjNextPlayer = tjPlayer.getNextPlayer();
        Player tjPreviousPlayer = tjPlayer.getPreviousPlayer();
        Player benPreviousPlayer = benPlayer.getPreviousPlayer();
        Player benNextPlayer = benPlayer.getNextPlayer();

        // Assert
        assertNull(hunterPlayer);
        assertSame(benPlayer, tjNextPlayer);
        assertSame(benPlayer, tjPreviousPlayer);
        assertSame(tjPlayer, benPreviousPlayer);
        assertSame(tjPlayer, benNextPlayer);
    }

    @Test
    public void populateCountriesTest() {
        // Setup
        String[] tjAddArgs = new String[] { "-add", "TJ" };
        String[] hunterAddArgs = new String[] { "-add", "hunter" };
        String[] benAddArgs = new String[] { "-add", "ben" };
        String[] rogerAddArgs = new String[] { "-add", "roger" };

        // Action
        gameController.handlePlayerAddAndRemoveCommand(tjAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(hunterAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(benAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(rogerAddArgs);

        gameController.populateCountries();

        boolean isOneCountryNotAssigned = false;

        ArrayList<Country> countries = GameMap.getInstance().getCountries();
        for (Country country : countries) {
            Player conquerer = country.getConquerer();
            if (conquerer == null) {
                isOneCountryNotAssigned = true;
                break;
            }
        }

        // Assert
        assertFalse(isOneCountryNotAssigned);
    }

    @Test
    public void initPlayersUnplacedArmiesTest() {
        // Setup
        String[] tjAddArgs = new String[] { "-add", "TJ" };
        String[] hunterAddArgs = new String[] { "-add", "hunter" };
        String[] benAddArgs = new String[] { "-add", "ben" };
        String[] rogerAddArgs = new String[] { "-add", "roger" };

        // Action
        gameController.handlePlayerAddAndRemoveCommand(tjAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(hunterAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(benAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(rogerAddArgs);

        gameController.initPlayersUnplacedArmies();

        int unplacedArmiesEachPlayer = -1;
        boolean isUnplacedArmiesDifferentForOnePlayer = false;

        ArrayList<Player> players = GameMap.getInstance().getPlayers();
        for (Player player : players) {
            if (unplacedArmiesEachPlayer == -1) {
                unplacedArmiesEachPlayer = player.getUnplacedArmies();
            } else {
                if (unplacedArmiesEachPlayer != player.getUnplacedArmies()) {
                    isUnplacedArmiesDifferentForOnePlayer = true;
                }
            }

        }

        // Assert
        assertFalse(isUnplacedArmiesDifferentForOnePlayer);
    }
}
