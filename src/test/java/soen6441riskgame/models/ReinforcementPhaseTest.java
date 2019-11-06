package soen6441riskgame.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

/**
 * Tests the Reinforcement Phase Commands.
 */

public class ReinforcementPhaseTest {
    
    private String valid1 = "reinforce countryname 1";
    private String valid2 = "reinforce countryName 1";
    private String valid3 = "exchangecards 1 2 3 -none";
    private String valid4 = "exchangecards -none";

    private String invalid1 = "reinforce countryname num";
    private String invalid2 = "exchangecards num num num -none";

    /**
     * it tests validity of the reinforcement command.
     */
    @Test
    public void Reinforcement1Test() {
        ModelCommands cmds = new ModelCommands(valid1);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    /**
     * it tests validity of the reinforcement command.
     */
    @Test
    public void Reinforcement2CapTest() {
        ModelCommands cmds = new ModelCommands(valid2);
        boolean flag = true;
        String actual = "countryName";

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                assertEquals(cmds.regularCommands.get(0), actual);
                flag = false;
            }
        }
        assertFalse(flag);
    }

    /**
     * it tests validity of the reinforcement command.
     */
    @Test
    public void Reinforcement3Test() {
        ModelCommands cmds = new ModelCommands(valid3);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    /**
     * it tests validity of the reinforcement command.
     */
    @Test
    public void Reinforcement4Test() {
        ModelCommands cmds = new ModelCommands(valid4);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    /**
     * it tests validity of the reinforcement command.
     */
    @Test
    public void Reinforcement5Test() {
        ModelCommands cmds = new ModelCommands(invalid1);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() == 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    /**
     * it tests validity of the reinforcement command.
     */
    @Test
    public void Reinforcement6Test() {
        ModelCommands cmds = new ModelCommands(invalid2);
        boolean flag = false;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() == 0){
                flag = true;
            }
        }
        assertFalse(flag);
    }
}