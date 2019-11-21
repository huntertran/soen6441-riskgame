package soen6441riskgame.singleton;

import java.util.ArrayList;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.utils.ConsolePrinter;
import soen6441riskgame.views.PlayersWorldDominationView;

/**
 * hold countries, continents and player world domination view
 */
public class GameBoardMap implements Resettable {
    private PlayersWorldDominationView playersWorldDominationView = new PlayersWorldDominationView();
    private String mapName;
    private ArrayList<Continent> continents = new ArrayList<Continent>();
    private ArrayList<Country> countries = new ArrayList<Country>();
    private int[][] borders;

    /**
     * get PlayersWorldDominationView instance
     *
     * @return PlayersWorldDominationView instance
     */
    public PlayersWorldDominationView getPlayersWorldDominationView() {
        return playersWorldDominationView;
    }

    /**
     * get borders of countries, in the form of matrix
     *
     * @return 2-d array
     */
    public int[][] getBorders() {
        return borders;
    }

    /**
     * replace the borders matrix with new borders matrix
     *
     * @param graph the new border to set
     */
    public void setBorders(int[][] graph) {
        this.borders = graph;
    }

    /**
     * get the list of continent objects
     *
     * @return list of continent objects
     */
    public ArrayList<Continent> getContinents() {
        return continents;
    }

    /**
     * set the name of the map (for saving)
     *
     * @param mapName name of map
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    /**
     * get the list of Country objects
     *
     * @return list of Country objects
     */
    public ArrayList<Country> getCountries() {
        return countries;
    }

    /**
     * reset continents and countries
     */
    @Override
    public void reset() {
        mapName = "";
        continents = new ArrayList<Continent>();
        countries = new ArrayList<Country>();
        borders = new int[1][1];

    }

    /**
     * print all continents
     */
    public void showContinents() {
        for (Continent continent : continents) {
            continent.view();
        }
    }

    /**
     * get country object from name
     *
     * @param countryName name of the country
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
     * Print the current border in matrix style
     */
    public void printBorders() {
        int[][] borders = getBorders();
        ArrayList<Country> countries = getCountries();

        String[] countryNames = new String[countries.size()];

        for (int index = 0; index < countries.size(); index++) {
            countryNames[index] = countries.get(index).getName();
        }

        ConsolePrinter.print2dArray(borders, countryNames);
    }

}
