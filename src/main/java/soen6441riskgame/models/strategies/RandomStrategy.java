package soen6441riskgame.models.strategies;

import java.util.ArrayList;
import soen6441riskgame.App;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.GameHelper;

public class RandomStrategy implements IStrategy {

    public int getDiceCount(Country atk) {
        int dices = 1;

        for (int i = 3; i > 0; i--) {
            if ((atk.getArmyAmount() - 1) < i) {
                continue;
            } else {
                dices = i;
                break;
            }
        }
        return dices;
    }

    @Override
    public void execute(GameBoard board, Player p) {
        ArrayList<Country> conquered = p.getConqueredCountries();

        int randIndex = GameHelper.randomNumberGenerator(0, (conquered.size() - 1));
        String countryName = conquered.get(randIndex).getName();

        ModelCommands cmds;
        String command;

        // Reinforce Phase
        // get number of army to place.
        // reinforce countryname num
        command = GameCommands.REINFORCE.toString();
        command += GameCommands.SPACE.toString();
        command += countryName;
        command += GameCommands.SPACE.toString();
        command += String.valueOf(p.getUnplacedArmies());
        cmds = new ModelCommands(command);
        App.jumpToCommand(cmds);

        // Attack Phase
        int randAtkValue = GameHelper.randomNumberGenerator(0, 5);
        ArrayList<Country> attackingCountryList = GameHelper.filterAttackableCountries(conquered);

        for (int i = 0; i < randAtkValue; i++) {
            randIndex = GameHelper.randomNumberGenerator(0, (attackingCountryList.size() - 1));
            Country atkCountry = attackingCountryList.get(randIndex);
            ArrayList<Country> neighbours = atkCountry.getNeighbors();
            int randNeighborIndex = GameHelper.randomNumberGenerator(0, neighbours.size());

            // Attack
            // Command: attack countrynamefrom countynameto numdice
            command = GameCommands.ATTACK.toString();
            command += GameCommands.SPACE.toString();
            command += atkCountry.getName();
            command += GameCommands.SPACE.toString();
            command += neighbours.get(randNeighborIndex).getName();
            command += GameCommands.SPACE.toString();
            command += String.valueOf(getDiceCount(atkCountry));

            cmds = new ModelCommands(command);
            App.jumpToCommand(cmds);
        }

        // Fortify Phase
        // Command: fortify fromcountry tocountry num
        /*
         * 1. get list of conquered countries 2. get filtered list of conquered countries to move army from
         * 3. generate index of country from filtered list of conquered countries 4. generate randArmyMove
         * int (min 1, max = number of army moving from country filtered)
         */
        conquered = p.getConqueredCountries();
        ArrayList<Country> moveArmyFrom = GameHelper.filterAttackableCountries(conquered);

        int randIndexCountryFrom = GameHelper.randomNumberGenerator(0, (moveArmyFrom.size() - 1));
        Country countryFrom = moveArmyFrom.get(randIndexCountryFrom);

        int randIndexCountryTo = GameHelper.randomNumberGenerator(0, (conquered.size() - 1));
        Country countryTo = conquered.get(randIndexCountryTo);

        int randArmyAmountToMove = GameHelper.randomNumberGenerator(1, countryFrom.getArmyAmount());

        // Do fortify
        command = GameCommands.FORTIFY.toString();
        command += GameCommands.SPACE.toString();
        command += countryFrom.getName();
        command += GameCommands.SPACE.toString();
        command += countryTo.getName();
        command += GameCommands.SPACE.toString();
        command += String.valueOf(randArmyAmountToMove);

        cmds = new ModelCommands(command);
        App.jumpToCommand(cmds);
    }
}