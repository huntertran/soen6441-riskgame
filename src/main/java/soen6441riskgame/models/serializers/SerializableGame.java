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
import soen6441riskgame.singleton.GameBoardPlaying;

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
    @Expose
    private GameBoardPlaying gameBoardPlaying;

    /**
     * serialize game objects
     *
     * @return json string
     */
    public String serialize() {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                                     .serializeNulls()
                                     .excludeFieldsWithoutExposeAnnotation()
                                     .create();

        return gson.toJson(this);
    }

    /**
     * Builder pattern for serialize the game
     */
    public static class Builder {
        private List<Country> countries;
        private List<Continent> continents;
        private List<Player> players;
        private int[][] borders;
        private Card[] cards;
        private GameBoardPlaying gameBoardPlaying;

        /**
         * set continents for save
         *
         * @param continents continents for save
         * @return builder object
         */
        public Builder setContinents(List<Continent> continents) {
            this.continents = continents;
            return this;
        }

        /**
         * set countries for save
         *
         * @param countries countries for save
         * @return builder object
         */
        public Builder setCountries(List<Country> countries) {
            this.countries = countries;
            return this;
        }

        /**
         * set players for save
         *
         * @param players players for save
         * @return builder object
         */
        public Builder setPlayers(List<Player> players) {
            this.players = players;
            return this;
        }

        /**
         * set borders for save
         *
         * @param borders borders for save
         * @return builder object
         */
        public Builder setBorders(int[][] borders) {
            this.borders = borders;
            return this;
        }

        /**
         * set cards for save
         *
         * @param cards cards for save
         * @return builder object
         */
        public Builder setCards(Card[] cards) {
            this.cards = cards;
            return this;
        }

        /**
         * set playing data
         *
         * @param gameBoardPlaying playing data
         * @return builder object
         */
        public Builder setGameBoardPlaying(GameBoardPlaying gameBoardPlaying) {
            this.gameBoardPlaying = gameBoardPlaying;
            return this;
        }

        /**
         * build object in specific order
         *
         * @return SerializableGame that hold all the data
         */
        public SerializableGame build() {
            SerializableGame serializableGame = new SerializableGame();
            serializableGame.continents = this.continents;
            serializableGame.countries = this.countries;
            serializableGame.players = this.players;
            serializableGame.borders = this.borders;
            serializableGame.cards = this.cards;
            serializableGame.gameBoardPlaying = this.gameBoardPlaying;
            return serializableGame;
        }

        /**
         * reconstruct game in specific order
         */
        public void reconstructGame() {
            GameBoard.getInstance().reset();

            for (Continent continent : continents) {
                continent.initializeCountries();
                GameBoard.getInstance()
                         .getGameBoardMap()
                         .getContinents()
                         .add(continent);
            }

            GameBoard.getInstance().getGameBoardMap().setBorders(borders);

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

                for (Continent continent : GameBoard.getInstance().getGameBoardMap().getContinents()) {
                    if (linkedCountry.getContinent().getName() == continent.getName()) {
                        continent.getCountries().add(linkedCountry);
                    }
                }
            }

            GameBoard.getInstance().loadCardFromSave(cards);

            GameBoard.getInstance().loadGameBoardPlaying(gameBoardPlaying);
        }
    }
}
