package soen6441riskgame;

import org.junit.runners.Suite.SuiteClasses;
import soen6441riskgame.controllers.GameControllerTest;
import soen6441riskgame.controllers.MapControllerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@SuiteClasses({ GameControllerTest.class, MapControllerTest.class })

public class AllTest {
}
