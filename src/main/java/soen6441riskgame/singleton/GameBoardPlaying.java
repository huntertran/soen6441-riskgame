package soen6441riskgame.singleton;

import soen6441riskgame.models.Country;

public class GameBoardPlaying implements Resettable {

    /**
     * The number of dice for attacker
     */
    public int attackerNumDice = 0;
    /**
     * The number of dice for defender
     */
    public int defenderNumDice = 0;
    /**
     * The country attacking country
     */
    public Country attackingCountry = null;
    /**
     * The country defending country
     */
    public Country defendingCountry = null;
    /**
     * The all out
     */
    public boolean alloutFlag = false;
    /**
     * The attack move cmd required
     */
    public boolean attackMoveCmdRequired = false;

    /**
     * it resets the attack variables
     */
    @Override
    public void reset() {
        defendingCountry = null;
        attackingCountry = null;
        attackerNumDice = 0;
        defenderNumDice = 0;
    }

}