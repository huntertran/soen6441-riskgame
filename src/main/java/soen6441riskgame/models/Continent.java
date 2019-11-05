package soen6441riskgame.models;

import java.util.ArrayList;
import java.util.Observable;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;
import soen6441riskgame.utils.GraphChecker;

/**
 * Hold continent data
 */
public class Continent extends Observable implements Viewable {
    private String name;
    private int army;
    private ArrayList<Country> countries = new ArrayList<Country>();

    /**
     * constructor
     *
     * @param name name of the continent
     * @param army the continent value
     */
    public Continent(String name, int army) {
        this.name = name;
        this.army = army;
    }

    /**
     * get the continent order from list, start with 1
     */
    public int getOrder() {
        return GameBoard.getInstance().getGameBoardMap().getContinents().indexOf(this) + 1;
    }

    /**
     * get continent's army that will be rewarded to player whose conquer this continent
     *
     * @return the number of army added to conquerer
     */
    public int getArmy() {
        return army;
    }

    /**
     * get all the countries inside this continent
     *
     * @return the countries inside this continent
     */
    public ArrayList<Country> getCountries() {
        return countries;
    }

    /**
     * get continent name
     */
    public String getName() {
        return name;
    }

    /**
     * get continent conquerer
     *
     * @return null if not conquered by any player
     */
    public Player getConquerer() {
        ArrayList<Country> countries = getCountries();

        if (countries.size() == 0) {
            return null;
        }

        Player countryConquerer = countries.get(0).getConquerer();
        for (Country country : countries) {
            Player conquerer = country.getConquerer();
            if (conquerer != null && !conquerer.equals(countryConquerer)) {
                return null;
            }
        }

        return countryConquerer;
    }

    /**
     * check if the continent is a mini-connected graph
     *
     * @see https://github.com/huntertran/soen6441-riskgame/wiki/Connected-Graph-Validation-Unit-Test
     * @return false if it's not a mini-connected graph
     */
    public boolean isContinentConnected() {
        ArrayList<Country> countries = getCountries();
        return GraphChecker.isCountriesConnected(countries);
    }

    /**
     * print the continent content
     */
    public void view(int indent) {
        this.viewWithoutCountry();

        for (Country country : this.getCountries()) {
            country.view(indent + 1);
        }
    }

    /**
     * print the continent content without printing it's countries
     */
    public void viewWithoutCountry() {
        String printString = "Continent: %s\t| No.: %s\t| Number of army: %s";

        String conquererName = "";

        if (this.getConquerer() != null) {
            printString += "\t| Conquerer: %s";
            conquererName = this.getConquerer().getName();
        }

        ConsolePrinter.printFormat(printString,
                                   this.getName(),
                                   this.getOrder(),
                                   this.getArmy(),
                                   conquererName);
    }
}