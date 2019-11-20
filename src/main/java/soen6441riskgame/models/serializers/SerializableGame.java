package soen6441riskgame.models.serializers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;

import soen6441riskgame.models.Card;
import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;

public class SerializableGame {

    @Expose
    private List<Country> countries;
    @Expose
    private List<Continent> continents;
    @Expose
    private List<Player> players;
    @Expose
    private int[][] borders;
    @Expose
    private Card[] cards;

    public String serialize() {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                                     .serializeNulls()
                                     .excludeFieldsWithoutExposeAnnotation()
                                     .registerTypeAdapter(NameOnlySerializable.class, new NameOnlyJsonAdapter())
                                     .create();

        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public void deserialize(JsonReader reader) {
        GameBoard.getInstance().reset();
        Gson gson = new GsonBuilder().registerTypeAdapter(NameOnlySerializable.class, new NameOnlyJsonAdapter())
                                     .create();

        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

        continents = new ArrayList<Continent>(Arrays.asList(gson.fromJson(jsonObject.get("continents"),
                                                                          Continent[].class)));

        for (Continent continent : continents) {
            GameBoard.getInstance().getGameBoardMap().getContinents().add(continent);
        }

        ConsolePrinter.printFormat(jsonObject.toString());
    }

    public static class Builder {
        private List<Country> countries;
        private List<Continent> continents;
        private List<Player> players;
        private int[][] borders;
        private Card[] cards;

        public Builder setContinents(List<Continent> continents) {
            this.continents = continents;
            return this;
        }

        public Builder setCountries(List<Country> countries) {
            this.countries = countries;
            return this;
        }

        public Builder setPlayers(List<Player> players) {
            this.players = players;
            return this;
        }

        public Builder setBorders(int[][] borders) {
            this.borders = borders;
            return this;
        }

        public Builder setCards(Card[] cards) {
            this.cards = cards;
            return this;
        }

        public SerializableGame build() {
            SerializableGame serializableGame = new SerializableGame();
            serializableGame.continents = this.continents;
            serializableGame.countries = this.countries;
            serializableGame.players = this.players;
            serializableGame.borders = this.borders;
            serializableGame.cards = this.cards;
            return serializableGame;
        }
    }
}