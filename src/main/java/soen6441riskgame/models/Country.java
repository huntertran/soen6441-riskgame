package main.java.soen6441riskgame.models;

import java.util.ArrayList;

import main.java.soen6441riskgame.singleton.GameMap;

public class Country implements Viewable {
    private Coordinate coordinate;
    private int order;
    private int armyAmount;
    private String name;
    private Continent continent;
    private ArrayList<Country> neighbors = new ArrayList<Country>();

    public Country(int order, String name, Coordinate coordinate, Continent continent) {
        this.setOrder(order);
        this.setName(name);
        this.setCoordinate(coordinate);
        this.setContinent(continent);
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
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    private ArrayList<Country> getNeighbors() {
        if (neighbors.isEmpty()) {
            for (int index = 0; index < GameMap.getInstance().getGraph()[this.getOrder() - 1].length; index++) {
                if (GameMap.getInstance().getGraph()[this.getOrder() - 1][index] == 1) {
                    neighbors.add(GameMap.getInstance().getCountries().get(index));
                }
            }
        }

        return neighbors;
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
        System.out.format("Country: %s | No.: %s | Army: %s\n", this.getName(), this.getOrder(), this.getArmyAmount());
    }
}