package soen6441riskgame.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.*;
import org.junit.jupiter.api.Test;



public class FortificationPhaseTest {
    
    private String valid1 = "fortify fromcountry tocountry 2 –none";
    private String valid2 = "fortify fromcountry tocountry 2";
    private String valid3 = "fortify -none";
    private String valid4 = "fortify fromCountry toCountry 2 –none";

    private String invalid1 = "fortify fromcountry tocountry num";
    private String invalid2 = "fortify fromcountry tocountry num -none";

    @Test
    public void Fortification1Test() {
        ModelCommands cmds = new ModelCommands(valid1);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void Fortification2CapTest() {
        ModelCommands cmds = new ModelCommands(valid4);
        boolean flag = true;
        String actual = "fromCountry";
        String actual2 = "toCountry";

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                assertEquals(cmds.regularCommands.get(0), actual);
                assertEquals(cmds.regularCommands.get(1), actual2);
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void Fortification3Test() {
        ModelCommands cmds = new ModelCommands(valid2);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void Fortification4Test() {
        ModelCommands cmds = new ModelCommands(valid3);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void Fortification5Test() {
        ModelCommands cmds = new ModelCommands(invalid1);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() == 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void Fortification6Test() {
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