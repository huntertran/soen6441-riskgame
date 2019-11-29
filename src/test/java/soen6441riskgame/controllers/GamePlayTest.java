package soen6441riskgame.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soen6441riskgame.App;
import soen6441riskgame.helpers.GamePlayActionsTestHelper;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.models.commands.MapEditorCommands;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;

/**
 * Tests for Game Play.
 */
public class GamePlayTest {
    // MapController mapController;
    GameController gameController;

    /**
     * the before method is executed before each test case to setup the context.
     */
    @BeforeEach
    public void before() {

        GameBoard testingInstanceGameMap = new GameBoard();
        GameBoard.setTestingInstance(testingInstanceGameMap);

        String filePath = "./src/test/java/files/maps/domination/RiskEurope.map";
        App.jumpToCommand(new ModelCommands(MapEditorCommands.LOADMAP + " " + filePath));

        gameController = new GameController();
    }

    @Test
    public void playTest() {
        // validate map
        App.jumpToCommand(new ModelCommands(MapEditorCommands.VALIDATEMAP));

        // add players
        App.jumpToCommand(new ModelCommands("gameplayer -add hunter -add ben -add tj"));

        // populate countries
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));

        // get current player
        // gameController.showCurrentPlayer();
        Player player = gameController.getCurrentPlayer();

        // place army
        ConsolePrinter.printFormat("Country to place: " + player.getConqueredCountries().get(0).getName());

        Country targetCountry = player.getConqueredCountries().get(0);

        App.jumpToCommand(new ModelCommands(GameCommands.PLACEARMY + " " + targetCountry.getName()));
        App.jumpToCommand(new ModelCommands(GameCommands.PLACEARMY + " " + targetCountry.getName()));
        App.jumpToCommand(new ModelCommands(GameCommands.PLACEARMY + " " + targetCountry.getName()));

        // place all
        App.jumpToCommand(new ModelCommands(GameCommands.PLACEALL));

        // reinforce
        for (int index = 0; index < 3; index++) {
            player = gameController.getCurrentPlayer();

            while (player.getUnplacedArmies() > 0) {
                App.jumpToCommand(new ModelCommands(GameCommands.REINFORCE
                                                    + " " + player.getConqueredCountries().get(0).getName()
                                                    + " 1"));
            }
        }

        // fortify
        for (int index = 0; index < 3; index++) {
            GamePlayActionsTestHelper.multipleFortify(gameController, 3);
        }
    }
}
