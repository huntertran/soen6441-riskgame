package soen6441riskgame.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import de.vandermeer.asciitable.AsciiTable;
import soen6441riskgame.App;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.enums.StrategyName;
import soen6441riskgame.models.Boundary;
import soen6441riskgame.models.ModelCommands;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.models.commands.MapEditorCommands;
import soen6441riskgame.models.commands.TournamentCommands;
import soen6441riskgame.models.strategies.Strategy;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;
import soen6441riskgame.utils.GameHelper;
import soen6441riskgame.utils.Parser;

/**
 * The tournament should proceed without any user interaction and show the results of the tournament
 * at the end. Both the phase view and player domination view should be enabled in tournament mode.
 *
 * A tournament starts with the user choosing
 *
 * M = 1 to 5 different maps
 *
 * P = 2 to 4 different computer players strategies
 *
 * G = 1 to 5 games to be played on each map
 *
 * D = 10 to 50 maximum number of turns for each game.
 *
 * A tournament is then automatically played by playing G games on each of the M different maps
 * between the chosen computer player strategies. In order to minimize run completion time, each
 * game should be declared a draw after D turns. Once started, the tournament plays all the games
 * automatically without user interaction. At the end of the tournament, a report of the results
 * should be displayed, e.g.
 *
 * M: Map1, Map2, Map3
 *
 * P: Aggressive, Benevolent, Random, Cheater.
 *
 * G: 4
 *
 * D: 30
 *
 * <table summary="sample result">
 * <tr>
 * <th></th>
 * <th>Game 1</th>
 * <th>Game 2</th>
 * <th>Game 3</th>
 * <th>Game 4</th>
 * </tr>
 * <tr>
 * <td>Map 1</td>
 * <td>Aggressive</td>
 * <td>Random</td>
 * <td>Cheater</td>
 * <td>Cheater</td>
 * </tr>
 * <tr>
 * <td>Map 2</td>
 * <td>Cheater</td>
 * <td>Draw</td>
 * <td>Cheater</td>
 * <td>Aggressive</td>
 * </tr>
 * <tr>
 * <td>Map 3</td>
 * <td>Cheater</td>
 * <td>Aggressive</td>
 * <td>Cheater</td>
 * <td>Draw</td>
 * </tr>
 * </table>
 */
public class TournamentController {
    public static final Boundary mapBoundary = new Boundary(1, 6);
    public static final Boundary strategyBoundary = new Boundary(2, 4);
    public static final Boundary numberOfGameBoundary = new Boundary(1, 5);
    public static final Boundary maxNumberOfTurnBoundary = new Boundary(10, 50);

    private Strategy[] strategies;
    private String[] maps;
    private int numberOfGame;
    private int maxNumberOfTurn;

    private GameController gameController;

    /**
     * because of ModelCommands logic, this function have to do extra work to take the correct argument
     * for each parameter
     *
     * @param args tournament's parameters
     */
    public void enterTournament(List<String> args) {

        String[] parameters = new String[args.size()];
        parameters = args.toArray(parameters);
        parseTournamentParameters(parameters);

        String[][] results = new String[numberOfGame][maps.length];

        if (isTournamentValid()) {
            gameController = new GameController();
            for (int gameIndex = 0; gameIndex < numberOfGame; gameIndex++) {
                for (int mapIndex = 0; mapIndex < maps.length; mapIndex++) {
                    Player winner = simulateGamePlay(maps[mapIndex]);
                    if (winner == null) {
                        results[gameIndex][mapIndex] = "DRAW";
                    } else {
                        results[gameIndex][mapIndex] = winner.getName();
                    }
                }
            }

            ConsolePrinter.printFormat(getGamePlayResultAsTable(results));
        } else {
            ConsolePrinter.printFormat("Parameter invalid");
        }
    }

    /**
     * convert the game play results to a table-like string
     * 
     * @param results game play results
     * @return printable string
     */
    private String getGamePlayResultAsTable(String[][] results) {
        AsciiTable table = new AsciiTable();

        ArrayList<String> mapHeaders = new ArrayList<String>(Arrays.asList(maps));
        mapHeaders.add(0, "");

        table.addRule();
        table.addRow(mapHeaders);

        int gameIndex = 1;
        for (String[] row : results) {
            table.addRule();
            ArrayList<String> gameResultsWithColumn = new ArrayList<String>(Arrays.asList(row));
            gameResultsWithColumn.add(0, "Game " + gameIndex);
            table.addRow(gameResultsWithColumn);
            gameIndex++;
        }

        table.addRule();

        return table.render();
    }

