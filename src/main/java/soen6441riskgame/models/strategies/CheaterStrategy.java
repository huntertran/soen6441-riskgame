package soen6441riskgame.models.strategies;

import java.util.ArrayList;

import soen6441riskgame.App;
import soen6441riskgame.enums.StrategyName;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;

/**
 * reinforce() method doubles the number of armies on all its countries
 * 
 * attack() method automatically conquers all the neighbors of all its countries
 * 
 * fortify() method doubles the number of armies on its countries that have neighbors that belong to
 * other players.
 */
public class CheaterStrategy implements Strategy {

    /**
     * get strategy name
     * 
     * @return the name of the strategy as enum
     */
    @Override
    public StrategyName getName() {
        return StrategyName.CHEATER;
    }

    /**
     * reinforce
     * 
     * @param player             current player
     * @param countryToReinforce country to reinforce
     */
    @Override
    public void reinforce(Player player, Country countryToReinforce) {
        ArrayList<Country> conqueredCountries = player.getConqueredCountries();

        for (Country country : conqueredCountries) {
            int armyToReinforce = country.getArmyAmount();
            player.setUnplacedArmies(armyToReinforce);

            String command = GameCommands.REINFORCE;
            command += GameCommands.SPACE;
            command += country.getName();
            command += GameCommands.SPACE;
            command += String.valueOf(player.getUnplacedArmies());

            App.jumpToCommand(new ModelCommands(command));
        }
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
        ArrayList<Country> conqueredCountries = player.getConqueredCountries();

        for (Country country : conqueredCountries) {
            ArrayList<Country> neighbors = country.getNeighbors();
            for (Country neighbor : neighbors) {
                if (neighbor.getConquerer() != player) {
                    neighbor.setConquerer(player);
                }
            }
        }

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
        int toCountryArmy = toCountry.getArmyAmount();
        fromCountry.setArmyAmount(fromCountry.getArmyAmount() + toCountryArmy);

        fortify(fromCountry, toCountry, toCountryArmy);
    }

    /**
     * execute the strategy
     * 
     * @param player current player
     */
    @Override
    public void playTurn(Player player) {
        reinforce(player, null);
        attack(player, null);

        // the cheater strategy required re-implementation of fortify command
        ArrayList<Country> countriesToFortify = getCountriesToFortify(player);
        for (Country toCountry : countriesToFortify) {
            Country fromCountry = getFortifyFromCountry(player, toCountry);
            fortify(fromCountry, toCountry);
        }

        fortifyNone();
    }

    /**
     * choose the first country belong to player from list of neighbors
     * 
     * @param player    current player
     * @param toCountry origin country
     * @return to country
     */
    private Country getFortifyFromCountry(Player player, Country toCountry) {
        Country fromCountry = null;
        ArrayList<Country> neighbors = toCountry.getNeighbors();
        for (Country neighbor : neighbors) {
            if (neighbor.getConquerer() == player) {
                fromCountry = neighbor;
                break;
            }
        }
        return fromCountry;
    }

    /**
     * get all the countries that need to be fortified
     * 
     * @param player current player
     * @return all the countries that need to be fortified
     */
    private ArrayList<Country> getCountriesToFortify(Player player) {
        ArrayList<Country> countriesToFortify = new ArrayList<Country>();
        ArrayList<Country> conqueredCountries = player.getConqueredCountries();
        for (Country conqueredCountry : conqueredCountries) {
            ArrayList<Country> neighbors = conqueredCountry.getNeighbors();
            for (Country neighbor : neighbors) {
                if (neighbor.getConquerer() != player) {
                    if (!countriesToFortify.contains(conqueredCountry)) {
                        countriesToFortify.add(conqueredCountry);
                    }

                    break;
                }
            }
        }

        return countriesToFortify;
    }

}
