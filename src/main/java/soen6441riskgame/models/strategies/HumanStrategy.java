package soen6441riskgame.models.strategies;

import java.util.ArrayList;
import java.util.Scanner;

import soen6441riskgame.App;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.enums.StrategyName;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;

public class HumanStrategy implements Strategy {
    /**
     * get strategy name
     * 
     * @return the name of the strategy as enum
     */
    @Override
    public StrategyName getName() {
        return StrategyName.HUMAN;
    }

    @Override
    public void reinforce(Player player, Country countryToReinforce) {
        return;
    }

    @Override
    public ArrayList<Country> attack(Player player, Country attackingCountry) {
        return null;
    }

    @Override
    public void fortify(Country fromCountry, Country toCountry) {
        return;
    }

    @Override
    public void playTurn(Player player) {
        ConsolePrinter.printFormat(GameBoard.getInstance().standardPrintStream,
                                   "HUMAN PLAYER TURN");
        Scanner scanner = new Scanner(System.in);

        while (player.isPlaying() && player.getCurrentPhase() != GamePhase.END_OF_GAME) {
            String command = scanner.nextLine();
            ModelCommands cmds = new ModelCommands(command);
            App.jumpToCommand(cmds);
            ConsolePrinter.printFormat(GameBoard.getInstance().standardPrintStream,
                                       "ENTER YOUR ACTION: ");
            command = scanner.nextLine();
        }

        scanner.close();
    }
}