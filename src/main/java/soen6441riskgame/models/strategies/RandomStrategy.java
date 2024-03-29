package soen6441riskgame.models.strategies;

import java.util.ArrayList;

import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.enums.StrategyName;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.GameHelper;

/**
 * 1. reinforces random a random country
 *
 * <p>
 * 2. attacks a random number of times a random country
 *
 * <p>
 * 3. and fortifies a random country
 *
 * <p>
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
     * get country to reinforce
     *
     * @param player current player
     * @return the country
     */
    private Country getCountryToReinforce(Player player) {
        ArrayList<Country> conquered = player.getConqueredCountries();

        int randIndex = GameHelper.nextRandomIntInRange(0, (conquered.size() - 1));
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
        if (player.getUnplacedArmies() > 0) {
            int randArmy = GameHelper.nextRandomIntInRange(1, player.getUnplacedArmies());
            reinforce(countryToReinforce, randArmy);
        }
    }

    /**
     * attacks a random number of times a random country
     *
     * @param player           current player
     * @param attackingCountry attack from
     * @return list of attacked countries
     */
    @Override
    public ArrayList<Country> attack(Player player, Country attackingCountry) {
        ArrayList<Country> neighbours = attackingCountry.getNeighbors();
        Country defendingCountry = null;
        ArrayList<Country> defendableCountries = new ArrayList<>();
        ArrayList<Country> attackedCountries = new ArrayList<>();

        for (Country country : neighbours) {
            if (country.getConquerer() != player) {
                defendableCountries.add(country);
            }
        }

        if (defendableCountries.size() > 0) {
            defendingCountry = defendableCountries.get(GameHelper.nextRandomIntInRange(0,
                                                                                       defendableCountries.size() - 1));
        }

        if (defendingCountry != null) {
            attack(attackingCountry, defendingCountry, 0);

            // after attack with allout
            if (defendingCountry.getConquerer() == player) {
                // player conquered the defending country
                int armyToMove = GameBoard.getInstance().getGameBoardPlaying().getAttackerNumDice();
                attackMove(armyToMove);
                attackedCountries.add(defendingCountry);
            }
        }

        return attackedCountries;
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
        if (fromCountry.getArmyAmount() - 1 > 0) {
            int randArmyAmountToMove = GameHelper.nextRandomIntInRange(1, fromCountry.getArmyAmount() - 1);
            fortify(fromCountry, toCountry, randArmyAmountToMove);
        }

    }

    /**
     * 1. reinforces random a random country
     *
     * <p>
     * 2. attacks a random number of times a random country
     *
     * <p>
     * 3. and fortifies a random country
     *
     * @param player current player
     */
    @Override
    public void playTurn(Player player) {
        exchangeCards(player);

        Country randomCountry = getCountryToReinforce(player);

        // run reinforce once to generate unplaced armies
        reinforce(randomCountry, 0);

        while (player.getUnplacedArmies() > 0) {
            randomCountry = getCountryToReinforce(player);
            reinforce(player, randomCountry);
        }

        if (player.getUnplacedArmies() == 0
            && (player.getCurrentPhase() == GamePhase.REINFORCEMENT
                || player.getCurrentPhase() == GamePhase.WAITING_TO_TURN)) {
            // call reinforce when unplaced armies = 0 will change phase from REINFORCE to ATTACK
            reinforce(randomCountry, 0);
        }

        ArrayList<Country> attackingCountries = filterAttackableCountries(player.getConqueredCountries());
        if (attackingCountries.size() > 0) {
            int attackTime = GameHelper.nextRandomInt(attackingCountries.size());
            Country attackingCountry = attackingCountries.get(0);
            for (int index = 0; index < attackTime && player.getCurrentPhase() == GamePhase.ATTACK; index++) {
                attack(player, attackingCountry);
            }
        }

        if (player.getCurrentPhase() == GamePhase.ATTACK) {
            attackEnd();
        }

        if (player.isGameEnded()) {
            player.setEndOfGamePhase();
        } else {

            ArrayList<Country> conquered = player.getConqueredCountries();
            ArrayList<Country> moveArmyFrom = filterAttackableCountries(conquered);
            ArrayList<Country> fortifiableCountries = new ArrayList<>();

            if (moveArmyFrom.size() > 1 && conquered.size() > 1) {
                int randIndexCountryFrom = GameHelper.nextRandomIntInRange(0, (moveArmyFrom.size() - 1));
                Country countryFrom = moveArmyFrom.get(randIndexCountryFrom);

                for (Country country : countryFrom.getNeighbors()) {
                    if (country.getConquerer() == player) {
                        fortifiableCountries.add(country);
                    }
                }

                if (fortifiableCountries.size() > 0) {
                    int randIndexCountryTo = GameHelper.nextRandomIntInRange(0, (fortifiableCountries.size() - 1));
                    Country countryTo = fortifiableCountries.get(randIndexCountryTo);

                    fortify(countryFrom, countryTo);
                }

            }

            fortifyNone();
        }
    }

    /**
     * A function to filter ArrayList of Countries which has army less than two
     *
     * @param countries all country list of a player
     *
     * @return return ArrayList of countries with army count more than one.
     *
     */
    private ArrayList<Country> filterAttackableCountries(ArrayList<Country> countries) {
        ArrayList<Country> filteredList = new ArrayList<>();

        for (Country country : countries) {
            if (country.getArmyAmount() > 1) {
                filteredList.add(country);
            }
        }

        return filteredList;
    }
}
