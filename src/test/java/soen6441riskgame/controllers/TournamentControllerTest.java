package soen6441riskgame.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import soen6441riskgame.helpers.IntArrayConverter;
import soen6441riskgame.models.Card;
import soen6441riskgame.models.CardSet;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameBoard;

public class TournamentControllerTest {
    TournamentController controller;

    @BeforeEach
    public void before() {
        controller = new TournamentController();
    }

    @ParameterizedTest
    @ValueSource(strings = {
                             "-M smallmap -P Aggressive,Benevolent,Random, -G 1 -D 40"
    })
    public void enterTournamentTest(String command) {
        // setup
        List<String> params = Arrays.asList(command.split(" "));

        controller.enterTournament(params);
    }

    @ParameterizedTest
    @CsvSource({
                 "0;1;2;3;4;5;6;7;8;9, 2",
                 "0;1;2;3;4;5;6, 1",
                 "0;5;6, 0"
    })
    public void buildValidCardSetsTest(@ConvertWith(IntArrayConverter.class) int[] cardIndexes,
                                       int expectedValidSets) {
        Player player = new Player("test");

        // add cards to player
        for (int index : cardIndexes) {
            Card newCard = GameBoard.getInstance()
                                    .getCardsForSave()[index];
            newCard.setHoldingPlayer(player);
            player.getHoldingCards().add(newCard);
        }

        ArrayList<CardSet> cardSets = controller.buildValidCardSets(player);

        assertEquals(expectedValidSets, cardSets.size());
    }
}