package soen6441riskgame.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;



public class StartupPhaseTest {
    private String validStart1 = "loadmap fileName";
    private String validStart2 = "gameplayer -add playerName";
    private String validStart3 = "gameplayer -remove playerName";
    private String validStart4 = "gameplayer -add playername1 -add playername2 -add playername3 -add playername4";
    private String validStart5 = "gameplayer -remove playername1 -remove playername2 -remove playername3";
    private String validStart6 = "gameplayer -add playername1 -add playername2 -add playername3 -add playername4 -remove playername1 -remove playername2 -remove playername3";
    private String validStart7 = "populatecountries";

    @Test
    public void StartupPhase1Test() {
        ModelCommands cmds = new ModelCommands(validStart1);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void StartupPhase2Test() {
        ModelCommands cmds = new ModelCommands(validStart2);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.subRoutine.size() > 0){
                assertEquals(cmds.subRoutine.size(), 1);
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void StartupPhase3Test() {
        ModelCommands cmds = new ModelCommands(validStart3);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.subRoutine.size() > 0){
                assertEquals(cmds.subRoutine.size(), 1);
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void StartupPhase4Test() {
        ModelCommands cmds = new ModelCommands(validStart4);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.subRoutine.size() > 0){
                assertEquals(cmds.subRoutine.size(), 4);
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void StartupPhase5Test() {
        ModelCommands cmds = new ModelCommands(validStart5);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.subRoutine.size() > 0){
                assertEquals(cmds.subRoutine.size(), 3);
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void StartupPhase6Test() {
        ModelCommands cmds = new ModelCommands(validStart6);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.subRoutine.size() > 0){
                assertEquals(cmds.subRoutine.size(), 7);
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void StartupPhase7Test() {
        ModelCommands cmds = new ModelCommands(validStart7);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            flag = false;
        }
        assertFalse(flag);
    }
}