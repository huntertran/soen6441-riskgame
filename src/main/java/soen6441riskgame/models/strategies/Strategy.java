package soen6441riskgame.models.strategies;

import java.util.ArrayList;

import soen6441riskgame.App;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;

public interface Strategy {
    void reinforce(Player player, Country countryToReinforce);

    ArrayList<Country> attack(Player player, Country attackingCountry);

    void fortify(Country fromCountry, Country toCountry);

    default void fortify(Country fromCountry, Country toCountry, int armiesAmount) {
        String command = GameCommands.FORTIFY;
        command += GameCommands.SPACE;
        command += fromCountry.getName();
        command += GameCommands.SPACE;
        command += toCountry.getName();
        command += GameCommands.SPACE;
        command += String.valueOf(armiesAmount);

        App.jumpToCommand(new ModelCommands(command));
    }

    default void fortifyNone() {
        App.jumpToCommand(new ModelCommands(GameCommands.FORTIFY
                                            + GameCommands.SPACE
                                            + GameCommands.DASH
                                            + GameCommands.NONE));
    }

    void execute(Player player);
}
