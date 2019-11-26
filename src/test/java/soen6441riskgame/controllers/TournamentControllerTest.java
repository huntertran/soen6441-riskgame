package soen6441riskgame.controllers;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TournamentControllerTest {
    @ParameterizedTest
    @ValueSource(strings = {
                             "-M smallmap,newsmallmap -P Aggressive,Benevolent,Random, -G 2 -D 40"
    })
    public void enterTournamentTest(String command) {
        // setup
        List<String> params = Arrays.asList(command.split(" "));

        TournamentController controller = new TournamentController();
        controller.enterTournament(params);
    }
}