package soen6441riskgame.controllers;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameMap;

public class GamePlayTest {
    MapController mapController;
    GameController gameController;

    @Before
    public void before() throws IOException {

        GameMap testingInstanceGameMap = new GameMap();
        GameMap.setTestingInstance(testingInstanceGameMap);

        String filePath = "./src/test/java/soen6441riskgame/maps/RiskEurope.map";
        mapController = new MapController();
        mapController.resetMap();
        mapController.loadMap(filePath);

        gameController = new GameController();
    }

    @After
    public void after() {
        mapController.resetMap();
    }

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

                String[] fortifyArgs = new String[] { fromCountry.getName(), toCountry.getName(), "1" };

                gameController.handleFortifyCommand(fortifyArgs);
            }
        }

        gameController.handleFortifyCommand(new String[] { "none" });
    }

    @Test
    public void playTest() {
        // validate map
        mapController.validateMap();

        // add players
        gameController.handlePlayerAddAndRemoveCommand(new String[] { "-add", "TJ" });
        gameController.handlePlayerAddAndRemoveCommand(new String[] { "-add", "hunter" });
        gameController.handlePlayerAddAndRemoveCommand(new String[] { "-add", "ben" });

        // populate countries
        gameController.populateCountries();
        gameController.initPlayersUnplacedArmies();

        // get current player
        gameController.showCurrentPlayer();
        Player player = gameController.getCurrentPlayer();

        // place army
        System.out.println("Country to place: " + player.getConqueredCountries().get(0).getName());
        gameController.handlePlaceArmyCommand(player.getConqueredCountries().get(0).getName());
        gameController.handlePlaceArmyCommand(player.getConqueredCountries().get(0).getName());
        gameController.handlePlaceArmyCommand(player.getConqueredCountries().get(0).getName());

        // place all
        gameController.handlePlaceAllCommand();

        // reinforce
        gameController.enterReinforcement();

        for (int index = 0; index < 3; index++) {
            player = gameController.getCurrentPlayer();

            while (player.getUnplacedArmies() > 0) {
                String[] reinforceArgs = new String[] { player.getConqueredCountries().get(0).getName(), "1" };
                gameController.handleReinforceCommand(reinforceArgs);
            }
        }

        // fortify
        for (int index = 0; index < 3; index++) {
            fortify();
        }
    }
}