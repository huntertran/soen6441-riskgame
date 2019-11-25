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
    public static final String ALLOUT = "allout";

    public static final String DASH = "-";
    public static final String SPACE = " ";
    public static final String NOATTACK = "noattack";

    public static final String SAVEGAME = "savegame";
    public static final String LOADGAME = "loadgame";

    public static final String TOURNAMENT = "tournament";
    public static final String TOURNAMENT_MAP_LIST = "-M";
    public static final String TOURNAMENT_PLAYER_STRATEGY_LIST = "-P";
    public static final String TOURNAMENT_NUMBER_OF_GAME = "-G";
    public static final String TOURNAMENT_MAX_NUMBER_OF_TURN = "-D";
}