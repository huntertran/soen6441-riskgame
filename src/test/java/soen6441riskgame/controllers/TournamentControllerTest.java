package soen6441riskgame.controllers;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TournamentControllerTest {
    TournamentController controller;

    @BeforeEach
    public void before() {
        controller = new TournamentController();
    }

    @ParameterizedTest
    @ValueSource(strings = {
                             "-M smallmap -P Aggressive,Benevolent,Random, -G 1 -D 40",
                             "-M smallmap -P Benevolent,Random -G 1 -D 50"
    })
    public void enterTournamentTest(String command) {
        // setup
        List<String> params = Arrays.asList(command.split(" "));

        controller.enterTournament(params);
    }
}