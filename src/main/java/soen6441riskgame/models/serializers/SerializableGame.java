package soen6441riskgame.models.serializers;

import java.util.Comparator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import soen6441riskgame.models.Card;
import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameBoard;

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
                                     .create();

        String jsonString = gson.toJson(this);
        return jsonString;
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

        public void reconstructGame() {
            GameBoard.getInstance().reset();

            for (Continent continent : continents) {
                GameBoard.getInstance()
                         .getGameBoardMap()
                         .getContinents()
                         .add(continent);
            }

            for (Player player : players) {
                GameBoard.getInstance()
                         .getGameBoardPlayer()
                         .getPlayers()
                         .add(player);
            }

            for (Player player : players) {
                player.linkNextAndPrevious(GameBoard.getInstance()
                                                    .getGameBoardPlayer()
                                                    .getPlayers());
                player.reconstruct();
            }

            // sort countries
            countries.sort(Comparator.comparingInt(Country::getSerializedOrder));

            for (Country country : countries) {
                Country linkedCountry = new Country(country,
                                                    GameBoard.getInstance()
                                                             .getGameBoardMap()
                                                             .getContinents(),
                                                    GameBoard.getInstance()
                                                             .getGameBoardPlayer()
                                                             .getPlayers());
                GameBoard.getInstance()
                         .getGameBoardMap()
                         .getCountries()
                         .add(linkedCountry);
            }

            GameBoard.getInstance().loadCardFromSave(cards);
        }
    }
}