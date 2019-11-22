package soen6441riskgame.models.strategies;

import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameBoard;

public interface IStrategy {
    void execute(GameBoard board, Player p);
}