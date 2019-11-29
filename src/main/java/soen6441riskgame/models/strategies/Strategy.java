package soen6441riskgame.models.strategies;

import java.util.ArrayList;

import soen6441riskgame.App;
import soen6441riskgame.enums.StrategyName;
import soen6441riskgame.models.CardSet;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;

/**
 * the interface for computer playing strategy
 */
public interface Strategy {
    /**
     * get strategy name
     *
     * @return the name of the strategy as enum
     */
    StrategyName getName();

    /**
     * reinforce
     *
     * @param player             current player
     * @param countryToReinforce country to reinforce
     */
    void reinforce(Player player, Country countryToReinforce);

    default void reinforce(Country countryToReinforce, int numberOfArmies) {
        String command = GameCommands.REINFORCE;
        command += GameCommands.SPACE;
        command += countryToReinforce.getName();
        command += GameCommands.SPACE;
        command += String.valueOf(numberOfArmies);
        App.jumpToCommand(new ModelCommands(command));
    }

    /**
     * default exchange cards commands
     * 
     * @param player player to exchange
     */
    default void exchangeCards(Player player) {
        if (player.getHoldingCards().size() >= 5) {
            ArrayList<CardSet> cardSets = player.buildValidCardSets();

            StringBuilder command = new StringBuilder(GameCommands.EXCHANGECARDS);
            command.append(GameCommands.SPACE);
            for (CardSet cardSet : cardSets) {
                command.append(cardSet.getCardsPositionForTournament());
                command.append(" ");
            }

            command.append(" -none");

            App.jumpToCommand(new ModelCommands(command.toString()));
        }
    }

    /**
     * attack
     *
     * @param player           current player
     * @param attackingCountry attack from
     * @return list of attacked countries
     */
    ArrayList<Country> attack(Player player, Country attackingCountry);

    /**
     * default attack command
     * 
     * @param attackingCountry attacking country
     * @param defendingCountry defending country
     * @param numberOfDice     number of dice
     */
    default void attack(Country attackingCountry, Country defendingCountry, int numberOfDice) {
        String command = GameCommands.ATTACK;
        command += GameCommands.SPACE;
        command += attackingCountry.getName();
        command += GameCommands.SPACE;
        command += defendingCountry.getName();
        command += GameCommands.SPACE;

        if (numberOfDice == 0) {
            command += GameCommands.DASH;
            command += GameCommands.ALLOUT;
        } else {
            command += String.valueOf(numberOfDice);
        }

        App.jumpToCommand(new ModelCommands(command));
    }

    /**
     * default attack move command
     * 
     * @param army number of army to move
     */
    default void attackMove(int army) {
        String command = GameCommands.ATTACKMOVE;
        command += GameCommands.SPACE;
        command += army;
        App.jumpToCommand(new ModelCommands(command));
    }

    default void attackEnd() {
        App.jumpToCommand(new ModelCommands(GameCommands.ATTACK
                                            + GameCommands.SPACE
                                            + GameCommands.DASH
                                            + GameCommands.NOATTACK));
    }

    /**
     * fortify
     *
     * @param fromCountry from country
     * @param toCountry   to country
     */
    void fortify(Country fromCountry, Country toCountry);

    /**
     * default fortify
     *
     * @param fromCountry  from country
     * @param toCountry    to country
     * @param armiesAmount number of armies
     */
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

    /**
     * call fortify none to give turn to next player
     */
    default void fortifyNone() {
        App.jumpToCommand(new ModelCommands(GameCommands.FORTIFY
                                            + GameCommands.SPACE
                                            + GameCommands.DASH
                                            + GameCommands.NONE));
    }

    /**
     * play the player's turn with the strategy
     *
     * @param player current player
     */
    void playTurn(Player player);
}
