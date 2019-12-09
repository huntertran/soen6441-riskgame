package soen6441riskgame.enums;

import java.util.ArrayList;
import java.util.List;

import soen6441riskgame.models.CommandRoutine;
import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.commands.Argument;
import soen6441riskgame.models.commands.CommandBranch;

public enum MapCommands {
                         EDITCONTINENT("editcontinent",
                                       List.of(new CommandBranch(CommonCommandArgs.ADD, 2),
                                               new CommandBranch(CommonCommandArgs.REMOVE, 1)),
                                       Continent.class,
                                       Integer.class),
                         EDITCOUNTRY("editcountry",
                                     List.of(new CommandBranch(CommonCommandArgs.ADD, 2),
                                             new CommandBranch(CommonCommandArgs.REMOVE, 1)),
                                     Country.class,
                                     Continent.class),
                         EDITNEIGHBOR("editneighbor",
                                      List.of(new CommandBranch(CommonCommandArgs.ADD, 2),
                                              new CommandBranch(CommonCommandArgs.REMOVE, 2)),
                                      Country.class,
                                      Country.class),
                         SHOWMAP("showmap"),
                         SAVEMAP("savemap", String.class),
                         EDITMAP("editmap", String.class),
                         VALIDATEMAP("validatemap"),
                         LOADMAP("loadmap", String.class),
                         NONE("");

    private String action;
    private ArrayList<CommandRoutine> routines;
    private Class<?>[] commandArgumentTypes;
    private List<CommandBranch> branches;

    MapCommands(String action,
                List<CommandBranch> branches,
                Class<?>... commandArgumentTypes) {
        this.action = action;
        this.commandArgumentTypes = commandArgumentTypes;
        this.branches = branches;
    }

    MapCommands(String action, Class<?>... commandArgumentTypes) {
        this.action = action;
        this.commandArgumentTypes = commandArgumentTypes;
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
            var parsableArguments = new ArrayList<Argument>();

            for (int index = 1; index < fragments.length; index++) {
                parsableArguments.add(new Argument(commandArgumentTypes[index], fragments[index]));
            }

            var commandRoutine = new CommandRoutine(action, parsableArguments);
            commandRoutine.setCommandBranch(branches);
            routines.add(commandRoutine);
        }
    }

    public String getAction() {
        return action;
    }

    public ArrayList<CommandRoutine> getCommandRoutines() {
        return routines;
    }

    /**
     * parse command
     * 
     * @param commandText command text from user input
     * @return command with sub actions
     */
    public static MapCommands parseCommand(String commandText) {
        var action = commandText.split(" ")[0];

        for (MapCommands command : values()) {
            if (command.getAction().equals(action)) {
                command.init(commandText.substring(action.length()).strip());
                return command;
            }
        }

        return NONE;
    }
}