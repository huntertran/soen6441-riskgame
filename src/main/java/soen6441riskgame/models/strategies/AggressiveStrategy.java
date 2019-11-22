package soen6441riskgame.models.strategies;

import java.util.ArrayList;

import soen6441riskgame.App;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.singleton.GameBoard;

public class AggressiveStrategy implements IStrategy {

    @Override
    public void execute(GameBoard board, Player p) {
        ArrayList<Country> conquered = p.getConqueredCountries();
        Country strongestPlayerCountry = null;
        int maxPlayerArmy = 0;
        int tempArmy = 0;

        // Find the strongest Country from conquered country list
        for (Country country : conquered) {
            tempArmy = country.getArmyAmount();

            if (tempArmy >= maxPlayerArmy) {
                strongestPlayerCountry = country;
                maxPlayerArmy = tempArmy;
            }
        }

        ModelCommands cmds;
        String command;

        // Reinforce Phase
        // get number of army to place.
        // reinforce countryname num
        command = GameCommands.REINFORCE.toString();
        command += GameCommands.SPACE.toString();
        command += strongestPlayerCountry.getName();
        command += GameCommands.SPACE.toString();
        command += String.valueOf(p.getUnplacedArmies());
        cmds = new ModelCommands(command);
        App.jumpToCommand(cmds);

        // Attack Phase
        // Find adjacent neighbor countries to attack
        ArrayList<Country> neighbours = strongestPlayerCountry.getNeighbors();
        ArrayList<Country> attackedCountries = new ArrayList<>();

        boolean flag = false;
        for (int i = 0; i < neighbours.size() || flag == true; i++) {
            int totalNumPlayerArmy = strongestPlayerCountry.getArmyAmount();
            Country country = neighbours.get(i);

            // Attacker lost all army in strongest country
            if (totalNumPlayerArmy <= 1) {
                flag = true;
                break;
            }
            // Attacker lost the strongest country
            else if (strongestPlayerCountry.getConquerer() == p) {
                flag = true;
                break;
            }
            // Attacker won the country
            else if (country.getConquerer() == p) {
                flag = true;
                break;
            }
            // Continue to attack neighbours
            else {
                command = GameCommands.ATTACK.toString();
                command += GameCommands.SPACE.toString();
                command += strongestPlayerCountry.getName();
                command += GameCommands.SPACE.toString();
                command += country.getName();
                command += GameCommands.SPACE.toString();
                command += GameCommands.DASH.toString();
                command += GameCommands.ALLOUT.toString();
                cmds = new ModelCommands(command);
                App.jumpToCommand(cmds);

                attackedCountries.add(country);
            }
        }

        // Fortify Phase
        // Command: fortify fromcountry tocountry num
        // get max number of armies to move
        // then move the countries

        int maxArmyToMove = strongestPlayerCountry.getArmyAmount() - 1;

        int index = attackedCountries.size() - 1;
        Country reinforceCountry = attackedCountries.get(index);

        command = GameCommands.REINFORCE.toString();
        command += GameCommands.SPACE.toString();
        command += strongestPlayerCountry.getName();
        command += GameCommands.SPACE.toString();
        command += reinforceCountry.getName();
        command += GameCommands.SPACE.toString();
        command += String.valueOf(maxArmyToMove);

        cmds = new ModelCommands(command);
        App.jumpToCommand(cmds);
    }

}