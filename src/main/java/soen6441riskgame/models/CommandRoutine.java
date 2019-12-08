package soen6441riskgame.models;

import java.util.ArrayList;
import java.util.List;

import soen6441riskgame.enums.CommonCommandArgs;
import soen6441riskgame.models.commands.Argument;
import soen6441riskgame.models.commands.CommandBranch;

public class CommandRoutine {
    private CommonCommandArgs action;
    private List<Argument> actionArguments;
    private CommandBranch branch;

    /**
     * construct a command routine
     * 
     * @param action            action text start with <code>-</code> or <code>--</code>
     * @param parsableArguments action arguments, could be country, continent, number, etc
     */
    public CommandRoutine(CommonCommandArgs action, ArrayList<Argument> parsableArguments) {
        this.action = action;
        this.actionArguments = parsableArguments;
    }

    /**
     * get the action
     * 
     * @return the CommonCommandAction
     */
    public CommonCommandArgs getAction() {
        return action;
    }

    /**
     * get the list of arguments
     * 
     * @return the list of arguments
     */
    public List<Argument> getActionArguments() {
        return actionArguments;
    }

    /**
     * set the command branch for this routine
     * 
     * @param availableBranches all available branches of parent command
     */
    public void setCommandBranch(List<CommandBranch> availableBranches) {
        for (CommandBranch branch : availableBranches) {
            if (branch.getAction() == action) {
                this.branch = branch;
                return;
            }
        }
    }

    public CommandBranch getBranch() {
        return branch;
    }
}