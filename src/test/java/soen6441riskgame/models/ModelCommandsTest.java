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
    public void AttackTest(String attackCommand, boolean isValid) {
        ModelCommands cmds = new ModelCommands(attackCommand);
        boolean actualIsValid = false;

        if ((cmds.cmd != "") || (cmds.cmd != null)) {
            if (cmds.regularCommands.size() > 0) {
                actualIsValid = true;
            }
        }

        assertEquals(isValid, actualIsValid);
    }
}