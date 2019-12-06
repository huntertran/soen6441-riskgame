package soen6441riskgame.models;

import java.util.List;

import soen6441riskgame.enums.CommonCommandArgs;
import soen6441riskgame.models.commands.Parsable;

public class CommandRoutine {
    private String actionText;

    private List<Object> actionArguments;

    /**
     * construct a command routine
     * @param action action text start with <code>-</code> or <code>--</code>
     * @param actionArguments action arguments, could be country, continent, number, etc
     */
    public CommandRoutine(CommonCommandArgs action, Parsable... actionArguments) {

    }

    /**
     * @return the actionText
     */
    public String getActionText() {
        return actionText;
    }

    /**
     * @return the actionArguments
     */
    public List<Object> getActionArguments() {
        return actionArguments;
    }

    /**
     * @param actionArguments the actionArguments to set
     */
    public void setActionArguments(List<Object> actionArguments) {
        this.actionArguments = actionArguments;
    }

    /**
     * @param actionText the actionText to set
     */
    public void setActionText(String actionText) {
        this.actionText = actionText;
    }
}