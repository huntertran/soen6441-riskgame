package soen6441riskgame.models;

import java.util.ArrayList;
import java.util.List;

import soen6441riskgame.commands.MapEditorCommands;
import soen6441riskgame.commands.GameCommands;
import soen6441riskgame.utils.Parser;

public class ModelCommands {
    public String cmd;
    public List<String> regularCommands = new ArrayList<String>();
    public List<ModelCommandsPair> subRoutine = new ArrayList<ModelCommandsPair>();

    public ModelCommands(String new_args) {

        if((new_args != null) && (new_args != "")) {
            String[] temp_args = new_args.split(" ", 2);
            this.cmd = temp_args[0].toLowerCase();
            
            if(temp_args.length > 1) {
                String[] paramsArray = temp_args[1].split("-");

                for(int i = 0; i < paramsArray.length; i++) {
                    try {
                        String[] params = paramsArray[i].split(" ");

                        for(int j = 0; j < params.length; j++) {
                            // check for null or empty value after split
                            if((params[j] != null) && (!params[j].equals(""))) {
                                
                                // check specific validation criteria as per command
                                if(params[j].equalsIgnoreCase(MapEditorCommands.ADD)) {
                                    if(cmd.equalsIgnoreCase(MapEditorCommands.EDITCONTINENT)) {
                                        Parser p = new Parser();
                                        if(p.checkValidInputNumber(params[j+2])) {
                                            subRoutine.add(new ModelCommandsPair(params[j].toLowerCase(), params[j+1].toLowerCase(), params[j+2].toLowerCase()));
                                            break;
                                        }
                                    }
                                    else if(cmd.equalsIgnoreCase(GameCommands.GAMEPLAYER)) {
                                        subRoutine.add(new ModelCommandsPair(params[j].toLowerCase(), params[j+1].toLowerCase()));
                                        break;
                                    }
                                    else {
                                        subRoutine.add(new ModelCommandsPair(params[j].toLowerCase(), params[j+1].toLowerCase(), params[j+2].toLowerCase()));
                                        break;
                                    }
                                }
                                else if(params[j].equalsIgnoreCase(MapEditorCommands.REMOVE)) {
                                    subRoutine.add(new ModelCommandsPair(params[j].toLowerCase(), params[j+1].toLowerCase()));
                                    break;
                                }
                                else if(cmd.equalsIgnoreCase(GameCommands.REINFORCE)) {
                                    Parser p = new Parser();
                                    if(p.checkValidInputNumber(params[1])) {
                                        regularCommands.add(params[j].toLowerCase());
                                        break;
                                    }
                                }
                                else if(cmd.equalsIgnoreCase(GameCommands.FORTIFY)) {
                                    Parser p = new Parser();
                                    if(p.checkValidInputNumber(params[2])) {
                                        regularCommands.add(params[j].toLowerCase());
                                        break;
                                    }
                                }
                                else {
                                    regularCommands.add(params[j].toLowerCase());
                                    break;
                                }
                            }
                        }
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Invalid value detected.");
                        continue;
                    }
                    catch(Exception e) {
                        System.out.println("Invalid command detected.");
                        continue;
                    }
                }
            }
        }
    }
} 