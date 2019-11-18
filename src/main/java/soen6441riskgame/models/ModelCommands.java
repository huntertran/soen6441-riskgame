package soen6441riskgame.models;

import java.util.ArrayList;
import java.util.List;

import soen6441riskgame.models.commands.MapEditorCommands;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.utils.Parser;

/**
 * Command Model Class Used for Creating the different arguments passed from console into object
 * separated as required.
 */
public class ModelCommands {

    // cmd <String> holds first command argument
    public String cmd;
    // regularCommands List<String> contains rest of argument in list of string format.
    public List<String> regularCommands = new ArrayList<String>();
    // subRoutine List<ModelCommandsPair> holds list of object ModelCommandsPair for specialized sub
    // commands.
    public List<ModelCommandsPair> subRoutine = new ArrayList<ModelCommandsPair>();

    /***
     * Class Constructor
     *
     * @param new_args = raw string input of the command line parameters
     */
    public ModelCommands(String new_args) {
        init(new_args);
    }

    private void init(String new_args) {
        // check if arguments provided is not null or empty string.
        if ((new_args == null) || (new_args == "")) {
            return;
        }

        // Split first word and rest of the string into two array.
        String[] temp_args = new_args.split(" ", 2);

        // assign command as the first value of the temp_args array
        this.cmd = temp_args[0].toLowerCase();

        if (temp_args.length <= 1) {
            return;
        }

        // Split rest of the string parameter except the command using "-"
        String[] paramsArray = temp_args[1].split("-");

        for (int i = 0; i < paramsArray.length; i++) {
            try {
                populateRegularCommandsOrSubRoutines(paramsArray, paramsArray[i]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid value detected.");
                continue;
            } catch (Exception e) {
                System.out.println("Invalid command detected.");
                continue;
            }
        }
    }

    private void populateRegularCommandsOrSubRoutines(String[] paramsArray, String s) throws Exception {
        // Split rest of the string parameter except the command using space (" ")
        String[] params = s.split(" ");

        buildSubRoutines(paramsArray, s, params);

        buildRegularCommands(paramsArray, s, params);
    }

    private void buildSubRoutines(String[] paramsArray, String s, String[] params) {
        for (int j = 0; j < params.length; j++) {
            // check for null or empty value after split
            if ((params[j] == null) || (params[j].equals(""))) {
                continue;
            }

            if (params[j].equalsIgnoreCase(MapEditorCommands.ADD)
                || params[j].equalsIgnoreCase(MapEditorCommands.REMOVE)) {
                // check specific validation criteria as per command
                // check if ADD command is provided
                if (params[j].equalsIgnoreCase(MapEditorCommands.ADD)) {
                    if (cmd.equalsIgnoreCase(MapEditorCommands.EDITCONTINENT)) {
                        // check if number of army are in number format
                        if (Parser.checkValidInputNumber(params[j + 2])) {
                            subRoutine.add(new ModelCommandsPair(params[j].toLowerCase(),
                                                                 params[j + 1],
                                                                 params[j + 2]));
                            break;
                        } else {
                            throw new NumberFormatException();
                        }
                    }
                    // check if the command is GAMEPLAYER
                    else if (cmd.equalsIgnoreCase(GameCommands.GAMEPLAYER)) {
                        subRoutine.add(new ModelCommandsPair(params[j].toLowerCase(), params[j + 1]));
                        break;
                    }
                    // command contains sub commands so add separately to subroutine list.
                    else {
                        subRoutine.add(new ModelCommandsPair(params[j].toLowerCase(),
                                                             params[j + 1],
                                                             params[j + 2]));
                        break;
                    }
                }
                // check if REMOVE command is provided
                else if (params[j].equalsIgnoreCase(MapEditorCommands.REMOVE)) {
                    subRoutine.add(new ModelCommandsPair(params[j].toLowerCase(), params[j + 1]));
                    break;
                }
            }
        }
    }

    private void buildRegularCommands(String[] paramsArray, String s, String[] params) throws Exception {
        for (int j = 0; j < params.length; j++) {
            // check for null or empty value after split
            if ((params[j] == null) || (params[j].equals(""))) {
                continue;
            }

            if (params[j].equalsIgnoreCase(MapEditorCommands.ADD)
                || params[j].equalsIgnoreCase(MapEditorCommands.REMOVE)) {
                continue;
            }

            String lowerCasedCommand = cmd.toLowerCase();

            switch (lowerCasedCommand) {
                case GameCommands.REINFORCE: {
                    // check if number of armies are in number format
                    if (Parser.checkValidInputNumber(params[1])) {
                        regularCommands.add(params[j]);
                        regularCommands.add(params[j + 1]);
                        return;
                    }
                }
                case GameCommands.FORTIFY: {
                    // Three parameters of FORTIFY command
                    if (params.length == 3) {
                        // check if number of armies are in number format
                        if (Parser.checkValidInputNumber(params[2])) {
                            regularCommands.add(params[j]);
                            regularCommands.add(params[j + 1]);
                            regularCommands.add(params[j + 2].replace("-", ""));
                            return;
                        }
                        // did not provide number
                        else {
                            throw new NumberFormatException();
                        }
                    } else if (params.length == 4) {
                        // check if number of armies are in number format
                        if (Parser.checkValidInputNumber(params[2])) {
                            regularCommands.add(params[j]);
                            regularCommands.add(params[j + 1]);
                            regularCommands.add(params[j + 2]);
                            regularCommands.add(params[j + 3]);
                            return;
                        }
                        // did not provide number
                        else {
                            throw new NumberFormatException();
                        }
                    }
                    // Player does not wish to fortify
                    else if (params[0].replace("-", "").equalsIgnoreCase(GameCommands.NONE)) {
                        regularCommands.add(params[j].toLowerCase());
                        return;
                    }
                    // Unknown command provided throw invalid exception
                    else {
                        throw new Exception("Invalid Input");
                    }
                }
                case GameCommands.EXCHANGECARDS: {
                    if ((params.length % 3) == 0) {
                        for (String param : params) {
                            if (Parser.checkValidInputNumber(param)) {
                                regularCommands.add(param);
                            } else {
                                throw new NumberFormatException();
                            }
                        }
                        return;
                    } else if ((params.length % 3) == 1) {
                        int size = params.length - 1;
                        for (int index = 0; index < size; index++) {
                            if (Parser.checkValidInputNumber(params[index])) {
                                regularCommands.add(params[index]);
                            } else {
                                throw new NumberFormatException();
                            }
                        }
                        regularCommands.add(params[size]);
                        return;
                    } else if (params[j].replace("-", "").equalsIgnoreCase(GameCommands.NONE)) {
                        regularCommands.add(params[j].toLowerCase());
                        return;
                    }
                }
                case GameCommands.ATTACK: {
                    if (params.length == 3) {
                        if (Parser.checkValidInputNumber(params[2])) {
                            regularCommands.add(params[j]);
                            regularCommands.add(params[j + 1]);
                            regularCommands.add(params[j + 2]);
                            return;
                        } else {
                            throw new NumberFormatException();
                        }
                    } else if (params.length == 2) {
                        if (paramsArray[1].replace("-", "").equalsIgnoreCase(GameCommands.ALLOUT)) {
                            regularCommands.add(params[j]);
                            regularCommands.add(params[j + 1]);
                            // regularCommands.add(paramsArray[1]);
                            return;
                        }
                    } else if (params.length == 4) {
                        if (Parser.checkValidInputNumber(params[2])) {
                            regularCommands.add(params[j]);
                            regularCommands.add(params[j + 1]);
                            regularCommands.add(params[j + 2]);
                            return;
                        } else {
                            throw new NumberFormatException();
                        }
                    } else if (params.length == 1) {
                        regularCommands.add(params[j].toLowerCase());
                        return;
                    } else {
                        throw new Exception();
                    }
                }
                case GameCommands.DEFEND: {
                    if (Parser.checkValidInputNumber(params[j])) {
                        regularCommands.add(params[j]);
                        return;
                    } else {
                        throw new NumberFormatException();
                    }
                }
                case GameCommands.ATTACKMOVE: {
                    if (Parser.checkValidInputNumber(params[j])) {
                        regularCommands.add(params[j].toLowerCase());
                        return;
                    } else {
                        throw new NumberFormatException();
                    }
                }
                default: {
                    regularCommands.add(params[j]);
                }
            }
        }
    }
}
