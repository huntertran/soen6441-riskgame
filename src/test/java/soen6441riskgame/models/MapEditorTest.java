package soen6441riskgame.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.*;
import org.junit.jupiter.api.Test;



public class MapEditorTest {

    private String valid1 = "editcontinent -add continentName 5 -remove continentName2";
    private String valid2 = "editcountry -add countryName continentName";
    private String valid3 = "editcountry -add countryName1 continentName1 -add countryName2 continentName2 -add countryName3 continentName3";
    private String valid4 = "editcountry -remove countryname";
    private String valid5 = "editcountry -remove countryname1 -remove countryname2 -remove countryname3";
    private String valid6 = "editneighbor -add countryname neighborcountryname";
    private String valid7 = "editneighbor -add countryName1 neighborcountryName1 -add countryName2 neighborcountryName2 -add countryName3 neighborcountryName3";
    private String valid8 = "editneighbor -remove countryname neighborcountryname";
    private String valid9 = "editneighbor -add countryname1 neighborcountryname1 -remove countryname2 neighborcountryname2";
    private String valid10 = "showmap";
    private String valid11 = "savemap fileName";
    private String valid12 = "editmap fileName";
    private String valid13 = "validatemap";

    private String invalid1 = "editcontinent -add continentname value";

    @Test
    public void MapEditor1CapTest() {
        ModelCommands cmds = new ModelCommands(valid1);
        boolean flag = true;

        String[] actual = {"continentName", "continentName2"};

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.subRoutine.size() > 0){
                assertEquals(cmds.subRoutine.size(), 2);
                int i = 0;
                for(ModelCommandsPair sub : cmds.subRoutine)
                {
                    assertEquals(sub.value1, actual[i]);
                    i++;
                }
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void MapEditor2CapTest() {
        ModelCommands cmds = new ModelCommands(valid3);
        boolean flag = true;

        String[] actual = {"countryName1", "countryName2", "countryName3"};
        String[] actualCont = {"continentName1", "continentName2", "continentName3"};

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.subRoutine.size() > 0){
                assertEquals(cmds.subRoutine.size(), 3);
                int i = 0;
                for(ModelCommandsPair sub : cmds.subRoutine)
                {
                    assertEquals(sub.value1, actual[i]);
                    assertEquals(sub.value2, actualCont[i]);
                    i++;
                }
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void MapEditor7CapTest() {
        ModelCommands cmds = new ModelCommands(valid7);
        boolean flag = true;
        
        String[] actual = {"countryName1", "countryName2", "countryName3"};
        String[] actualCont = {"neighborcountryName1", "neighborcountryName2", "neighborcountryName3"};

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.subRoutine.size() > 0){
                assertEquals(cmds.subRoutine.size(), 3);
                int i = 0;
                for(ModelCommandsPair sub : cmds.subRoutine)
                {
                    assertEquals(sub.value1, actual[i]);
                    assertEquals(sub.value2, actualCont[i]);
                    i++;
                }
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void MapEditor1Test() {
        ModelCommands cmds = new ModelCommands(valid1);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.subRoutine.size() > 0){
                assertEquals(cmds.subRoutine.size(), 2);
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void MapEditor2Test() {
        ModelCommands cmds = new ModelCommands(valid2);
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
    public void MapEditor3Test() {
        ModelCommands cmds = new ModelCommands(valid3);
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
    public void MapEditor4Test() {
        ModelCommands cmds = new ModelCommands(valid4);
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
    public void MapEditor5Test() {
        ModelCommands cmds = new ModelCommands(valid5);
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
    public void MapEditor6Test() {
        ModelCommands cmds = new ModelCommands(valid6);
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
    public void MapEditor7Test() {
        ModelCommands cmds = new ModelCommands(valid7);
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
    public void MapEditor8Test() {
        ModelCommands cmds = new ModelCommands(valid8);
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
    public void MapEditor9Test() {
        ModelCommands cmds = new ModelCommands(valid9);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.subRoutine.size() > 0){
                assertEquals(cmds.subRoutine.size(), 2);
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void MapEditor10Test() {
        ModelCommands cmds = new ModelCommands(valid10);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            flag = false;
        }
        assertFalse(flag);
    }

    @Test
    public void MapEditor11Test() {
        ModelCommands cmds = new ModelCommands(valid11);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void MapEditor12Test() {
        ModelCommands cmds = new ModelCommands(valid12);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.regularCommands.size() > 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }

    @Test
    public void MapEditor13Test() {
        ModelCommands cmds = new ModelCommands(valid13);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            flag = false;
        }
        assertFalse(flag);
    }

    @Test
    public void MapEditor14Test() {
        ModelCommands cmds = new ModelCommands(invalid1);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if(cmds.subRoutine.size() == 0){
                flag = false;
            }
        }
        assertFalse(flag);
    }
}