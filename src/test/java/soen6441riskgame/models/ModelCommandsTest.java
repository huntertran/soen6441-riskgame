package soen6441riskgame.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
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
    @CsvFileSource(resources = "attack_commands.csv", numLinesToSkip = 1)
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
    @CsvFileSource(resources = "fortify_commands.csv", numLinesToSkip = 1)
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