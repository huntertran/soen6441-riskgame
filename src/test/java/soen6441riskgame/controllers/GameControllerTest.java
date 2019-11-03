package soen6441riskgame.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import soen6441riskgame.App;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;
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

    private ArrayList<Player> addPlayersToGame() {
        App.jumpToCommand(new ModelCommands("gameplayer -add hunter -add ben -add tj"));
        return GameBoard.getInstance().getGameBoardPlayer().getPlayers();
    }

    @Test
    public void addPlayerTest() {
        // Setup
        String hunter = "hunter";
        String ben = "ben";
        String tj = "tj";

        // Action
        App.jumpToCommand(new ModelCommands("gameplayer -add " + hunter));
        App.jumpToCommand(new ModelCommands("gameplayer -add " + ben));
        App.jumpToCommand(new ModelCommands("gameplayer -add " + tj));

        Player tjPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName(tj);
        Player hunterPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName(hunter);
        Player benPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName(ben);

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
    public void addMultiplePlayersTest() {
        // Setup
        String hunter = "hunter";
        String ben = "ben";
        String tj = "tj";

        // Action
        App.jumpToCommand(new ModelCommands("gameplayer -add " + hunter + " -add " + ben + " -add " + tj));

        Player tjPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName(tj);
        Player hunterPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName(hunter);
        Player benPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName(ben);

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
        addPlayersToGame();
        App.jumpToCommand(new ModelCommands(GameCommands.GAMEPLAYER + " -add roger"));

        // Action
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));

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
        addPlayersToGame();
        App.jumpToCommand(new ModelCommands(GameCommands.GAMEPLAYER + " -add roger"));

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
        // Setup
        addPlayersToGame();
        gameController.populateCountries();
        gameController.enterReinforcement();
        gameController.handleReinforceCommand(new String[] { "Spain", "2" });

        // Action
        gameController.enterAttackPhase();

        // Assertion
        Player p1 = gameController.getCurrentPlayer();
        assertTrue(p1.getCurrentPhase() == GamePhase.ATTACK);
    }

    @Test
    public void handleAttackCommandTest() {
        // Setup
        addPlayersToGame();
        gameController.populateCountries();
        gameController.enterReinforcement();
        gameController.handleReinforceCommand(new String[] { "Spain", "2" });
        gameController.enterAttackPhase();
        int a = GameBoard.getInstance().getGameBoardMap().getCountryFromName("Spain").getArmyAmount();
        int b = GameBoard.getInstance().getGameBoardMap().getCountryFromName("France").getArmyAmount();
        // Action
        gameController.handleAttackCommand(new String[] { "Spain", "France", "1" });
        if (gameController.isAttackValid()) {
            gameController.handleDefendCommand(new String[] { "1" });
        }

        // Assert
        assertTrue(GameBoard.getInstance().getGameBoardMap().getCountryFromName("Spain").getArmyAmount() != a
                   || GameBoard.getInstance().getGameBoardMap().getCountryFromName("France").getArmyAmount() != b
                   || !gameController.isAttackValid());
    }

    @Test
    public void endAttackPhaseTest() {
        // Setup
        addPlayersToGame();
        gameController.populateCountries();
        gameController.enterReinforcement();
        gameController.enterAttackPhase();

        // Action
        gameController.handleAttackCommand(new String[] { "-noattack" });

        // Assert
        assertTrue(gameController.getCurrentPlayer().getCurrentPhase() == GamePhase.FORTIFICATION);
    }

    @Test
    public void handlePlaceArmyTest() {
        // Setup

        addPlayersToGame();
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));
        // get first player
        Player player = gameController.getCurrentPlayer();
        // get hunter's countries
        Country targetCountry = player.getConqueredCountries().get(0);

        // Action
        int expectedPlacedArmyNumber = targetCountry.getArmyAmount() + 1;
        App.jumpToCommand(new ModelCommands(GameCommands.PLACEARMY + " " + targetCountry.getName()));

        // Assert
        assertEquals(expectedPlacedArmyNumber, targetCountry.getArmyAmount());
    }
}
