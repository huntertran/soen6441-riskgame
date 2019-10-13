package soen6441riskgame.controllers;

import java.util.ArrayList;
import java.util.Random;

import soen6441riskgame.enums.CommonCommandArgs;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameMap;
import soen6441riskgame.utils.Parser;

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

        if (!country.getConquerer().isPlaying()) {
            System.out.println("Country not belong to the current player");
            return;
        }

        placeArmy(country, country.getConquerer());
    }

    private void placeArmy(Country country, Player player) {
        int originalArmy = country.getArmyAmount();

        country.setArmyAmount(originalArmy + 1);

        int newUnplacedArmies = player.getUnplacedArmies() - 1;
        player.setUnplacedArmies(newUnplacedArmies);
    }

    /**
     * automatically randomly place all remaining unplacedarmiesfor all players
     */
    public void handlePlaceAllCommand() {
        for (Player player : GameMap.getInstance().getPlayers()) {
            ArrayList<Country> conqueredCountries = player.getConqueredCountries();
            Random random = new Random();

            for (Country country : conqueredCountries) {
                int armiesToPlace = random.nextInt(player.getUnplacedArmies());
                country.increaseArmies(armiesToPlace);
            }
        }
    }

    private void addPlayer(String name) {
        Player player = GameMap.getInstance().getPlayerFromName(name);

        if (player == null) {
            player = new Player();
            player.setName(name);
            GameMap.getInstance().getPlayers().add(player);
            System.out.format("Player %d added", name);
        }
    }

    private void removePlayer(String name) {
        Player player = GameMap.getInstance().getPlayerFromName(name);

        if (player != null) {
            GameMap.getInstance().getPlayers().remove(player);
            System.out.format("Player %d removed", name);
        } else {
            System.out.format("Player %d not exist in game", name);
        }
    }

    private Player getCurrentPlayer(){
        Player currentPlayer = null;

        for(Player player : GameMap.getInstance().getPlayers()){
            if(player.isPlaying()){
                currentPlayer = player;
                break;
            }
        }

        if(currentPlayer == null){
            GameMap.getInstance().getPlayers().get(0).setPlaying(true);

            currentPlayer = GameMap.getInstance().getPlayers().get(0);
        }

        return currentPlayer;
    }

    private void calculateReinforcementArmies(Player currentPlayer){
        ArrayList<Country> conqueredCountries = currentPlayer.getConqueredCountries();
        int armiesFromAllConqueredCountries = Math.round(conqueredCountries.size() / 3);
        int armiesFromConqueredContinents = 0;

        int newUnplacedArmies = currentPlayer.getUnplacedArmies() + armiesFromAllConqueredCountries + armiesFromConqueredContinents;
        currentPlayer.setUnplacedArmies(newUnplacedArmies);
    }

    public void enterReinforcement() {
        Player currentPlayer = getCurrentPlayer();
        System.out.format("Player %s is in turn", currentPlayer.getName());
        calculateReinforcementArmies(currentPlayer);
    }

    public void handelReinforceCommand(String[] args){
        Country country = GameMap.getInstance().getCountryFromName(args[0]);

        int numberOfArmies = Parser.parseWithDefault(args[1], 0);

        if(country == null){
            System.out.format("Country %s is not existed", country);
            return;
        }

        Player currentPlayer = getCurrentPlayer();

        if(!country.getConquerer().equals(currentPlayer)){
            System.out.format("The country %s is not belong to %s", country.getName(), currentPlayer.getName());
            return;
        }

        country.increaseArmies(numberOfArmies);
    }
}