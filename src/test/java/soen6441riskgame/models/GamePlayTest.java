package soen6441riskgame.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.*;
import org.junit.jupiter.api.Test;



public class GamePlayTest {

    private String valid1 = "showmap";

    @Test
    public void GamePlay1Test() {
        ModelCommands cmds = new ModelCommands(valid1);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            flag = false;
        }
        assertFalse(flag);
    }

}