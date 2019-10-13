package soen6441riskgame.models;

import java.util.ArrayList;

public class Continent implements Viewable {
    private String name;
    private int army;
    private ArrayList<Country> countries = new ArrayList<Country>();
    private int order;

    public Continent(String name, int army, int... order) {
        this.setName(name);
        this.setArmy(army);

        if (order.length > 0) {
            this.setOrder(order[0]);
        }
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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

    public void view(int indent) {
        this.viewWithoutCountry();

        for (Country country : this.getCountries()) {
            country.view(indent + 1);
        }
    }

    public void viewWithoutCountry() {
        System.out.format("Continent: %s | No.: %s | Number of army: %s\n", this.getName(), this.getOrder(),
                this.getArmy());
    }
}