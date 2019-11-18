package soen6441riskgame.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.StringUtils;

import soen6441riskgame.helpers.StringArrayConverter;

/**
 * Tests the functionality of ModelCommands
 */
public class ModelCommandsTest {

    /**
     * Test for the attack commands
     *
     * @param attackCommand the command to test
     * @param isValid       the expected result
     */
    @ParameterizedTest
    @CsvSource({
                 "attack c1 c2 6, True", // tests the validity of the attack command when attack is launched from one
                                         // country to other using number on the dice.
                 "attack countryNameFrom countyNameTo 2 –allout, True", // it tests the validity of the attack command
                                                                        // when an allout attack is launched from one
                                                                        // country to other
                 "attack –noattack, True", // tests the validity of the attack -noattack command.
                 "attackmove 2, True", // tests the validity of attackmove command
                 "attack c1 c2 asd, False", // it tests the validity of attack command from one country to other with a
                                            // random string rather than number on the dice.
                 "attack countryNameFrom countyNameTo numdice –allout, False", // it tests the validity of attack
                                                                               // command when allout attack is launched
                                                                               // from one country to other with a
                                                                               // random string rather than number on
                                                                               // the dice.
                 "attackmove asd, False", // tests the validity of attack command attackmove with a string rather than
                                          // an
                                          // integer.
                 "reinforce countryName 1, True", // tests validity of the reinforcement command.
                 "reinforce countryName 1, True", // tests validity of the reinforcement command.
                 "exchangecards 1 2 3 -none, True", // tests validity of the reinforcement command.
                 "exchangecards -none, True", // tests validity of the reinforcement command.
                 "reinforce countryName num, False"// invalid command
    })
    public void CommonCommandTest(String attackCommand, boolean isValid) {
        ModelCommands cmds = new ModelCommands(attackCommand);
        boolean actualIsValid = false;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if (cmds.regularCommands.size() > 0) {
                actualIsValid = true;
            }
        }

        assertEquals(isValid, actualIsValid);
    }

    /**
     * Tests the Fortification Phase Commands.
     *
     * @param fortifyCommand                      the command to test
     * @param expectedParsedRegularCommandsNumber number of parsed args
     */
    @ParameterizedTest
    @CsvSource({
                 "fortify fromcountry toCountry 2 –none, 4", // test the validity of fortify command,
                 "fortify fromcountry toCountry 2, 3", // test the validity of fortify command when player choses not to
                                                       // fortify during fortification phase.,
                 "fortify -none, 1", // test the validity of fortify command,,
                 "fortify fromcountry toCountry num, 0", // test the validity of fortify command,
                 "fortify fromcountry toCountry num -none, 1", // test the validity of fortify command,
                 "exchangecards num num num -none, 1"// test the validity of exchangecards
    })
    public void CommonCommandParseArgumentTest(String fortifyCommand, int expectedParsedRegularCommandsNumber) {
        ModelCommands cmds = new ModelCommands(fortifyCommand);

        int actualParsedRegularCommandsNumber = 0;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            actualParsedRegularCommandsNumber = cmds.regularCommands.size();
        }

        assertEquals(expectedParsedRegularCommandsNumber, actualParsedRegularCommandsNumber);
    }

    @ParameterizedTest
    @CsvSource({
                 "reinforce countryName 1, countryName"
    })
    public void ReinforceCommandWithCaseNameTest(String command, String expectedCountryName) {
        ModelCommands cmds = new ModelCommands(command);

        String actualCountryName = "";

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if (cmds.regularCommands.size() > 0) {
                actualCountryName = cmds.regularCommands.get(0);
            }
        }

        assertEquals(expectedCountryName, actualCountryName);
    }

    /**
     * test the country name with capitalized character were respected after parse
     *
     * @param fortifyCommand fortify command
     * @param expectedFrom   the from country name
     * @param expectedTo     the to country name
     */
    @ParameterizedTest
    @CsvSource({
                 "fortify fromCountry toCountry 2 –none, fromCountry, toCountry"
    })
    public void FortifyCommandWithCaseNameTest(String fortifyCommand, String expectedFrom, String expectedTo) {
        ModelCommands cmds = new ModelCommands(fortifyCommand);

        String actualFrom = "";
        String actualTo = "";

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if (cmds.regularCommands.size() > 0) {
                actualFrom = cmds.regularCommands.get(0);
                actualTo = cmds.regularCommands.get(1);
            }
        }

        assertEquals(expectedFrom, actualFrom);
        assertEquals(expectedTo, actualTo);
    }

    @ParameterizedTest
    @ValueSource(strings = {
                             "showmap",
                             "savemap fileName",
                             "validatemap"
    })
    public void GamePlayTest(String command) {
        ModelCommands cmds = new ModelCommands(command);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            flag = false;
        }

        assertFalse(flag);
    }

    @ParameterizedTest
    @CsvSource({
                 "editcontinent -add continentName 5 -remove continentName2, 2",
                 "editcountry -add countryName continentName, 1",
                 "editcountry -add countryName1 continentName1 -add countryName2 continentName2 -add countryName3 continentName3, 3",
                 "editcountry -remove countryName, 1",
                 "editcountry -remove countryName1 -remove countryName2 -remove countryName3, 3",
                 "editneighbor -add countryName neighborCountryName, 1",
                 "editneighbor -add countryName1 neighborCountryName1 -add countryName2 neighborCountryName2 -add countryName3 neighborCountryName3, 3",
                 "editneighbor -remove countryName neighborCountryName, 1",
                 "editneighbor -add countryName1 neighborCountryName1 -remove countryName2 neighborCountryName2, 2",
                 "editcontinent -add continentName value, 0"
    })
    public void MapEditorTest(String command, int expectedNumberOfSubRoutine) {
        ModelCommands cmds = new ModelCommands(command);

        int actualNumberOfParsedSubRoutine = 0;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            actualNumberOfParsedSubRoutine = cmds.subRoutine.size();
        }

        assertEquals(expectedNumberOfSubRoutine, actualNumberOfParsedSubRoutine);
    }

    @ParameterizedTest
    @CsvSource({
                 "editcontinent -add continentName 5 -remove continentName2, continentName;continentName2,5;",
                 "editcountry -add countryName1 continentName1 -add countryName2 continentName2 -add countryName3 continentName3, countryName1;countryName2;countryName3, continentName1;continentName2;continentName3",
                 "editneighbor -add countryName1 neighborCountryName1 -add countryName2 neighborCountryName2 -add countryName3 neighborCountryName3,countryName1;countryName2;countryName3,neighborCountryName1;neighborCountryName2;neighborCountryName3"
    })
    public void MapEditorWithCaseNameTest(String command,
                                          @ConvertWith(StringArrayConverter.class) String[] expectedValue1Names,
                                          @ConvertWith(StringArrayConverter.class) String[] expectedValue2Names) {
        ModelCommands cmds = new ModelCommands(command);
        boolean flag = true;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if (cmds.subRoutine.size() > 0) {
                int i = 0;
                for (ModelCommandsPair sub : cmds.subRoutine) {
                    assertEquals(sub.value1, expectedValue1Names[i]);

                    if (!StringUtils.isBlank(sub.value2)) {
                        assertEquals(sub.value2, expectedValue2Names[i]);
                    }

                    i++;
                }

                flag = false;
            }
        }
        assertFalse(flag);
    }
}