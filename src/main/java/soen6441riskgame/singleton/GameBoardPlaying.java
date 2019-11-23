package soen6441riskgame.singleton;

import com.google.gson.annotations.Expose;

import soen6441riskgame.models.Country;

public class GameBoardPlaying implements Resettable {

    /**
     * The number of dice for attacker
     */
    @Expose
    private int attackerNumDice = 0;

    /**
     * The number of dice for defender
     */
    @Expose
    private int defenderNumDice = 0;

    /**
     * The country attacking country
     */
    private Country attackingCountry = null;

    /**
     * the name for save load function
     */
    @Expose
    private String attackingCountryName;

    /**
     * The country defending country
     */
    private Country defendingCountry = null;

    /**
     * the name for save load function
     */
    @Expose
    private String defendingCountyName;

    /**
     * The all out
     */
    @Expose
    private boolean alloutFlag = false;

    /**
     * The attack move cmd required
     */
    @Expose
    private boolean attackMoveCmdRequired = false;

    /**
     * The number of dice for attacker
     * 
     * @return The number of dice for attacker
     */
    public int getAttackerNumDice() {
        return attackerNumDice;
    }

    /**
     * set attacker number of dice
     *
     * @param attackerNumDice attacker number of dice
     */
    public void setAttackerNumDice(int attackerNumDice) {
        this.attackerNumDice = attackerNumDice;
    }

    /**
     * The number of dice for defender
     * 
     * @return The number of dice for defender
     */
    public int getDefenderNumDice() {
        return defenderNumDice;
    }

    public void setDefenderNumDice(int defenderNumDice) {
        this.defenderNumDice = defenderNumDice;
    }

    /**
     * The country attacking country
     * 
     * @return The country attacking country
     */
    public Country getAttackingCountry() {
        return attackingCountry;
    }

    public void setAttackingCountry(Country attackingCountry) {
        this.attackingCountry = attackingCountry;
        this.attackingCountryName = attackingCountry.getName();
    }

    /**
     * The country defending country
     * 
     * @return The country defending country
     */
    public Country getDefendingCountry() {
        return defendingCountry;
    }

    /**
     * set defending country
     *
     * @param defendingCountry country defending
     */
    public void setDefendingCountry(Country defendingCountry) {
        this.defendingCountry = defendingCountry;
        this.defendingCountyName = defendingCountry.getName();
    }

    /**
     * The all out
     * 
     * @return The all out
     */
    public boolean isAlloutFlag() {
        return alloutFlag;
    }

    /**
     * set allout flag
     *
     * @param alloutFlag is allout
     */
    public void setAlloutFlag(boolean alloutFlag) {
        this.alloutFlag = alloutFlag;
    }

    /**
     * The attack move cmd required
     * 
     * @return The attack move cmd required
     */
    public boolean isAttackMoveCmdRequired() {
        return attackMoveCmdRequired;
    }

    /**
     * set attack move command required or not
     *
     * @param attackMoveCmdRequired attack move command required or not
     */
    public void setAttackMoveCmdRequired(boolean attackMoveCmdRequired) {
        this.attackMoveCmdRequired = attackMoveCmdRequired;
    }

    /**
     * get attacking country name
     * 
     * @return get attacking country name
     */
    public String getAttackingCountryName() {
        return attackingCountryName;
    }

    /**
     * get defending country name
     * 
     * @return get defending country name
     */
    public String getDefendingCountyName() {
        return defendingCountyName;
    }

    /**
     * it resets the attack variables
     */
    @Override
    public void reset() {
        this.defendingCountry = null;
        this.defendingCountyName = null;
        this.attackingCountry = null;
        this.attackingCountryName = null;
        this.attackerNumDice = 0;
        this.defenderNumDice = 0;
    }
}
