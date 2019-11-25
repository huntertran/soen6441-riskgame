package soen6441riskgame.controllers;

import java.util.ArrayList;

import soen6441riskgame.enums.StrategyName;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.models.strategies.Strategy;

public class TournamentController {
    private Strategy[] strategies;

    /**
     * because of ModelCommands logic, this function have to do extra work to take the correct argument
     * for each parameter
     * 
     * @param args tournament's parameters
     */
    public void enterTournament(String[] args) {
        String[] maps;
        String[] playerStrategies = {};
        String numberOfGame;
        String maxNumberOfTurns;

        for (int index = 0; index < args.length; index = index + 2) {
            switch (args[index]) {
                case GameCommands.TOURNAMENT_MAP_LIST: {
                    maps = args[index + 1].split(",");
                    break;
                }
                case GameCommands.TOURNAMENT_PLAYER_STRATEGY_LIST: {
                    playerStrategies = args[index + 1].split(",");
                    break;
                }
                case GameCommands.TOURNAMENT_NUMBER_OF_GAME: {
                    numberOfGame = args[index + 1];
                    break;
                }
                case GameCommands.TOURNAMENT_MAX_NUMBER_OF_TURN: {
                    numberOfGame = args[index + 1];
                    break;
                }
            }
        }

        ArrayList<Strategy> parsedStrategies = parsePlayerStrategies(playerStrategies);
        strategies = new Strategy[parsedStrategies.size()];
        strategies = parsedStrategies.toArray(strategies);
    }

    private ArrayList<Strategy> parsePlayerStrategies(String[] playerStrategies) {
        ArrayList<Strategy> strategies = new ArrayList<Strategy>();

        for (String strategy : playerStrategies) {
            Strategy parsed = StrategyName.fromString(strategy);
            if (parsed != null) {
                strategies.add(parsed);
            }
        }

        return strategies;
    }
}