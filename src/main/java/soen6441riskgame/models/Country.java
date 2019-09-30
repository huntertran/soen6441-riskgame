package main.java.soen6441riskgame.models;

public class Country {
    private Coordinate coordinate;
    private int order;
    private int armyAmount;
    private String name;
    private Continent continent;

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
}