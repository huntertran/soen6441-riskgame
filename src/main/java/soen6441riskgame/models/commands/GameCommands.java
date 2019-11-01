package soen6441riskgame.models.commands;

/**
 * All commands for playing game
 */
public final class GameCommands {

    // Startup phase
    public static final String GAMEPLAYER = "gameplayer";
    public static final String POPULATECOUNTRIES = "populatecountries";
    public static final String PLACEARMY = "placearmy";
    public static final String PLACEALL = "placeall";

    // Reinforcement phase
    public static final String REINFORCE = "reinforce";

    //Attack phase
    public static final String ATTACK = "attack";

    //Defend from attack
    public static final String DEFEND = "defend";

    // Fortification phase
    public static final String FORTIFY = "fortify";

    // Current player
    public static final String CURRENTPLAYER = "currentplayer";
    // Player Exchange Cards
    public static final String EXCHANGECARDS = "exchangecards";
    // None command
    public static final String NONE = "none";
    // EXIT GAME
    public static final String EXIT = "exit";

    public static final String ATTACKMOVE = "attackmove";
}