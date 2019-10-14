package soen6441riskgame.singleton;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;

import java.util.ArrayList;

public class GameMap {
    private static final GameMap instance = new GameMap();
    private String mapName;
    private ArrayList<Continent> continents = new ArrayList<Continent>();
    private ArrayList<Country> countries = new ArrayList<Country>();
    private ArrayList<Player> players = new ArrayList<Player>();
    private int[][] borders;

    private GameMap() {
    };

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

    public void reset() {
        mapName = "";
        continents = new ArrayList<Continent>();
        countries = new ArrayList<Country>();
        borders = new int[1][1];
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

    public Player getPlayerFromName(String name) {
        for (Player player : getPlayers()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }

        return null;
    }

    /**
     * check if 2 country is neighbor in map
     *
     * @param countryName
     * @param neighborCountryName
     * @return false if any of two countries is not existed
     */
    public boolean isNeighboringCountries(String countryName, String neighborCountryName) {
        Country country = getCountryFromName(countryName);
        Country neighbor = getCountryFromName(neighborCountryName);

        if (country == null || neighbor == null) {
            return false;
        }

        return isNeighboringCountries(country, neighbor);
    }

    public boolean isNeighboringCountries(Country country, Country neighborCountry) {
        int countryOrder = -1;
        int neighbouringCountryOrder = -1;
        countryOrder = country.getOrder();
        neighbouringCountryOrder = neighborCountry.getOrder();

        if (GameMap.getInstance().getBorders()[countryOrder - 1][neighbouringCountryOrder - 1] == 1
                && countryOrder != -1 && neighbouringCountryOrder != -1) {
            return true;
        }

        return false;
    }
}