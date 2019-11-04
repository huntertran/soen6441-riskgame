package soen6441riskgame.enums;

/**
 * This enum has Common command arguments for <code>-add</code> or <code>-remove</code>
 */
public enum CommonCommandArgs {
    ADD("-add"), REMOVE("-remove"), NONE("-none"), INVALID("");

    private String argument;

    CommonCommandArgs(final String argument) {
        this.argument = argument;
    }

    /**
     * get the string arg
     * @return
     */
    public String getArgument() {
        return argument;
    }

    /**
     * convert the string arg to enum
     * @param argumentString the arg string
     * @return the arg converted to enum
     */
    public static CommonCommandArgs fromString(String argumentString) {
        for (CommonCommandArgs argument : values()) {
            if (argument.getArgument().replace("-", "").equals(argumentString.toLowerCase().replace("-", ""))) {
                return argument;
            }
        }

        return INVALID;
    }
}
