package soen6441riskgame.models;

import java.util.ArrayList;

import soen6441riskgame.singleton.GameMap;

public class Player {
    private String name;
    private int armies;
    private int unplacedArmies;
    private boolean isPlaying = false;
    private boolean isLost = false;

    public String getName() {
        return name;
    }

    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean isLost) {
        this.isLost = isLost;
    }

    public ArrayList<Country> getConqueredCountries() {
        ArrayList<Country> conquered = new ArrayList<>();

        for (Country country : GameMap.getInstance().getCountries()) {
            if (country.getConquerer().equals(this)) {
                conquered.add(country);
            }
        }

        return conquered;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public int getUnplacedArmies() {
        return unplacedArmies;
    }

    public void setUnplacedArmies(int unplacedArmies) {
        this.unplacedArmies = unplacedArmies;
    }

    public int getArmies() {
        return armies;
    }

    public void setArmies(int armies) {
        this.armies = armies;
    }

    public void setName(String name) {
        this.name = name;
    }
}
