package soen6441riskgame.helpers;

import java.util.ArrayList;

import soen6441riskgame.App;
import soen6441riskgame.controllers.GameController;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.utils.ConsolePrinter;

public class GamePlayActionsTestHelper {
    public static void multipleFortify(GameController gameController, int fortifyTimes) {
        for (int index = 0; index < fortifyTimes; index++) {
            Player player = gameController.getCurrentPlayer();

            Country fromCountry = getPlayerCountryForFromCountryArg(player);

            Country toCountry = getPlayerCountryForToCountryArg(player, fromCountry);

            fortify(
                1,
                    fromCountry,
                    toCountry);
        }

        App.jumpToCommand(new ModelCommands(GameCommands.FORTIFY + " -none"));
    }

    public static Country getPlayerCountryForFromCountryArg(Player player) {
        ArrayList<Country> countries = player.getConqueredCountries();

        Country fromCountry = null;
        for (Country country : countries) {
            if (country.getArmyAmount() > 1) {
                fromCountry = country;
                break;
            }
        }

        return fromCountry;
    }

    public static Country getPlayerCountryForToCountryArg(Player player, Country fromCountry) {
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

        return toCountry;
    }

    public static void fortify(int armies,
                               Country fromCountry,
                               Country toCountry) {

        if (fromCountry != null && toCountry != null) {
            ConsolePrinter.printFormat("From country: " + fromCountry.getName());
            ConsolePrinter.printFormat("To country: " + toCountry.getName());

            String[] fortifyArgs = new String[] { GameCommands.FORTIFY,
                                                  fromCountry.getName(),
                                                  toCountry.getName(),
                                                  String.valueOf(armies) };
            App.jumpToCommand(new ModelCommands(String.join(" ", fortifyArgs)));
        }
    }

    public static void addPlayersToGame() {
        App.jumpToCommand(new ModelCommands(GameCommands.GAMEPLAYER + " -add hunter -add ben -add tj"));
    }
}
