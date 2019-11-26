package soen6441riskgame.models.strategies;

import java.util.ArrayList;
import java.util.Random;

import soen6441riskgame.enums.StrategyName;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.GameHelper;

/**
 * 1. reinforces random a random country
 *
 * 2. attacks a random number of times a random country
 *
 * 3. and fortifies a random country
 *
 * all following the standard rules for each phase.
 */
public class RandomStrategy implements Strategy {
    /**
     * get strategy name
     * 
     * @return the name of the strategy as enum
     */
    @Override
    public StrategyName getName() {
        return StrategyName.RANDOM;
    }

    /**
     * get count of dice
     * 
     * @param attackingCountry attacking country
     * @return count of dice
     */
    public int getDiceCount(Country attackingCountry) {
        int dices = 1;

        for (int i = 3; i > 0; i--) {
            if ((attackingCountry.getArmyAmount() - 1) >= i) {
                dices = i;
                break;
            }
        }

        return dices;
    }

    /**
     * get country to reinforce
     * 
     * @param player current player
     * @return the country
     */
    private Country getCountryToReinforce(Player player) {
        ArrayList<Country> conquered = player.getConqueredCountries();

        int randIndex = GameHelper.randomNumberGenerator(0, (conquered.size() - 1));
        return conquered.get(randIndex);
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
     * attack
     * 
     * @param player           current player
     * @param attackingCountry attack from
     * @return list of attacked countries
     */
    @Override
    public ArrayList<Country> attack(Player player, Country attackingCountry) {

        ArrayList<Country> conquered = player.getConqueredCountries();

        // Attack Phase
        int randomAttackValue = GameHelper.randomNumberGenerator(1, 5);
        ArrayList<Country> attackingCountryList = GameHelper.filterAttackableCountries(conquered);

        for (int i = 0; i < randomAttackValue; i++) {
            int randIndex = GameHelper.randomNumberGenerator(0, (attackingCountryList.size() - 1));
            Country attackCountry = attackingCountryList.get(randIndex);
            ArrayList<Country> neighbours = attackCountry.getNeighbors();
            int randNeighborIndex = GameHelper.randomNumberGenerator(0, neighbours.size());

            Country defendingCountry = neighbours.get(randNeighborIndex);

            attack(attackCountry, defendingCountry, 0);

            // after attack with allout
            if (defendingCountry.getConquerer() == player) {
                // player conquered the defending country
                int armyToMove = GameBoard.getInstance().getGameBoardPlaying().getAttackerNumDice();
                attackMove(armyToMove);
            }

            if (attackCountry.getConquerer() != player) {
                // player lost the attacking country
                break;
            }
        }

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
        // Fortify Phase
        // Command: fortify fromcountry tocountry num
        /*
         * 1. get list of conquered countries 2. get filtered list of conquered countries to move army from
         * 3. generate index of country from filtered list of conquered countries 4. generate randArmyMove
         * int (min 1, max = number of army moving from country filtered)
         */

        int randArmyAmountToMove = GameHelper.randomNumberGenerator(1, fromCountry.getArmyAmount());
        fortify(fromCountry, toCountry, randArmyAmountToMove);

    }

    /**
     * execute the strategy
     * 
     * @param player current player
     */
    @Override
    public void playTurn(Player player) {

        Random random = new Random();
        int reinforceTime = random.nextInt(player.getConqueredCountries().size());

        for (int index = 0; index < reinforceTime; index++) {
            Country randomCountry = getCountryToReinforce(player);
            reinforce(player, randomCountry);
        }

        ArrayList<Country> conquered = player.getConqueredCountries();
        ArrayList<Country> moveArmyFrom = GameHelper.filterAttackableCountries(conquered);

        attack(player, null);

        int randIndexCountryFrom = GameHelper.randomNumberGenerator(0, (moveArmyFrom.size() - 1));
        Country countryFrom = moveArmyFrom.get(randIndexCountryFrom);

        int randIndexCountryTo = GameHelper.randomNumberGenerator(0, (conquered.size() - 1));
        Country countryTo = conquered.get(randIndexCountryTo);

        fortify(countryFrom, countryTo);
    }
}
