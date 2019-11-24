package soen6441riskgame.models.strategies;

import java.util.ArrayList;
import soen6441riskgame.App;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;

/**
 * focuses on protecting its weak countries
 *
 * 1. reinforces its weakest countries
 *
 * 2. never attacks
 *
 * 3. fortifies in order to move armies to weaker countries
 */
public class BenevolentStrategy implements Strategy {

    private Country getWeakestCountryToReinforce(Player player) {
        ArrayList<Country> conquered = player.getConqueredCountries();
        Country weakestPlayerCountry = null;
        int minPlayerArmy = 0;
        int tempArmy;

        // Find the weakest country from conquered country list
        for (Country country : conquered) {
            tempArmy = country.getArmyAmount();

            if (tempArmy <= minPlayerArmy) {
                weakestPlayerCountry = country;
                minPlayerArmy = tempArmy;
            }
        }

        return weakestPlayerCountry;
    }

    @Override
    public void reinforce(Player player, Country countryToReinforce) {
        // Reinforce Phase
        // get number of army to place.
        // reinforce countryname num
        String command = GameCommands.REINFORCE;
        command += GameCommands.SPACE;
        command += countryToReinforce.getName();
        command += GameCommands.SPACE;
        command += String.valueOf(player.getUnplacedArmies());
        App.jumpToCommand(new ModelCommands(command));
    }

    @Override
    public ArrayList<Country> attack(Player player, Country attackingCountry) {
        // Attack Phase
        // Find adjacent neighbours countries to attack
        String command = GameCommands.ATTACK;
        command += GameCommands.SPACE;
        command += GameCommands.DASH;
        command += GameCommands.NOATTACK;
        App.jumpToCommand(new ModelCommands(command));

        return null;
    }

    @Override
    public void fortify(Country fromCountry, Country toCountry) {
        // Fortify Phase
        // Command: fortify fromcountry tocountry num
        // get max number of armies to move
        // then move the countries

        // Find the weaker country from conquered country list

        // get the average number of armies
        int averageArmies = fromCountry.getArmyAmount() + toCountry.getArmyAmount() / 2;
        int armyToMove = fromCountry.getArmyAmount() - averageArmies;

        String command = GameCommands.REINFORCE;
        command += GameCommands.SPACE;
        command += fromCountry.getName();
        command += GameCommands.SPACE;
        command += toCountry.getName();
        command += GameCommands.SPACE;
        command += String.valueOf(armyToMove);

        App.jumpToCommand(new ModelCommands(command));
    }

    @Override
    public void execute(Player player) {
        Country weakestPlayerCountry = getWeakestCountryToReinforce(player);

        reinforce(player, weakestPlayerCountry);

        attack(player, null);

        ArrayList<Country> conqueredCountries = player.getConqueredCountries();

        Country weakerPlayerCountry = null;
        int weakerPlayerArmy = 0;

        for (Country country : conqueredCountries) {
            int tempArmy = country.getArmyAmount();

            if (tempArmy <= weakerPlayerArmy) {
                weakerPlayerCountry = country;
                weakerPlayerArmy = tempArmy;
            }
        }

        fortify(weakestPlayerCountry, weakerPlayerCountry);

    }
}
