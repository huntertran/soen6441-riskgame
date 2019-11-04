package soen6441riskgame.models;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.*;
import org.junit.jupiter.api.Test;



public class AttackPhaseTest {
    
    private String validAtk1 = "attack c1 c2 6";
    private String validAtk2 = "attack countrynamefrom countynameto 2 –allout";
    private String validAtk3 = "attack –noattack";
    private String validAtk4 = "attackmove 2";

    private String inValidAtk1 = "attack c1 c2 asd";
    private String inValidAtk2 = "attack countrynamefrom countynameto numdice –allout";
    private String inValidAtk3 = "attackmove asd";

    @Test
    public void Attack1Test() {
        ModelCommands cmds = new ModelCommands(validAtk1);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void Attack2Test() {
        ModelCommands cmds = new ModelCommands(validAtk2);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void Attack3Test() {
        ModelCommands cmds = new ModelCommands(validAtk3);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void Attack4Test() {
        ModelCommands cmds = new ModelCommands(validAtk4);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void Attack5Test() {
        ModelCommands cmds = new ModelCommands(inValidAtk1);
        boolean flag = false;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = true;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void Attack6Test() {
        ModelCommands cmds = new ModelCommands(inValidAtk2);
        boolean flag = false;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = true;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void Attack7Test() {
        ModelCommands cmds = new ModelCommands(inValidAtk3);
        boolean flag = false;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = true;
            }
        }
        assertFalse(flag);
    }

}