package soen6441riskgame.controllers;

import java.util.ArrayList;
import java.util.Random;

import soen6441riskgame.enums.CommonCommandArgs;
import soen6441riskgame.models.Continent;
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
                country.receiveArmiesFromUnPlacedArmies(armiesToPlace);
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

    private Player getCurrentPlayer() {
        Player currentPlayer = null;
        ArrayList<Player> players = GameMap.getInstance().getPlayers();

        for (Player player : players) {
            if (player.isPlaying() && !player.isLost()) {
                currentPlayer = player;
                break;
            }
        }

        if (currentPlayer == null) {
            // set first player
            for (Player player : players) {
                if (!player.isLost()) {
                    player.setPlaying(true);
                    currentPlayer = player;
                }
            }
        }

        return currentPlayer;
    }

    private int getArmiesFromAllConqueredCountries(Player currentPlayer) {
        ArrayList<Country> conqueredCountries = currentPlayer.getConqueredCountries();
        return Math.round(conqueredCountries.size() / 3);
    }

    private int getArmiesFromConqueredContinents(Player currentPlayer) {
        int armiesFromConqueredContinents = 0;

        for (Continent continent : GameMap.getInstance().getContinents()) {
            if (continent.getConquerer().equals(currentPlayer)) {
                armiesFromConqueredContinents = armiesFromConqueredContinents + continent.getArmy();
            }
        }

        return armiesFromConqueredContinents;
    }

    private void calculateReinforcementArmies(Player currentPlayer) {
        int armiesFromAllConqueredCountries = getArmiesFromAllConqueredCountries(currentPlayer);
        int armiesFromConqueredContinents = getArmiesFromConqueredContinents(currentPlayer);

        int newUnplacedArmies = currentPlayer.getUnplacedArmies() + armiesFromAllConqueredCountries
                + armiesFromConqueredContinents;

        currentPlayer.setUnplacedArmies(newUnplacedArmies);
    }

    public void enterReinforcement() {
        Player currentPlayer = getCurrentPlayer();
        System.out.format("Player %s is in turn", currentPlayer.getName());
        calculateReinforcementArmies(currentPlayer);
    }

    public void handelReinforceCommand(String[] args) {
        Country country = GameMap.getInstance().getCountryFromName(args[0]);

        int numberOfArmies = Parser.parseWithDefault(args[1], 0);

        if (country == null) {
            System.out.format("Country %s is not existed", country);
            return;
        }

        Player currentPlayer = getCurrentPlayer();

        if (!country.getConquerer().equals(currentPlayer)) {
            System.out.format("The country %s is not belong to %s", country.getName(), currentPlayer.getName());
            return;
        }

        country.receiveArmiesFromUnPlacedArmies(numberOfArmies);
    }

    /**
     * move any number of armies from one country to another if they are connected
     * 
     * If args[0] is "none" then user choose not to do a move
     * 
     * @param args[0] from country
     * @param args[1] to country
     * @param args[2] number of armies
     */
    public void handleFortifyCommand(String[] args) {
        if (args[0].toLowerCase().equals("none")) {
            return;
        }

        Country fromCountry = GameMap.getInstance().getCountryFromName(args[0]);
        Country toCountry = GameMap.getInstance().getCountryFromName(args[1]);
        int numberOfArmies = Parser.parseWithDefault(args[2], 0);

        if (fromCountry == null || toCountry == null) {
            System.out.println("The country name(s) not existed");
            return;
        }

        fromCountry.moveArmies(toCountry, numberOfArmies);
    }
}