package main.java.soen6441riskgame.models;

import java.util.ArrayList;

public class Continent {
    private String name;
    private int army;
    private ArrayList<Country> Countries;

    public Continent(String name, int army) {
        this.setName(name);
        this.setArmy(army);
    }

    public int getArmy() {
        return army;
    }

    public void setArmy(int army) {
        this.army = army;
    }

    public ArrayList<Country> getCountries() {
        return Countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        this.Countries = countries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}