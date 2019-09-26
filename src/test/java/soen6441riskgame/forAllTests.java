package test.java.soen6441riskgame;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This class is for running all the test files at ones.
 * 
 * 
 * 
 */

@RunWith(Suite.class)
@SuiteClasses({ FileTest.class, Testing.class, PlayerTest.class, GraphTest.class, ContestsTest.class })
public class forAllTests {

}
