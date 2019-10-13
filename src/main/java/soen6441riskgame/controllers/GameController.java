package soen6441riskgame.controllers;

import java.util.ArrayList;
import java.util.Random;

import soen6441riskgame.enums.CommonCommandArgs;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameMap;

public class GameController {

    public static int MAX_INITIAL_ARMY_AMOUNT = 50;

    public void handlePlayerAddAndRemoveCommand(String[] arg) {
        CommonCommandArgs playerCommand = CommonCommandArgs.fromString(arg[0]);

        switch (playerCommand) {
        case ADD: {
            addPlayer(arg[1]);
            break;
        }
        case REMOVE: {
            removePlayer(arg[1]);
            break;
        }
        case NONE: {
            System.out.println("Incorrect command");
            break;
        }
        }
    }

    /**
     * random assign countries to players
     */
    public void populateCountries() {
        int totalCountry = GameMap.getInstance().getCountries().size();
        int totalPlayer = GameMap.getInstance().getPlayers().size();
        int numberOfAssignedCountry = 0;

        Random random = new Random();

        while (numberOfAssignedCountry < totalCountry) {
            int nextIndexCountryToAssign = random.nextInt(totalCountry);
            int playerIndexToAssign = random.nextInt(totalPlayer);

            Country countryToAssign = GameMap.getInstance().getCountries().get(nextIndexCountryToAssign);

            if (!countryToAssign.isConquered()) {
                Player player = GameMap.getInstance().getPlayers().get(playerIndexToAssign);
                countryToAssign.setConquerer(player);

                // user need to place at least 1 army to the country he owned
                countryToAssign.setArmyAmount(1);
                numberOfAssignedCountry++;
            }
        }

        System.out.println("All countries are randomly assigned to players");
    }

    /**
     * allocate a number of initial armies to each players, depending on number of
     * players
     */
    public void initPlayersUnplacedArmies() {
        ArrayList<Player> players = GameMap.getInstance().getPlayers();
        int unplacedArmiesEachPlayer = MAX_INITIAL_ARMY_AMOUNT / players.size();

        for (Player player : players) {
            player.setUnplacedArmies(unplacedArmiesEachPlayer);
        }
    }

    public void handlePlaceArmyCommand(String countryName) {
        Country country = GameMap.getInstance().getCountryFromName(countryName);

        if (country == null) {
            System.out.format("Country %s not existed", countryName);
            return;
        }

        if(!country.getConquerer().isPlaying()){
            System.out.println("Country not belong to the current player");
            return;
        }

        placeArmy(country, country.getConquerer());
    }

    public void placeArmy(Country country, Player player){
        int originalArmy = country.getArmyAmount();

        country.setArmyAmount(originalArmy + 1);

        int newUnplacedArmies = player.getUnplacedArmies() - 1;
        player.setUnplacedArmies(newUnplacedArmies);
    }

    private void addPlayer(String name) {
        Player player = getPlayerFromName(name);

        if (player == null) {
            player = new Player();
            player.setName(name);
            GameMap.getInstance().getPlayers().add(player);
            System.out.format("Player %d added", name);
        }
    }

    private void removePlayer(String name) {
        Player player = getPlayerFromName(name);

        if (player != null) {
            GameMap.getInstance().getPlayers().remove(player);
            System.out.format("Player %d removed", name);
        } else {
            System.out.format("Player %d not exist in game", name);
        }
    }

    private Player getPlayerFromName(String name) {
        for (Player player : GameMap.getInstance().getPlayers()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }

        return null;
    }
}