package soen6441riskgame.views.player;

import java.util.Observable;
import java.util.Observer;

import soen6441riskgame.models.Player;
import soen6441riskgame.utils.ConsolePrinter;

public class PhaseView implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        Player player = (Player) o;

        if (player != null) {
            ConsolePrinter.printFormat("Current phase: %s", player.getCurrentPhase());
            ConsolePrinter.printFormat("Current player: %s", player.getName());
        }
    }
}