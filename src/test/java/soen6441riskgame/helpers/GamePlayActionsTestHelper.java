package soen6441riskgame.helpers;

import java.util.ArrayList;

import soen6441riskgame.App;
import soen6441riskgame.controllers.GameController;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.utils.ConsolePrinter;

/**
 * support setup environment for other tests
 */
public class GamePlayActionsTestHelper {
    /**
     * fortify multiple time
     * 
     * @param gameController game controller
     * @param fortifyTimes   time to fortify
     */
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

    /**
     * get player's country for command that required `fromcountry` argument
     * 
     * @param player the player to get country
     * @return country
     */
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

    /**
     * get player's country for command that required `tocountry` argument
     * 
     * @param player      the player to get country
     * @param fromCountry from country (which have neighbor relationship with result)
     * @return country
     */
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

    /**
     * fortify command
     * 
     * @param armies      number of armies
     * @param fromCountry from country
     * @param toCountry   to country
     */
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

    /**
     * add player hunter, ben and tj to game
     */
    public static void addPlayersToGame() {
        App.jumpToCommand(new ModelCommands(GameCommands.GAMEPLAYER + " -add hunter -add ben -add tj"));
    }
}
