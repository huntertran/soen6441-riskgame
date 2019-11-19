package soen6441riskgame.models.serializers;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.NameOnlySerializable;
import soen6441riskgame.models.Player;

public class SerializableGame {

    @Expose
    private ArrayList<Country> countries;
    @Expose
    private ArrayList<Continent> continents;
    @Expose
    private ArrayList<Player> players;
    @Expose
    private int[][] borders;

    public String serialize() {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                                     .serializeNulls()
                                     .excludeFieldsWithoutExposeAnnotation()
                                     .registerTypeAdapter(NameOnlySerializable.class, new NameOnlyJsonAdapter())
                                     .create();

        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static class Builder {
        private ArrayList<Country> countries;
        private ArrayList<Continent> continents;
        private ArrayList<Player> players;
        private int[][] borders;

        public Builder setContinents(ArrayList<Continent> continents) {
            this.continents = continents;
            return this;
        }

        public Builder setCountries(ArrayList<Country> countries) {
            this.countries = countries;
            return this;
        }

        public Builder setPlayers(ArrayList<Player> players) {
            this.players = players;
            return this;
        }

        public Builder setBorders(int[][] borders) {
            this.borders = borders;
            return this;
        }

        public SerializableGame build() {
            SerializableGame serializableGame = new SerializableGame();
            serializableGame.continents = this.continents;
            serializableGame.countries = this.countries;
            serializableGame.players = this.players;
            serializableGame.borders = this.borders;
            return serializableGame;
        }
    }
}