package soen6441riskgame.models.strategies;

import soen6441riskgame.enums.StrategyName;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameBoard;

import java.util.ArrayList;

/**
 * focuses on attack
 *
 * <p>
 * 1. reinforces its strongest country
 *
 * <p>
 * 2. always attack with it until it cannot attack anymore
 *
 * <p>
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
     * <p>
     * get number of army to place.
     *
     * <code>reinforce countryname num</code>
     *
     * @param player             the player
     * @param countryToReinforce the country to reinforce
     */
    @Override
    public void reinforce(Player player, Country countryToReinforce) {
        reinforce(countryToReinforce, player.getUnplacedArmies());
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

        for (Country defendingCountry : neighbours) {
            // int attackingCountryArmyAmount = attackingCountry.getArmyAmount();
            if (defendingCountry.getConquerer() == player) {
                continue;
            }

            attack(attackingCountry, defendingCountry, 0);

            if (player.isGameEnded()) {
                // because right after attack, if player win, the attack move command will invalid
                return attackedCountries;
            }

            // after attack with allout
            if (defendingCountry.getConquerer() == player) {
                // player conquered the defending country
                int armyToMove = GameBoard.getInstance().getGameBoardPlaying().getAttackerNumDice();
                attackMove(armyToMove);
                attackedCountries.add(defendingCountry);
            }

            if (!player.furtherAttackPossible()) {
                return attackedCountries;
            }
        }

        attackEnd();

        return attackedCountries;
    }

    /**
     * Fortify Phase
     *
     * <p>
     * Command:
     *
     * <code>fortify fromcountry tocountry num</code>
     *
     * <p>
     * get max number of armies to move then
     *
     * <p>
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
    public void playTurn(Player player) {
        exchangeCards(player);

        Country strongestPlayerCountry = getStrongestCountryToReinforce(player);

        if (strongestPlayerCountry != null) {
            reinforce(player, strongestPlayerCountry);
        }

        ArrayList<Country> attackedCountries = attack(player, strongestPlayerCountry);

        if (player.isGameEnded()) {
            player.setEndOfGamePhase();
        } else {

            if (attackedCountries.size() != 0) {
                int index = attackedCountries.size() - 1;
                Country fortifyToCountry = attackedCountries.get(index);

                fortify(strongestPlayerCountry, fortifyToCountry);
            }

            fortifyNone();
        }
    }
}
