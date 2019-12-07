package soen6441riskgame.models;

import java.util.ArrayList;
import java.util.List;

import soen6441riskgame.enums.CommonCommandArgs;
import soen6441riskgame.models.commands.Parsable;
import soen6441riskgame.utils.Parser;

public class CommandRoutine {
    private CommonCommandArgs action;

    private List<Parsable> actionArguments;

    /**
     * construct a command routine
     * 
     * @param action            action text start with <code>-</code> or <code>--</code>
     * @param parsableArguments action arguments, could be country, continent, number, etc
     */
    public CommandRoutine(CommonCommandArgs action,
                          ArrayList<Class<?>> parsableArguments,
                          String[] objectValues) {
        actionArguments = new ArrayList<Parsable>();

        for (int index = 0; index < parsableArguments.size(); index++) {
            actionArguments.add(Parser.parseObject(objectValues[index], parsableArguments.get(index)));
        }
    }

    /**
     * @return the actionText
     */
    public CommonCommandArgs getActionText() {
        return action;
    }

    /**
     * @return the actionArguments
     */
    public List<Parsable> getActionArguments() {
        return actionArguments;
    }

    /**
     * @param actionArguments the actionArguments to set
     */
    public void setActionArguments(List<Parsable> actionArguments) {
        this.actionArguments = actionArguments;
    }

    /**
     * @param actionText the actionText to set
     */
    public void setActionText(CommonCommandArgs actionText) {
        this.action = actionText;
    }
}