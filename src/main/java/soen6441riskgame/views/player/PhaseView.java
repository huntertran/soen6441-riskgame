package soen6441riskgame.views.player;

import java.util.Observable;
import java.util.Observer;

import soen6441riskgame.models.Player;
import soen6441riskgame.utils.ConsolePrinter;

public class PhaseView implements Observer {

    /**
     * update the PhaseView with new data
     *
     * @param o   player that updated
     * @param arg extra arg for update
     */
    @Override
    public void update(Observable o, Object arg) {
        Player player = (Player) o;

        if (player != null) {
            ConsolePrinter.printFormat("Current phase: %s", player.getCurrentPhase());
            ConsolePrinter.printFormat("Current player: %s", player.getName());

            if (player.getCurrentPhaseActions().size() > 0) {
                ConsolePrinter.printFormat("Actions:");

                for (String action : player.getCurrentPhaseActions()) {
                    ConsolePrinter.printFormat("    %s", action);
                }
            }
        }
    }
}