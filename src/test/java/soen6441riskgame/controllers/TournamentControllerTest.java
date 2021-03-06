package soen6441riskgame.controllers;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * test tournament controller
 */
public class TournamentControllerTest {
    TournamentController controller;

    /**
     * create new tournament controller for each test
     */
    @BeforeEach
    public void before() {
        controller = new TournamentController();
    }

    /**
     * test tournament command with various arguments
     * 
     * @param command command to test
     */
    @ParameterizedTest
    @ValueSource(strings = {
                             "-M ./src/test/java/files/maps/domination/smallmap.map,./src/test/java/files/maps/domination/newsmallmap.map -P Aggressive,Benevolent,Random -G 4 -D 50",
                             "-M ./src/test/java/files/maps/domination/smallmap.map -P Aggressive,Benevolent,Random, -G 1 -D 40",
                             "-M ./src/test/java/files/maps/domination/smallmap.map -P Benevolent,Random -G 1 -D 50"
    })
    public void enterTournamentTest(String command) {
        // setup
        List<String> params = Arrays.asList(command.split(" "));

        controller.enterTournament(params);
    }
}