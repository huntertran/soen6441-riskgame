package soen6441riskgame.controllers;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import soen6441riskgame.App;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.models.commands.MapEditorCommands;
import soen6441riskgame.singleton.GameBoard;

public class GamePlayTest {
    // MapController mapController;
    GameController gameController;

    @BeforeEach
    public void before() throws IOException {

        GameBoard testingInstanceGameMap = new GameBoard();
        GameBoard.setTestingInstance(testingInstanceGameMap);

        String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        App.jumpToCommand(new ModelCommands(MapEditorCommands.LOADMAP + " " + filePath));

        gameController = new GameController();
    }

    // @AfterEach
    // public void after() {
    // mapController.resetMap();
    // }

    private void fortify() {
        for (int index = 0; index < 3; index++) {
            Player player = gameController.getCurrentPlayer();

            ArrayList<Country> countries = player.getConqueredCountries();

            Country fromCountry = null;
            for (Country country : countries) {
                if (country.getArmyAmount() > 1) {
                    fromCountry = country;
                    break;
                }
            }

            Country toCountry = null;
            if (fromCountry != null) {
                ArrayList<Country> toCountries = fromCountry.getNeighbors();
                for (Country country : toCountries) {
                    if (country.getConquerer().equals(player) && !country.equals(fromCountry)) {
                        toCountry = country;
                        break;
                    }
                }
            }

            if (fromCountry != null && toCountry != null) {
                System.out.println("From country: " + fromCountry.getName());
                System.out.println("To country: " + toCountry.getName());

                String[] fortifyArgs = new String[] { GameCommands.FORTIFY,
                                                      fromCountry.getName(),
                                                      toCountry.getName(),
                                                      "1" };
                App.jumpToCommand(new ModelCommands(String.join(" ", fortifyArgs)));
            }
        }

        App.jumpToCommand(new ModelCommands(GameCommands.FORTIFY + " none"));
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
        System.out.println("Country to place: " + player.getConqueredCountries().get(0).getName());

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
            fortify();
        }
    }
}