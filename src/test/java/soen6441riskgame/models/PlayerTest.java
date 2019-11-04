package soen6441riskgame.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import soen6441riskgame.App;
import soen6441riskgame.controllers.GameController;
import soen6441riskgame.controllers.MapController;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.helpers.GamePlayActionsTestHelper;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.singleton.GameBoard;

public class PlayerTest {
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
    public void calculateReinforcementArmiesTest(){
        // setup
        GamePlayActionsTestHelper.addPlayersToGame();
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));
        App.jumpToCommand(new ModelCommands(GameCommands.PLACEALL));
        Player player = gameController.getCurrentPlayer();

        // action
        // before enter reinforcement
        player.calculateReinforcementArmies();
        int zeroArmiesBeforeReinforce = player.getUnplacedArmies();

        // set reinforcement phase
        player.setCurrentPhase(GamePhase.REINFORCEMENT);
        player.calculateReinforcementArmies();
        int numberOfInitArmiesInReinforce = player.getUnplacedArmies();

        assertEquals(0, zeroArmiesBeforeReinforce);
        assertTrue(numberOfInitArmiesInReinforce >= 3);
    }
}