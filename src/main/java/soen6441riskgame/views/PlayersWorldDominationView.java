package soen6441riskgame.views;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;
import soen6441riskgame.utils.presenter.WindowOutputStream;
import soen6441riskgame.utils.presenter.WindowPane;

/**
 * Implementation of a “players world domination view” using the Observer pattern. The players world
 * domination view should display
 *
 * 1. the percentage of the map controlled by every player
 *
 * 2. the continents controlled by every player
 *
 * 3. the total number of armies owned by every player
 *
 */
public class PlayersWorldDominationView implements Observer {
    private WindowPane presenter;
    private PrintStream printStream;

    public PlayersWorldDominationView() {
        init();
    }

    private void init() {
        presenter = ConsolePrinter.createWindowPane("Players World Domination", 600, 600);
        printStream = new PrintStream(new WindowOutputStream(presenter));
    }

    /**
     * update the view when property changed
     */
    @Override
    public void update(Observable o, Object arg) {
        ArrayList<Player> players = GameBoard.getInstance().getGameBoardPlayer().getPlayers();

        int totalNumberOfCountries = GameBoard.getInstance().getGameBoardMap().getCountries().size();

        for (Player player : players) {
            double percentConquered = (player.getConqueredCountries().size() * 100) / totalNumberOfCountries;

            ConsolePrinter.printFormat(printStream,
                                       "Player %s conquered %.2f percent of the world, having %d armies",
                                       player.getName(),
                                       percentConquered,
                                       player.getTotalArmies());

            ArrayList<Continent> conqueredContinents = player.getConqueredContinents();
            ConsolePrinter.printFormat(printStream,
                                       "Conquered continents: %d",
                                       conqueredContinents.size());

            for (Continent continent : conqueredContinents) {
                ConsolePrinter.printFormat(printStream,
                                           "    %s",
                                           continent.getName());
            }
        }
    }
}