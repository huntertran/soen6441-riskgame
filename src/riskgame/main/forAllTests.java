package com.project.soen6441riskgame;

import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runner.RunWith;


/**
 * This class is for running all the test files at ones.
 * 
 * 
 * 
 */


@RunWith(Suite.class)
@SuiteClasses({ RiskGameFileTest.class, RiskGameTesting.class, RiskGamePlayerTest.class, RiskGameGraphTest.class, RiskGameContestsTest.class})
public class forAllTests {

}
