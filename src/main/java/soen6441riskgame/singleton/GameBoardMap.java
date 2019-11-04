package soen6441riskgame.singleton;

import java.util.ArrayList;

import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.utils.ConsolePrinter;
import soen6441riskgame.views.PlayersWorldDominationView;

public class GameBoardMap implements Resettable {
    private PlayersWorldDominationView playersWorldDominationView = new PlayersWorldDominationView();
    private String mapName;
    private ArrayList<Continent> continents = new ArrayList<Continent>();
    private ArrayList<Country> countries = new ArrayList<Country>();
    private int[][] borders;

    public PlayersWorldDominationView getPlayersWorldDominationView(){
        return playersWorldDominationView;
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

    @Override
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