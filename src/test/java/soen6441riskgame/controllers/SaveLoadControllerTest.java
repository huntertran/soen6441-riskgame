package soen6441riskgame.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import soen6441riskgame.App;
import soen6441riskgame.enums.CardType;
import soen6441riskgame.helpers.GamePlayActionsTestHelper;
import soen6441riskgame.models.Card;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.models.commands.MapEditorCommands;
import soen6441riskgame.singleton.GameBoard;

public class SaveLoadControllerTest {
    private SaveLoadController saveLoadController;
    private GameController gameController;

    /**
     * the before method is executed before each test case to setup the context.
     */
    @BeforeEach
    public void before() {
        GameBoard testingInstanceGameMap = new GameBoard();
        GameBoard.setTestingInstance(testingInstanceGameMap);

        // String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        String filePath = "smallmap";
        App.jumpToCommand(new ModelCommands(MapEditorCommands.LOADMAP + " " + filePath));

        saveLoadController = new SaveLoadController();
        gameController = new GameController();
    }

    private String setupForSave(){
        GamePlayActionsTestHelper.addPlayersToGame();
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));
        App.jumpToCommand(new ModelCommands(GameCommands.PLACEALL));

        Player currentPlayer = gameController.getCurrentPlayer();

        // add cards to player
        for (int index = 0; index < 6; index++) {
            Card newCard = GameBoard.getInstance()
                                    .getSpecificCardForTest(CardType.Infantry);
            newCard.setHoldingPlayer(currentPlayer);
            currentPlayer.getHoldingCards().add(newCard);
        }

        return "testSavedGame";
    }

    @Test
    public void saveGameTest() {
        String saveGameFilePath = setupForSave();
        boolean result = saveLoadController.saveGame(saveGameFilePath);

        assertTrue(result);
    }

    @Test
    public void loadGameTest(){
        String saveGameFilePath = setupForSave();
        boolean saveResult = saveLoadController.saveGame(saveGameFilePath);
        boolean readResult = saveLoadController.loadGame(saveGameFilePath);

        assertTrue(saveResult);
        assertTrue(readResult);
    }
}