    /**
     * simulate a game play
     * 
     * @param map map to play
     * @return the winner of this game
     */
    private Player simulateGamePlay(String map) {
        GameBoard.getInstance().reset();
        App.jumpToCommand(new ModelCommands(MapEditorCommands.LOADMAP + " " + map));

        initializePlayers();
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));
        App.jumpToCommand(new ModelCommands(GameCommands.PLACEALL));

        // get first player
        Player currentPlayer = gameController.getCurrentPlayer();

        for (int turnPlayed = 0; turnPlayed < maxNumberOfTurn; turnPlayed++) {

            currentPlayer.getStrategy().playTurn(currentPlayer);

            if (currentPlayer.getCurrentPhase() == GamePhase.END_OF_GAME) {
                return currentPlayer;
            }

            currentPlayer = gameController.getCurrentPlayer();
        }

        return null;
    }

    /**
     * init players with strategy
     */
    private void initializePlayers() {
        StringBuilder addPlayerCommand = new StringBuilder(GameCommands.GAMEPLAYER + " ");
        for (Strategy strategy : strategies) {
            addPlayerCommand.append("-add ")
                            .append(strategy.getName().toString())
                            .append(" ");
        }

        App.jumpToCommand(new ModelCommands(addPlayerCommand.toString()));

        for (Player player : GameBoard.getInstance().getGameBoardPlayer().getPlayers()) {
            player.setStrategy(StrategyName.fromString(player.getName()));
        }
    }

    /**
     * parse tournament parameters
     * 
     * @param args tournament parameters
     */
    private void parseTournamentParameters(String[] args) {
        for (int index = 0; index < args.length; index = index + 2) {
            switch (args[index]) {
                case TournamentCommands.TOURNAMENT_MAP_LIST: {
                    maps = args[index + 1].split(",");
                    break;
                }
                case TournamentCommands.TOURNAMENT_PLAYER_STRATEGY_LIST: {
                    String[] playerStrategies = args[index + 1].split(",");
                    ArrayList<Strategy> parsedStrategies = parsePlayerStrategies(playerStrategies);
                    strategies = new Strategy[parsedStrategies.size()];
                    strategies = parsedStrategies.toArray(strategies);
                    break;
                }
                case TournamentCommands.TOURNAMENT_NUMBER_OF_GAME: {
                    numberOfGame = Parser.parseWithDefault(args[index + 1], 0);
                    break;
                }
                case TournamentCommands.TOURNAMENT_MAX_NUMBER_OF_TURN: {
                    maxNumberOfTurn = Parser.parseWithDefault(args[index + 1], 0);
                    break;
                }
            }
        }
    }

    /**
     * parse player's strategies
     * 
     * @param playerStrategies player's strategies
     * @return a list of strategy
     */
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

    /**
     * M = 1 to 5 different maps
     *
     * @return is maps array valid
     */
    private boolean isMapsValid() {
        if (mapBoundary.isInBoundary(maps.length, true)) {
            return GameHelper.countDistinct(maps) == maps.length;
        }

        return false;
    }

    /**
     * P = 2 to 4 different computer players strategies
     *
     * @return is strategies valid
     */
    private boolean isStrategiesValid() {
        StrategyName[] strategyNames = new StrategyName[strategies.length];
        for (int i = 0; i < strategies.length; i++) {
            strategyNames[i] = strategies[i].getName();
        }

        if (strategyBoundary.isInBoundary(strategies.length, true)) {
            return GameHelper.countDistinct(strategyNames) == strategies.length;
        }

        return false;
    }

    /**
     * M = 1 to 5 different maps
     *
     * P = 2 to 4 different computer players strategies
     *
     * G = 1 to 5 games to be played on each map
     *
     * D = 10 to 50 maximum number of turns for each game.
     */
    private boolean isTournamentValid() {
        return isMapsValid()
               && isStrategiesValid()
               && numberOfGameBoundary.isInBoundary(numberOfGame, true)
               && maxNumberOfTurnBoundary.isInBoundary(maxNumberOfTurn, true);
    }
}
