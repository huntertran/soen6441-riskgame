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

    /**
     * do nothing
     * 
     * @param player             this method should not be call in Human Strategy
     * @param countryToReinforce this method should not be call in Human Strategy
     */
    @Override
    public void reinforce(Player player, Country countryToReinforce) {
        return;
    }

    /**
     * do nothing
     * 
     * @param player           this method should not be call in Human Strategy
     * @param attackingCountry this method should not be call in Human Strategy
     */
    @Override
    public ArrayList<Country> attack(Player player, Country attackingCountry) {
        return null;
    }

    /**
     * do nothing
     * 
     * @param fromCountry this method should not be call in Human Strategy
     * @param toCountry   this method should not be call in Human Strategy
     */
    @Override
    public void fortify(Country fromCountry, Country toCountry) {
        return;
    }

    /**
     * Allow the human player to play the game
     * 
     * @param player human player
     */
    @Override
    public void playTurn(Player player) {
        ConsolePrinter.printFormat(GameBoard.getInstance().standardPrintStream,
                                   "HUMAN PLAYER TURN");
        ConsolePrinter.printFormat(GameBoard.getInstance().standardPrintStream,
                                   false,
                                   "ENTER YOUR ACTION: ");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        while (player.isPlaying() && player.getCurrentPhase() != GamePhase.END_OF_GAME) {
            ModelCommands cmds = new ModelCommands(command);
            App.jumpToCommand(cmds);
            ConsolePrinter.printFormat(GameBoard.getInstance().standardPrintStream,
                                       false,
                                       "ENTER YOUR ACTION: ");
            command = scanner.nextLine();
        }

        scanner.close();
    }
}