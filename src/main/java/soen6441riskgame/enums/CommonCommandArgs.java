package main.java.soen6441riskgame.enums;

public enum CommonCommandArgs {
    ADD("-add"), REMOVE("-remove"), NONE("");

    private String argument;

    CommonCommandArgs(final String argument) {
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }

    public static CommonCommandArgs fromString(String argumentString) {
        for (CommonCommandArgs argument : values()) {
            if (argument.getArgument().replace("-","").equals(argumentString.toLowerCase().replace("-",""))) {
                return argument;
            }
        }

        return NONE;
    }
}