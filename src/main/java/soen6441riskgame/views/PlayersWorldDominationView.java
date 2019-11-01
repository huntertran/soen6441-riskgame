package soen6441riskgame.views;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameBoard;

public class PlayersWorldDominationView implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        ArrayList<Player> players = GameBoard.getInstance().getGameBoardPlayer().getPlayers();
        // TODO: print the players domination percentage
    }

}