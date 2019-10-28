package soen6441riskgame.models;

import java.util.ArrayList;

import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;

/**
 * Hold country data
 */
public class Country implements Viewable {
    private Coordinate coordinate;
    private int armyAmount;
    private String name;
    private Continent continent;
    private Player conquerer;

    public Country(String name, Coordinate coordinate, Continent continent) {
        this.setName(name);
        this.setCoordinate(coordinate);
        this.setContinent(continent);
    }

    public Player getConquerer() {
        return conquerer;
    }

    public void setConquerer(Player conquerer) {
        ConsolePrinter.printFormat("Player %s conquered %s", conquerer.getName(), this.getName());
        this.conquerer = conquerer;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArmyAmount() {
        return armyAmount;
    }

    public void setArmyAmount(int armyAmount) {
        this.armyAmount = armyAmount;
        ConsolePrinter.printFormat("Country %s now have %d armies, belong to %s", getName(), getArmyAmount(),
                                   getConquerer().getName());
    }

    /**
     * return the order of the country in the country list, starting with 1
     */
    public int getOrder() {
        return GameBoard.getInstance().getGameBoardMap().getCountries().indexOf(this) + 1;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * get the neighbors of this country
     *
     * @return if no neighbors, return an empty list
     */
    public ArrayList<Country> getNeighbors() {
        ArrayList<Country> neighbors = new ArrayList<Country>();

        int[][] borders = GameBoard.getInstance().getGameBoardMap().getBorders();
        int countryOrder = this.getOrder();

        for (int index = 0; index < borders[countryOrder - 1].length; index++) {
            if (borders[countryOrder - 1][index] == 1) {
                neighbors.add(GameBoard.getInstance().getGameBoardMap().getCountries().get(index));
            }
        }

        return neighbors;
    }

    public boolean isConquered() {
        return this.getConquerer() != null;
    }

    /**
     * Increase armies inside this country from unplaced armies
     *
     * @param amount of armies from unplaced armies of the conquerer
     */
    public void receiveArmiesFromUnPlacedArmies(int amount) {
        Player conquerer = this.getConquerer();

        if (amount > conquerer.getUnplacedArmies()) {
            System.out.println(
                               "The amount of armies you want to place in this country is bigger than the amount of armies you have");
            return;
        }

        this.increaseArmies(amount);

        int newUnplacedArmiesOfConquerer = conquerer.getUnplacedArmies() - amount;
        conquerer.setUnplacedArmies(newUnplacedArmiesOfConquerer);
    }

    /**
     * Increase armies inside this country from unplaced armies
     *
     * @param amount of armies from unplaced armies of the conquerer
     */
    public void increaseArmies(int amount) {
        int newArmiesAmount = this.getArmyAmount() + amount;
        this.setArmyAmount(newArmiesAmount);
    }

    /**
     * decrease armies inside this country from unplaced armies
     *
     * @param amount of armies from unplaced armies of the conquerer
     */
    public void decreaseArmies(int amount) {
        int newArmiesAmount = this.getArmyAmount() - amount;
        this.setArmyAmount(newArmiesAmount);
    }

    /**
     * move armies from this country to another neighbor country (have the same conquerer)
     *
     * @param toCountry    the destination country
     * @param armiesToMove number of armies to move
     */
    public void moveArmies(Country toCountry, int armiesToMove) {
        if (!this.isNeighboringCountries(toCountry)) {
            ConsolePrinter.printFormat("Country %s and %s is not neighbor", this.getName(), toCountry.getName());
            return;
        }

        if (armiesToMove > this.getArmyAmount() - 1) {
            System.out.println("The 'fromcountry' must have at least 1 army after fortification");
            ConsolePrinter.printFormat("You are moving %1$d army from %2$s to %3$s, but %2$s only have %4d armies left",
                                       armiesToMove, this.getName(), toCountry.getName(), this.getArmyAmount());
            return;
        }

        this.decreaseArmies(armiesToMove);
        toCountry.increaseArmies(armiesToMove);
    }

    public void view(int indent) {
        this.viewWithoutNeighbors(indent);

        this.printIndent(indent + 1);

        System.out.println("Neighbors:");
        for (Country country : this.getNeighbors()) {
            country.viewWithoutNeighbors(indent + 2);
        }
    }

    public void viewWithoutNeighbors(int indent) {
        this.printIndent(indent);
        String printString = "Country: %s\t| No.: %s\t| Army: %s ";

        String conquererName = "";

        if (this.getConquerer() != null) {
            printString += "\t| Conquerer: %s";
            conquererName = this.getConquerer().getName();
        }

        ConsolePrinter.printFormat(printString,
                                   this.getName(),
                                   this.getOrder(),
                                   this.getArmyAmount(),
                                   conquererName);
    }

    /**
     * Place army for player
     *
     * @param player the player to place army
     */
    public void placeArmy(Player player) {
        int originalArmy = getArmyAmount();
        setArmyAmount(originalArmy + 1);
        int newUnplacedArmies = player.getUnplacedArmies() - 1;
        player.setUnplacedArmies(newUnplacedArmies);
    }

    /**
     * check if 2 country is neighbor in map
     *
     * @param neighborCountry neighbor country object
     * @return false if any of two countries is not existed
     */
    public boolean isNeighboringCountries(Country neighborCountry) {
        int countryOrder = -1;
        int neighbouringCountryOrder = -1;
        countryOrder = getOrder();
        neighbouringCountryOrder = neighborCountry.getOrder();
        if (GameBoard.getInstance().getGameBoardMap().getBorders()[countryOrder - 1][neighbouringCountryOrder - 1] == 1
            && countryOrder != -1 && neighbouringCountryOrder != -1) {
            return true;
        }
        return false;
    }
}
