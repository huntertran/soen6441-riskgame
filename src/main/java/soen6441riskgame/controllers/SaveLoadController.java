package soen6441riskgame.controllers;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import soen6441riskgame.models.Card;
import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.models.serializers.SerializableGame;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.singleton.GameBoardPlaying;
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
                                                                                             .getCardsForSave())
                                                                          .setGameBoardPlaying(GameBoard.getInstance()
                                                                                                        .getGameBoardPlaying())
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

    public void deserialize(JsonReader reader) {
        GameBoard.getInstance().reset();
        Gson gson = new GsonBuilder().create();

        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

        List<Continent> continents = Arrays.asList(gson.fromJson(jsonObject.get("continents"), Continent[].class));
        List<Country> countries = Arrays.asList(gson.fromJson(jsonObject.get("countries"), Country[].class));
        List<Player> players = Arrays.asList(gson.fromJson(jsonObject.get("players"), Player[].class));
        Card[] cards = gson.fromJson(jsonObject.get("cards"), Card[].class);
        int[][] borders = gson.fromJson(jsonObject.get("borders"), int[][].class);
        GameBoardPlaying gameBoardPlaying = gson.fromJson(jsonObject.get("gameBoardPlaying"), GameBoardPlaying.class);

        SerializableGame.Builder builder = new SerializableGame.Builder()
                                                                         .setContinents(continents)
                                                                         .setCountries(countries)
                                                                         .setPlayers(players)
                                                                         .setBorders(borders)
                                                                         .setCards(cards)
                                                                         .setGameBoardPlaying(gameBoardPlaying);
        builder.reconstructGame();
    }

    public boolean loadGame(String savedGameFilePath) {
        boolean isLoaded = false;

        try (JsonReader reader = new JsonReader(new FileReader(savedGameFilePath))) {
            ConsolePrinter.printFormat("Reading from saved game");
            deserialize(reader);
            isLoaded = true;
        } catch (Exception e) {
            ConsolePrinter.printFormat("Error reading saved game: %s", e.getMessage());
        }

        return isLoaded;
    }
}