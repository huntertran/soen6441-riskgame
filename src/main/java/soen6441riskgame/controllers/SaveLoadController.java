package soen6441riskgame.controllers;

import java.io.FileWriter;
import java.io.IOException;

import soen6441riskgame.models.serializers.SerializableGame;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;

public class SaveLoadController {
    public boolean saveGame(String saveGameFilePath) {
        boolean isSaved = false;

        SerializableGame serializableGame = new SerializableGame.Builder().setCountries(GameBoard.getInstance()
                                                                                                 .getGameBoardMap()
                                                                                                 .getCountries())
                                                                          .setContinents(GameBoard.getInstance()
                                                                                                  .getGameBoardMap()
                                                                                                  .getContinents())
                                                                          .setBorders(GameBoard.getInstance()
                                                                                               .getGameBoardMap()
                                                                                               .getBorders())
                                                                          .setPlayers(GameBoard.getInstance()
                                                                                               .getGameBoardPlayer()
                                                                                               .getPlayers())
                                                                          .setCards(GameBoard.getInstance()
                                                                                             .getCardsForSaveLoad())
                                                                          .build();

        String jsonToSave = serializableGame.serialize();

        try (FileWriter writer = new FileWriter(saveGameFilePath)) {
            writer.write(jsonToSave);
            isSaved = true;
        } catch (Exception e) {
            ConsolePrinter.printFormat("Cannot create file to save");
        }

        return isSaved;
    }

    public boolean loadGame(String savedGameFilePath) throws IOException {
        boolean isLoaded = false;

        return isLoaded;
    }
}