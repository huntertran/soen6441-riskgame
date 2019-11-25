package soen6441riskgame.models.strategies;

import java.util.ArrayList;
import soen6441riskgame.App;
import soen6441riskgame.enums.StrategyName;
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
    /**
     * get strategy name
     * 
     * @return the name of the strategy as enum
     */
    @Override
    public StrategyName getName() {
        return StrategyName.BENEVOLENT;
    }

    /**
     * get weakest country to reinforce
     * 
     * @param player current player
     * @return the weakest country
     */
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

    /**
     * reinforce
     * 
     * @param player             current player
     * @param countryToReinforce country to reinforce
     */
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

    /**
     * attack
     * 
     * @param player           current player
     * @param attackingCountry attack from
     * @return list of attacked countries
     */
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

    /**
     * fortify
     * 
     * @param fromCountry from country
     * @param toCountry   to country
     */
    @Override
    public void fortify(Country fromCountry, Country toCountry) {
        int averageArmies = fromCountry.getArmyAmount() + toCountry.getArmyAmount() / 2;
        int armyToMove = fromCountry.getArmyAmount() - averageArmies;
        fortify(fromCountry, toCountry, armyToMove);
    }

    /**
     * execute the strategy
     * 
     * @param player current player
     */
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

        if (weakerPlayerCountry != null) {
            fortify(weakestPlayerCountry, weakerPlayerCountry);
        } else {
            fortifyNone();
        }
    }
}
