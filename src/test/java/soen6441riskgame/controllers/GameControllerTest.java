package soen6441riskgame.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameBoard;

public class GameControllerTest {
    MapController mapController;
    GameController gameController;

    @BeforeEach
    public void before() throws IOException {

        GameBoard testingInstanceGameMap = new GameBoard();
        GameBoard.setTestingInstance(testingInstanceGameMap);

        String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        mapController = new MapController();
        mapController.resetMap();
        mapController.loadMap(filePath);

        gameController = new GameController();
    }

    @AfterEach
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

        Player tjPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName(tjAddArgs[1]);
        Player hunterPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName(hunterAddArgs[1]);
        Player benPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName(benAddArgs[1]);

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

        Player tjPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName(tjAddArgs[1]);
        Player hunterPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName(hunterAddArgs[1]);
        Player benPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName(benAddArgs[1]);

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

        ArrayList<Country> countries = GameBoard.getInstance().getGameBoardMap().getCountries();
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

        ArrayList<Player> players = GameBoard.getInstance().getGameBoardPlayer().getPlayers();
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
    
    @Test
    public void enterAttackPhaseTest() {
        //Setup
        String[] tjAddArgs = new String[] { "-add", "TJ" };
        String[] hunterAddArgs = new String[] { "-add", "hunter" };
        String[] benAddArgs = new String[] { "-add", "ben" };
        gameController.handlePlayerAddAndRemoveCommand(tjAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(hunterAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(benAddArgs);   
        gameController.populateCountries();
        gameController.enterReinforcement();
        gameController.handleReinforceCommand(new String[] {"Spain","2"});
        
        // Action
        gameController.enterAttackPhase();
        
        //Assertion
        Player p1 = gameController.getCurrentPlayer();
        assertTrue(p1.getCurrentPhase() == GamePhase.ATTACK);
    }
    
    @Test
    public void handleAttackCommandTest() {
        //Setup
        String[] tjAddArgs = new String[] { "-add", "TJ" };
        String[] hunterAddArgs = new String[] { "-add", "hunter" };
        String[] benAddArgs = new String[] { "-add", "ben" };
        gameController.handlePlayerAddAndRemoveCommand(tjAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(hunterAddArgs);
        gameController.handlePlayerAddAndRemoveCommand(benAddArgs);   
        gameController.populateCountries();
        gameController.enterReinforcement();
        gameController.handleReinforceCommand(new String[] {"Spain","2"});
        gameController.enterAttackPhase();
        
        //Action
        gameController.handleAttackCommand(new String[] {"Spain", "France", "1"});
        
        //Assert
    }
}
