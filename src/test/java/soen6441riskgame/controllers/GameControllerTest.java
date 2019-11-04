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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import soen6441riskgame.App;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.helpers.GamePlayActionsTestHelper;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;

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
    public void addPlayerTest() {
        // Setup
        String hunter = "hunter";
        String ben = "ben";
        String tj = "tj";

        // Action
        App.jumpToCommand(new ModelCommands(GameCommands.GAMEPLAYER + " -add " + hunter));
        App.jumpToCommand(new ModelCommands(GameCommands.GAMEPLAYER + " -add " + ben));
        App.jumpToCommand(new ModelCommands(GameCommands.GAMEPLAYER + " -add " + tj));

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
        App.jumpToCommand(new ModelCommands(GameCommands.GAMEPLAYER + " -add " + hunter + " -add " + ben + " -add "
                                            + tj));

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
        GamePlayActionsTestHelper.addPlayersToGame();

        // Action
        App.jumpToCommand(new ModelCommands(GameCommands.GAMEPLAYER + " -remove hunter"));

        Player tjPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName("tj");
        Player hunterPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName("hunter");
        Player benPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayerFromName("ben");

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
        GamePlayActionsTestHelper.addPlayersToGame();
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
        GamePlayActionsTestHelper.addPlayersToGame();
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
        GamePlayActionsTestHelper.addPlayersToGame();
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
        GamePlayActionsTestHelper.addPlayersToGame();
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
        GamePlayActionsTestHelper.addPlayersToGame();
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

        GamePlayActionsTestHelper.addPlayersToGame();
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

    @Test
    public void handlePlaceAllTest() {
        // Setup

        GamePlayActionsTestHelper.addPlayersToGame();
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));
        // get first player
        Player player = gameController.getCurrentPlayer();

        int totalArmy = player.getTotalArmies();

        // Action
        App.jumpToCommand(new ModelCommands(GameCommands.PLACEALL));

        // Assert
        assertEquals(0, player.getUnplacedArmies());
        assertEquals(totalArmy, player.getTotalArmies());
    }

    @Test
    public void getCurrentPlayerTest() {
        // setup
        GamePlayActionsTestHelper.addPlayersToGame();
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));

        // action: get first player, should be hunter
        Player player = gameController.getCurrentPlayer();

        // assert
        assertEquals("hunter", player.getName());
    }

    @Test
    public void enterReinforcementTest() {
        // setup
        GamePlayActionsTestHelper.addPlayersToGame();
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));
        App.jumpToCommand(new ModelCommands(GameCommands.PLACEALL));

        // action: current player should be hunter
        boolean expected = gameController.enterReinforcement();

        // assert
        assertTrue(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = { 100, 2, 3, 4, 5, 6, 7, 8, 9, 10 })
    public void handleReinforceCommandTest(int armyAmount) {
        // setup
        GamePlayActionsTestHelper.addPlayersToGame();
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));
        App.jumpToCommand(new ModelCommands(GameCommands.PLACEALL));

        // action
        // enter reinforcement
        gameController.enterReinforcement();
        Player player = gameController.getCurrentPlayer();
        Country targetCountry = player.getConqueredCountries().get(0);

        int expectedNumberOfArmies = targetCountry.getArmyAmount();
        if (armyAmount <= player.getUnplacedArmies()) {
            expectedNumberOfArmies += armyAmount;
        }

        App.jumpToCommand(new ModelCommands(GameCommands.REINFORCE
                                            + " "
                                            + targetCountry.getName()
                                            + " "
                                            + String.valueOf(armyAmount)));

        // assert
        assertEquals(expectedNumberOfArmies, targetCountry.getArmyAmount());
    }

    @Test
    public void startRoundRobinPlayersTest() {
        // setup
        GamePlayActionsTestHelper.addPlayersToGame();
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));
        App.jumpToCommand(new ModelCommands(GameCommands.PLACEALL));

        // action
        Player player = gameController.startRoundRobinPlayers();

        // assert
        assertEquals("hunter", player.getName());
    }

    @Test
    public void turnToNextPlayerTest() {
        // setup
        GamePlayActionsTestHelper.addPlayersToGame();
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));
        App.jumpToCommand(new ModelCommands(GameCommands.PLACEALL));

        // action
        gameController.turnToNextPlayer();

        // assert
        assertEquals("ben", gameController.getCurrentPlayer().getName());
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 100 })
    public void handleFortifyCommandTest(int armiesToMove) {
        // setup
        GamePlayActionsTestHelper.addPlayersToGame();
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));
        App.jumpToCommand(new ModelCommands(GameCommands.PLACEALL));

        // reinforce
        Player player = gameController.getCurrentPlayer();

        while (player.getUnplacedArmies() > 0) {
            App.jumpToCommand(new ModelCommands(GameCommands.REINFORCE
                                                + " " + player.getConqueredCountries().get(0).getName()
                                                + " 1"));
        }

        // action
        Country fromCountry = GamePlayActionsTestHelper.getPlayerCountryForFromCountryArg(player);
        Country toCountry = GamePlayActionsTestHelper.getPlayerCountryForToCountryArg(player, fromCountry);

        if (fromCountry == null || toCountry == null) {
            ConsolePrinter.printFormat("Cannot perform FORTIFY unit test as user's conquered country is not connected");
            return;
        }

        int fromCountryArmiesAfterFortify = fromCountry.getArmyAmount();
        int toCountryArmiesAfterFortify = toCountry.getArmyAmount();

        if (fromCountry.getArmyAmount() - 1 >= armiesToMove) {
            fromCountryArmiesAfterFortify -= armiesToMove;
            toCountryArmiesAfterFortify += armiesToMove;
        }

        GamePlayActionsTestHelper.fortify(gameController,
                                          armiesToMove,
                                          fromCountry,
                                          toCountry);

        assertEquals(fromCountryArmiesAfterFortify, fromCountry.getArmyAmount());
        assertEquals(toCountryArmiesAfterFortify, toCountry.getArmyAmount());
    }
}
