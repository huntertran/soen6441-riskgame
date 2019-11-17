package soen6441riskgame.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
                 "attackmove asd, False" // tests the validity of attack command attackmove with a string rather than an
                                         // integer.
    })
    public void AttackCommandTest(String attackCommand, boolean isValid) {
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
                 "fortify fromcountry toCountry num -none, 1",// test the validity of fortify command,
    })
    public void FortifyCommandTest(String fortifyCommand, int expectedParsedRegularCommandsNumber) {
        ModelCommands cmds = new ModelCommands(fortifyCommand);

        int actualParsedRegularCommandsNumber = 0;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            actualParsedRegularCommandsNumber = cmds.regularCommands.size();
        }

        assertEquals(expectedParsedRegularCommandsNumber, actualParsedRegularCommandsNumber);
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
}