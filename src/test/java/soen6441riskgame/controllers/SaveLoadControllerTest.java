package soen6441riskgame.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import soen6441riskgame.App;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.models.commands.MapEditorCommands;
import soen6441riskgame.singleton.GameBoard;

public class SaveLoadControllerTest {
    private SaveLoadController saveLoadController;

    /**
     * the before method is executed before each test case to setup the context.
     *
     * @throws IOException
     */
    @BeforeEach
    public void before() throws IOException {
        GameBoard testingInstanceGameMap = new GameBoard();
        GameBoard.setTestingInstance(testingInstanceGameMap);

        // String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        String filePath = "smallmap";
        App.jumpToCommand(new ModelCommands(MapEditorCommands.LOADMAP + " " + filePath));

        // add players
        App.jumpToCommand(new ModelCommands("gameplayer -add hunter -add ben -add tj"));

        // populate countries
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));

        saveLoadController = new SaveLoadController();
    }

    @Test
    public void saveGameTest() {
        String saveGameFilePath = "testSavedGame.json";
        boolean result = saveLoadController.saveGame(saveGameFilePath);

        assertTrue(result);
    }
}