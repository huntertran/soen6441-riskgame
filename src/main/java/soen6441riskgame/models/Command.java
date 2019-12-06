package soen6441riskgame.models;

import java.util.List;

public class Command {
    private String commandText;

    private List<CommandRoutine> routines;

    public Command(String command) {
        
    }

    /**
     * @return the commandText
     */
    public String getCommandText() {
        return commandText;
    }

    /**
     * @return the routines
     */
    public List<CommandRoutine> getRoutines() {
        return routines;
    }

    /**
     * @param routines the routines to set
     */
    public void setRoutines(List<CommandRoutine> routines) {
        this.routines = routines;
    }

    /**
     * @param commandText the commandText to set
     */
    public void setCommandText(String commandText) {
        this.commandText = commandText;
    }
}