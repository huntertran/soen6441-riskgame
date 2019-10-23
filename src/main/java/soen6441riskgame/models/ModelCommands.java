package soen6441riskgame.models;

import java.util.ArrayList;
import java.util.List;

import soen6441riskgame.commands.MapEditorCommands;

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
                            if((params[j] != null) && (!params[j].equals(""))) {
                                if(params[j].toLowerCase().toString().equals(MapEditorCommands.ADD)) {
                                    subRoutine.add(new ModelCommandsPair(params[j].toLowerCase(), params[j+1].toLowerCase(), params[j+2].toLowerCase()));
                                    break;
                                }
                                else if(params[j].toLowerCase().toString().equals(MapEditorCommands.REMOVE)) {
                                    subRoutine.add(new ModelCommandsPair(params[j].toLowerCase(), params[j+1].toLowerCase()));
                                    break;
                                }
                                else {
                                    regularCommands.add(params[j].toLowerCase());
                                    break;
                                }
                            }
                        }
                    } 
                    catch(Exception e) {
                        System.out.println("Invalid command found");
                        continue;
                    }
                }
            }
        }
    }
} 