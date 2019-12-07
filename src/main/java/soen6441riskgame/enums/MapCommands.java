package soen6441riskgame.enums;

import java.util.ArrayList;
import java.util.Arrays;

import soen6441riskgame.models.CommandRoutine;
import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;

public enum MapCommands {
                         EDIT_CONTINENT("editcontinent", Continent.class, Integer.class),
                         EDIT_COUNTRY("editcountry", Country.class, Continent.class),
                         EDIT_NEIGHBOR("editneighbor", Country.class, Country.class),
                         SHOWMAP("showmap"),
                         SAVEMAP("savemap", String.class),
                         EDITMAP("editmap", String.class),
                         VALIDATEMAP("validatemap"),
                         LOADMAP("loadmap", String.class),
                         NONE("");

    private String action;
    private ArrayList<CommandRoutine> routines;
    private Class<?>[] commandArguments;

    MapCommands(String action, Class<?>... commandArguments) {
        this.action = action;
        this.commandArguments = commandArguments;
    }

    /**
     * init command with command text
     * 
     * @param commandText command text from user's input
     */
    public void init(String commandText) {
        routines = new ArrayList<>();

        var subCommands = commandText.split("\\s-");
        for (String subCommand : subCommands) {
            var fragments = subCommand.split(" ");

            var action = CommonCommandArgs.fromString(fragments[0]);
            var parsableArguments = new ArrayList<Class<?>>();

            for (int index = 1; index < fragments.length; index++) {
                parsableArguments.add(commandArguments[index]);
            }

            var commandRoutine = new CommandRoutine(action,
                                                    parsableArguments,
                                                    Arrays.copyOfRange(fragments, 1, fragments.length));
            routines.add(commandRoutine);
        }
    }

    public String getAction() {
        return action;
    }

    public Class<?>[] getCommandArguments() {
        return commandArguments;
    }

    /**
     * parse command
     * 
     * @param commandText command text from user input
     * @return command with sub actions
     */
    public MapCommands parseCommand(String commandText) {
        var action = commandText.split(" ")[0];

        for (MapCommands command : values()) {
            if (command.getAction().equals(action)) {
                command.init(commandText);
                return command;
            }
        }

        return NONE;
    }
}