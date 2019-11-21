package soen6441riskgame.models;

import soen6441riskgame.singleton.GameBoard;

interface IStrategy {
    public void execute(GameBoard board, Player p);
}