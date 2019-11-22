package soen6441riskgame.models.strategies;

import java.util.ArrayList;
import soen6441riskgame.App;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.singleton.GameBoard;


class StrategyBenevolent implements IStrategy {

    @Override
    public void execute(GameBoard board, Player p) {
        ArrayList<Country> conquered = p.getConqueredCountries();
        Country weakestPlayerCountry = null;
        int minPlayerArmy = 0;
        int tempArmy = 0;

        // Find the weakest country from conqured country list
        for (Country country : conquered) {
            tempArmy = country.getArmyAmount();

            if(tempArmy <= minPlayerArmy) {
                weakestPlayerCountry = country;
                minPlayerArmy = tempArmy;
            }
        }

        ModelCommands cmds;
        String command;
        
        // Reinforce Phase
        // get number of army to place.
        // reinforce countryname num
        command = GameCommands.REINFORCE.toString();
        command += GameCommands.SPACE.toString();
        command += weakestPlayerCountry.getName();
        command += GameCommands.SPACE.toString();
        command += String.valueOf(p.getUnplacedArmies());
        cmds = new ModelCommands(command);
        App.jumpToCommand(cmds);

        // Attack Phase
        // Find adjacent neighbour countries to attack
        command = GameCommands.ATTACK.toString();
        command += GameCommands.SPACE.toString();
        command += GameCommands.DASH.toString();
        command += GameCommands.NOATTACK.toString();
        cmds = new ModelCommands(command);
        App.jumpToCommand(cmds);


        // Fortify Phase
        // Command: fortify fromcountry tocountry num
        // get max number of armies to move
        // then move the countries
        
        // Find the weaker country from conqured country list
        Country weakerPlayerCountry = null;
        int weakerPlayerArmy = 0;

        for (Country country : conquered) {
            tempArmy = country.getArmyAmount();

            if(tempArmy <= weakerPlayerArmy) {
                weakerPlayerCountry = country;
                weakerPlayerArmy = tempArmy;
            }
        }

        command = GameCommands.REINFORCE.toString();
        command += GameCommands.SPACE.toString();
        command += weakestPlayerCountry.getName();
        command += GameCommands.SPACE.toString();
        command += weakerPlayerCountry.getName();
        command += GameCommands.SPACE.toString();
        command += String.valueOf(weakerPlayerArmy);

        cmds = new ModelCommands(command);
        App.jumpToCommand(cmds);
    }
}