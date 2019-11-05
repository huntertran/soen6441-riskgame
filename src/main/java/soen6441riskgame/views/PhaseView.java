package soen6441riskgame.views;

import java.util.Observable;
import java.util.Observer;

import soen6441riskgame.enums.ChangedProperty;
import soen6441riskgame.models.Player;
import soen6441riskgame.utils.ConsolePrinter;

/**
 * Implementation of a “phase view” using the Observer pattern. The phase view should display:
 *
 * 1. the name of the game phase currently being played
 *
 * 2. the current player’s name
 *
 * 3. information about actions that are taking place during this phase. The phase view should be
 * cleared at the beginning of every phase.
 */
public class PhaseView implements Observer {

    /**
     * update the PhaseView with new data
     *
     * @param o   player that updated
     * @param arg extra arg for update
     */
    @Override
    public void update(Observable o, Object arg) {
        ChangedProperty property = (ChangedProperty) arg;
        if (property != ChangedProperty.GAME_PHASE) {
            return;
        }

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