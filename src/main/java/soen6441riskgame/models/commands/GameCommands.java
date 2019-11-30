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
    public static final String EXCHANGECARDS = "exchangecards";
    public static final String REINFORCE = "reinforce";

    // Attack phase
    public static final String ATTACK = "attack";
    public static final String DEFEND = "defend";
    public static final String ATTACKMOVE = "attackmove";
    public static final String ALLOUT = "allout";
    public static final String NOATTACK = "noattack";

    // Fortification phase
    public static final String FORTIFY = "fortify";

    // save - load game
    public static final String SAVEGAME = "savegame";
    public static final String LOADGAME = "loadgame";

    // others
    public static final String CURRENTPLAYER = "currentplayer";

    public static final String NONE = "none";

    public static final String DASH = "-";
    public static final String SPACE = " ";

    // EXIT GAME
    public static final String EXIT = "exit";
}