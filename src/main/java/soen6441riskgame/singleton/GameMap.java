package soen6441riskgame.singleton;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.utils.ConsolePrinter;

import java.util.ArrayList;

/**
 * Hold the game and map data
 */
public class GameMap {
    private static GameMap instance = new GameMap();
    private String mapName;
    private ArrayList<Continent> continents = new ArrayList<Continent>();
    private ArrayList<Country> countries = new ArrayList<Country>();
    private ArrayList<Player> players = new ArrayList<Player>();
    private int[][] borders;

    public GameMap() {
    };

    /**
     * set a new instance for unit testing
     *
     * @param newTestingInstance
     */
    public static void setTestingInstance(GameMap newTestingInstance) {
        instance = newTestingInstance;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int[][] getBorders() {
        return borders;
    }

    public void setBorders(int[][] graph) {
        this.borders = graph;
    }

    public ArrayList<Continent> getContinents() {
        return continents;
    }

    public void setContinents(ArrayList<Continent> continents) {
        this.continents = continents;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }

    public static GameMap getInstance() {
        return instance;
    }

    /**
     * reset the map
     */
    public void reset() {
        mapName = "";
        continents = new ArrayList<Continent>();
        countries = new ArrayList<Country>();
        borders = new int[1][1];
        players = new ArrayList<Player>();
    }

    public void showContinents() {
        for (Continent continent : continents) {
            continent.view();
        }
    }

    public void showCountries() {
        for (Country country : countries) {
            country.view();
        }
    }

    /**
     * get country object from name
     *
     * @param countryName
     * @return null if country name is not existed in map
     */
    public Country getCountryFromName(String countryName) {
        for (Country country : getCountries()) {
            if (country.getName().equals(countryName)) {
                return country;
            }
        }

        return null;
    }

    /**
     * get player object from name
     *
     * @param name
     * @return null if player name is not in the list
     */
    public Player getPlayerFromName(String name) {
        for (Player player : getPlayers()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }

        return null;
    }

    /**
     * add a new player and link to next/previous player
     *
     * @param name
     */
    public void addPlayer(String name) {
        Player player = getPlayerFromName(name);

        if (player == null) {

            player = new Player(name);
            players.add(player);

            int previousPlayerIndex = 0;
            if (players.size() > 1) {
                previousPlayerIndex = players.size() - 2;
            }

            Player previousPlayer = players.get(previousPlayerIndex);
            Player nextPlayer = players.get(0);

            player.setPreviousPlayer(previousPlayer);
            player.setNextPlayer(nextPlayer);

            ConsolePrinter.printFormat("Player %s added", name);
        }
    }

    /**
     * remove a player and destroy link to next/previous player
     *
     * @param name
     */
    public void removePlayer(String name) {
        Player player = getPlayerFromName(name);

        if (player != null) {
            players.remove(player);

            Player previousPlayer = player.getPreviousPlayer();
            Player nextPlayer = player.getNextPlayer();

            previousPlayer.setNextPlayer(nextPlayer);
            nextPlayer.setPreviousPlayer(previousPlayer);

            ConsolePrinter.printFormat("Player %s removed", name);
        } else {
            ConsolePrinter.printFormat("Player %s not exist in game", name);
        }
    }

    /**
     * Print the current border in matrix style
     */
    public void printBorders() {
        int[][] borders = GameMap.getInstance().getBorders();
        ArrayList<Country> countries = GameMap.getInstance().getCountries();

        String[] countryNames = new String[countries.size()];

        for (int index = 0; index < countries.size(); index++) {
            countryNames[index] = countries.get(index).getName();
        }

        ConsolePrinter.print2dArray(borders, countryNames);
    }
}