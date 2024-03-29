package soen6441riskgame.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import soen6441riskgame.App;
import soen6441riskgame.controllers.GameController;
import soen6441riskgame.controllers.MapController;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.helpers.GamePlayActionsTestHelper;
import soen6441riskgame.helpers.IntArrayConverter;
import soen6441riskgame.models.commands.GameCommands;
import soen6441riskgame.singleton.GameBoard;

/**
 * Tests the Player Model methods.
 */
public class PlayerTest {
    MapController mapController;
    GameController gameController;

    /**
     * the before method is executed before each test case to setup the context.
     */
    @BeforeEach
    public void before() throws IOException {

        GameBoard testingInstanceGameMap = new GameBoard();
        GameBoard.setTestingInstance(testingInstanceGameMap);

        String filePath = "./src/test/java/files/maps/domination/RiskEurope.map";
        mapController = new MapController();
        GameBoard.getInstance().reset();
        mapController.loadMap(filePath);

        gameController = new GameController();
    }

    /**
     * the after method is executed after each test case to reset the map.
     */
    @AfterEach
    public void after() {
        GameBoard.getInstance().reset();
    }

    /**
     * it tests calculateReinforcementArmies method and checks whether number of reinforcement armies
     * for a player in his reinforcement phase are correct or not.
     */
    @Test
    public void calculateReinforcementArmiesTest() {
        // setup
        GamePlayActionsTestHelper.addPlayersToGame();
        App.jumpToCommand(new ModelCommands(GameCommands.POPULATECOUNTRIES));
        App.jumpToCommand(new ModelCommands(GameCommands.PLACEALL));
        Player player = gameController.getCurrentPlayer();

        // action
        // before enter reinforcement
        player.calculateReinforcementArmies();
        int zeroArmiesBeforeReinforce = player.getUnplacedArmies();

        // set reinforcement phase
        player.setCurrentPhase(GamePhase.REINFORCEMENT);
        player.calculateReinforcementArmies();
        int numberOfInitArmiesInReinforce = player.getUnplacedArmies();

        assertEquals(0, zeroArmiesBeforeReinforce);
        assertTrue(numberOfInitArmiesInReinforce >= 3);
    }

    /**
     * test building valid card sets
     * 
     * @param cardIndexes       indexes of the card
     * @param expectedValidSets expected number of sets
     */
    @ParameterizedTest
    @CsvSource({
                 "0;1;2;3;4;5;6;7;8;9, 3",
                 "0;1;2;3;4;5;6, 2",
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

        ArrayList<CardSet> cardSets = player.buildValidCardSets();

        assertTrue(expectedValidSets >= cardSets.size());
    }

    /**
     * test changing phase
     * 
     * @param oldPhase                 old phase
     * @param newPhase                 new phase
     * @param expectedPhase            expected phase after changed
     * @param isHoldingMaxNumberOfCard is player holding cards and must exchanged
     */
    @ParameterizedTest
    @CsvSource({
                 "WAITING_TO_TURN,LOST,LOST,False",
                 "REINFORCEMENT,LOST,LOST,False",
                 "ATTACK,LOST,LOST,False",
                 "FORTIFICATION,LOST,LOST,False",
                 "WAITING_TO_TURN,REINFORCEMENT,REINFORCEMENT,False",
                 "REINFORCEMENT,ATTACK,ATTACK,False",
                 "ATTACK,FORTIFICATION,FORTIFICATION,False",
                 "ATTACK,END_OF_GAME,END_OF_GAME,False",
                 "FORTIFICATION,WAITING_TO_TURN,WAITING_TO_TURN,False",
                 "REINFORCEMENT,WAITING_TO_TURN,REINFORCEMENT,False",
                 "ATTACK,REINFORCEMENT,ATTACK,False",
                 "REINFORCEMENT,ATTACK,REINFORCEMENT,True"
    })
    public void setCurrentPhaseTest(GamePhase oldPhase,
                                    GamePhase newPhase,
                                    GamePhase expectedPhase,
                                    Boolean isHoldingMaxNumberOfCard) {
        // setup
        String playerJson = "{"
                            + "\"name\": \"p1\","
                            + "\"unplacedArmies\": 0,"
                            + "\"isPlaying\": false,"
                            + "\"nextPlayerName\": \"p2\","
                            + "\"previousPlayerName\": \"p2\","
                            + "\"currentPhase\": \"" + oldPhase.toString() + "\","
                            + "\"currentPhaseActions\": [],"
                            + "\"isPlayerBeAwardCard\": false"
                            + "}";
        Gson gson = new Gson();
        Player player = new Player(gson.fromJson(playerJson, Player.class));
        player.reconstruct();

        if (isHoldingMaxNumberOfCard) {
            int index = 0;
            while (index < 5) {
                player.getHoldingCards().add(GameBoard.getInstance().getRandomAvailableCard());
                index++;
            }
        }

        // action
        player.setCurrentPhase(newPhase);

        // assert
        assertEquals(expectedPhase, player.getCurrentPhase());
    }
}
