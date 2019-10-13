package soen6441riskgame.models;

public class Player {
    private String name;
    private int armies;
    private int unplacedArmies;
    private boolean isPlaying = false;

    public String getName() {
        return name;
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
