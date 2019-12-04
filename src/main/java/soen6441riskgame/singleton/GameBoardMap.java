package soen6441riskgame.singleton;

import java.util.ArrayList;
import java.util.Observer;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.utils.ConsolePrinter;
import soen6441riskgame.views.PlayersWorldDominationView;
import soen6441riskgame.views.WorldView;
import soen6441riskgame.views.WorldViewForUnitTest;

/**
 * hold countries, continents and player world domination view
 */
public class GameBoardMap implements Resettable {
    private PlayersWorldDominationView playersWorldDominationView = new PlayersWorldDominationView();
    private WorldView worldView;
    private WorldViewForUnitTest worldViewForUnitTest;
    private String mapName;
    private ArrayList<Continent> continents = new ArrayList<Continent>();
    private ArrayList<Country> countries = new ArrayList<Country>();
    private int[][] borders;

    public GameBoardMap() {
        if (ConsolePrinter.isJUnitTest()) {
            worldViewForUnitTest = new WorldViewForUnitTest();
        } else {
            worldView = new WorldView();
        }
    }

    /**
     * get PlayersWorldDominationView instance
     *
     * @return PlayersWorldDominationView instance
     */
    public PlayersWorldDominationView getPlayersWorldDominationView() {
        return playersWorldDominationView;
    }

    public Observer getWordView() {
        if (ConsolePrinter.isJUnitTest()) {
            return worldViewForUnitTest;
        } else {
            return worldView;
        }
    }

    public WorldView getWorldView() {
        return worldView;
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
        GameBoard.getInstance().getGameBoardMap().getWordView().update(null, null);
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
     * get map name
     * 
     * @return map name
     */
    public String getMapName() {
        return mapName;
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
     * check if country with the specified name existed in map
     *
     * @param countryName name of the country
     * @return true if country existed
     */
    public boolean isCountryExisted(String countryName) {
        Country country = getCountryFromName(countryName);

        return country != null;
    }

    /**
     * get continent object from name
     *
     * @param continentName name of the continent
     * @return null if continent name is not existed in map
     */
    public Continent getContinentFromName(String continentName) {
        for (Continent continent : getContinents()) {
            if (continent.getName().equals(continentName)) {
                return continent;
            }
        }

        return null;
    }

    /**
     * check if continent with the specified name existed in map
     *
     * @param continentName name of the continent
     * @return is continent existed
     */
    public boolean isContinentExisted(String continentName) {
        Continent continent = getContinentFromName(continentName);

        return continent != null;
    }

    /**
     * add the country to country list in GameMap, and add to continent's country list in GameMap this
     * function should be remove when Dependency Injection implemented
     *
     * @param country   the country object
     * @param continent the continent object
     */
    public void updateCountryContinent(Country country, Continent continent) {
        country.addObserver(getPlayersWorldDominationView());
        country.addObserver(getWordView());
        getCountries().add(country);
        continent.getCountries().add(country);
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
