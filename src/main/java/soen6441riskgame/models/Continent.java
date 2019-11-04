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
    // private Player oldConquerer;
    private ArrayList<Country> countries = new ArrayList<Country>();

    public Continent(String name, int army) {
        this.setName(name);
        this.setArmy(army);
    }

    /**
     * get the continent order from list, start with 1
     */
    public int getOrder() {
        return GameBoard.getInstance().getGameBoardMap().getContinents().indexOf(this) + 1;
    }

    public int getArmy() {
        return army;
    }

    public void setArmy(int army) {
        this.army = army;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isContinentConnected() {
        ArrayList<Country> countries = getCountries();
        return GraphChecker.isCountriesConnected(countries);
    }

    public void view(int indent) {
        this.viewWithoutCountry();

        for (Country country : this.getCountries()) {
            country.view(indent + 1);
        }
    }

    public void viewWithoutCountry() {
        String printString = "Continent: %s\t| No.: %s\t| Number of army: %s";

        if (this.getConquerer() != null) {
            printString += "\t| Conquerer: %s";
        }

        ConsolePrinter.printFormat(printString,
                                   this.getName(),
                                   this.getOrder(),
                                   this.getArmy(),
                                   this.getConquerer());
    }
}