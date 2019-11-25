package soen6441riskgame.models.strategies;

import java.util.ArrayList;

import soen6441riskgame.App;
import soen6441riskgame.enums.StrategyName;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;

/**
 * focuses on attack
 *
 * 1. reinforces its strongest country
 *
 * 2. always attack with it until it cannot attack anymore
 *
 * 3. fortifies in order to maximize aggregation of forces in one country).
 */
public class AggressiveStrategy implements Strategy {
    /**
     * get strategy name
     * 
     * @return the name of the strategy as enum
     */
    @Override
    public StrategyName getName() {
        return StrategyName.AGGRESSIVE;
    }

    /**
     * get the strongest country to reinforce
     *
     * @param player the player
     * @return the strongest country
     */
    private Country getStrongestCountryToReinforce(Player player) {
        Country strongestPlayerCountry = null;
        int maxPlayerArmy = 0;

        ArrayList<Country> conqueredCountries = player.getConqueredCountries();

        for (Country country : conqueredCountries) {
            int tempArmy = country.getArmyAmount();

            if (tempArmy >= maxPlayerArmy) {
                strongestPlayerCountry = country;
                maxPlayerArmy = tempArmy;
            }
        }

        return strongestPlayerCountry;
    }

    /**
     * Reinforce Phase
     *
     * get number of army to place.
     *
     * <code>reinforce countryname num</code>
     *
     * @param player             the player
     * @param countryToReinforce the country to reinforce
     */
    @Override
    public void reinforce(Player player, Country countryToReinforce) {
        String command = GameCommands.REINFORCE;
        command += GameCommands.SPACE;
        command += countryToReinforce.getName();
        command += GameCommands.SPACE;
        command += String.valueOf(player.getUnplacedArmies());

        App.jumpToCommand(new ModelCommands(command));
    }

    /**
     * Attack Phase
     *
     * @param player           the player
     * @param attackingCountry the attacking country
     * @return the attacked countries
     */
    @Override
    public ArrayList<Country> attack(Player player, Country attackingCountry) {
        ArrayList<Country> neighbours = attackingCountry.getNeighbors();
        ArrayList<Country> attackedCountries = new ArrayList<>();

        boolean flag = false;
        for (int i = 0; i < neighbours.size() || flag; i++) {
            int totalNumPlayerArmy = attackingCountry.getArmyAmount();
            Country country = neighbours.get(i);

            // Attacker lost all army in strongest country
            if (totalNumPlayerArmy <= 1) {
                flag = true;
                break;
            }
            // Attacker lost the strongest country
            else if (attackingCountry.getConquerer() == player) {
                flag = true;
                break;
            }
            // Attacker won the country
            else if (country.getConquerer() == player) {
                flag = true;
                break;
            }
            // Continue to attack neighbours
            else {
                String command = GameCommands.ATTACK;
                command += GameCommands.SPACE;
                command += attackingCountry.getName();
                command += GameCommands.SPACE;
                command += country.getName();
                command += GameCommands.SPACE;
                command += GameCommands.DASH;
                command += GameCommands.ALLOUT;
                App.jumpToCommand(new ModelCommands(command));

                attackedCountries.add(country);
            }
        }

        return attackedCountries;
    }

    /**
     * Fortify Phase
     *
     * Command:
     *
     * <code>fortify fromcountry tocountry num</code>
     *
     * get max number of armies to move then
     *
     * move the countries
     *
     * @param fromCountry the attacking country
     * @param toCountry   the country that attacked
     */
    @Override
    public void fortify(Country fromCountry, Country toCountry) {
        int maxArmyToMove = fromCountry.getArmyAmount() - 1;
        fortify(fromCountry, toCountry, maxArmyToMove);
    }

    /**
     * execute the strategy
     * 
     * @param player current player
     */
    @Override
    public void execute(Player player) {
        Country strongestPlayerCountry = getStrongestCountryToReinforce(player);

        reinforce(player, strongestPlayerCountry);

        ArrayList<Country> attackedCountries = attack(player, strongestPlayerCountry);

        int index = attackedCountries.size() - 1;
        Country fortifyToCountry = attackedCountries.get(index);

        fortify(strongestPlayerCountry, fortifyToCountry);
    }
}
