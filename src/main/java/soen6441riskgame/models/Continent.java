package soen6441riskgame.models;

import java.util.ArrayList;

import soen6441riskgame.utils.ConsolePrinter;

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

    public Player getConquerer() {
        ArrayList<Country> countries = getCountries();

        if (countries.size() == 0) {
            return null;
        }

        Player countryConquerer = countries.get(0).getConquerer();
        for (Country country : countries) {
            Player conquerer = country.getConquerer();
            if (!conquerer.equals(countryConquerer)) {
                return null;
            }
        }

        return countryConquerer;
    }

    public void view(int indent) {
        this.viewWithoutCountry();

        for (Country country : this.getCountries()) {
            country.view(indent + 1);
        }
    }

    public void viewWithoutCountry() {
        ConsolePrinter.printFormat("Continent: %s | No.: %s | Number of army: %s\n", this.getName(), this.getOrder(),
                this.getArmy());
    }
}