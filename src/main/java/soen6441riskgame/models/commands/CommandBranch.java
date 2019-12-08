package soen6441riskgame.models.commands;

import soen6441riskgame.enums.CommonCommandArgs;

public class CommandBranch {
    private CommonCommandArgs action;
    private int numberOfArgs;

    public CommandBranch(CommonCommandArgs action, int numberOfArgs) {
        this.action = action;
        this.numberOfArgs = numberOfArgs;
    }

    public CommonCommandArgs getAction() {
        return action;
    }

    public int getNumberOfArgs() {
        return numberOfArgs;
    }
}