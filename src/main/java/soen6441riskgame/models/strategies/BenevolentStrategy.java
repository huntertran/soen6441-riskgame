package soen6441riskgame.models.strategies;

import java.util.ArrayList;

import soen6441riskgame.enums.StrategyName;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;

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

        if (conquered == null || conquered.size() == 0) {
            return null;
        }

        int weakestNumberOfArmy = conquered.get(0).getArmyAmount();

        for (Country country : conquered) {
            if (country.getArmyAmount() <= weakestNumberOfArmy) {
                weakestPlayerCountry = country;
                weakestNumberOfArmy = country.getArmyAmount();
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
        reinforce(countryToReinforce, player.getUnplacedArmies());
    }

    /**
     * never attack
     *
     * @param player           current player
     * @param attackingCountry attack from
     * @return null
     */
    @Override
    public ArrayList<Country> attack(Player player, Country attackingCountry) {
        attackEnd();
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
    public void playTurn(Player player) {
        Country weakestPlayerCountry = getWeakestCountryToReinforce(player);

        if(weakestPlayerCountry != null) {
            reinforce(player, weakestPlayerCountry);
        }
       
        exchangeCards(player);

        attack(player, null);

        Country weakerPlayerCountry = getWeakestCountryToReinforce(player);

        if (weakerPlayerCountry != null
            && weakerPlayerCountry != weakestPlayerCountry
            && weakestPlayerCountry != null) {
            fortify(weakestPlayerCountry, weakerPlayerCountry);
        }

        fortifyNone();
    }
}
