package soen6441riskgame.controllers;

import java.util.ArrayList;
import java.util.Random;

import soen6441riskgame.enums.CommonCommandArgs;
import soen6441riskgame.models.Continent;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameMap;
import soen6441riskgame.utils.ConsolePrinter;
import soen6441riskgame.utils.Parser;

public class GameController {

    public static int MAX_INITIAL_ARMY_AMOUNT = 50;
    public static final int MINIMUM_NUMBER_OF_ARMY_ON_COUNTRY = 1;

    /**
     * handle <code>editplayer</code> command
     * @param args [0] -add/-remove
     * @param args [1] player name
     */
    public void handlePlayerAddAndRemoveCommand(String[] args) {
        if (args.length == 0) {
            System.out.println("Missing parameter(s)");
            return;
        }

        CommonCommandArgs playerCommand = CommonCommandArgs.fromString(args[0]);

        switch (playerCommand) {
        case ADD: {
            GameMap.getInstance().addPlayer(args[1]);
            break;
        }
        case REMOVE: {
            GameMap.getInstance().removePlayer(args[1]);
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
                countryToAssign.setArmyAmount(MINIMUM_NUMBER_OF_ARMY_ON_COUNTRY);
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

    /**
     * Handle place army command for current player.
     * The player is selected in a round-robin rule
     * @param countryName name of the country to place army. The country must belong to current player
     */
    public void handlePlaceArmyCommand(String countryName) {
        Country country = GameMap.getInstance().getCountryFromName(countryName);

        if (country == null) {
            ConsolePrinter.printFormat("Country %s not existed", countryName);
            return;
        }

        if (!country.getConquerer().isPlaying()) {
            System.out.println("Country not belong to the current player");
            return;
        }

        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.getUnplacedArmies() != 0) {
            placeArmy(country, country.getConquerer());
        } else {
            turnToNextPlayer();
            getCurrentPlayer(true);
        }
    }

    /**
     * Place army for player
     * @param country the country to place army
     * @param player the player to place army
     */
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

    /**
     * get current player without print the message
     * @return current player
     */
    private Player getCurrentPlayer() {
        return getCurrentPlayer(false);
    }

    /**
     * start round-robin for list of players, exclude lost players
     */
    public void startRoundRobinPlayers() {
        Player currentPlayer = getCurrentPlayer();
        ArrayList<Player> players = GameMap.getInstance().getPlayers();

        if (currentPlayer == null) {
            // set first player
            for (Player player : players) {
                if (!player.isLost()) {
                    player.setPlaying(true);
                    break;
                }
            }
        }
    }

    /**
     * give turn to the next player in list
     */
    public void turnToNextPlayer() {
        Player currentPlayer = getCurrentPlayer();

        if (currentPlayer != null) {
            currentPlayer.setPlaying(false);
            currentPlayer.getNextPlayer().setPlaying(true);
        }
    }

    /**
     * get current player
     * @param isShowMessage if true, print the current player to screen
     * @return current player
     */
    private Player getCurrentPlayer(boolean isShowMessage) {
        Player currentPlayer = null;
        ArrayList<Player> players = GameMap.getInstance().getPlayers();

        for (Player player : players) {
            if (player.isPlaying() && !player.isLost()) {
                currentPlayer = player;
                break;
            }
        }

        if (isShowMessage) {
            ConsolePrinter.printFormat("Player %s is in turn", currentPlayer.getName());
        }

        return currentPlayer;
    }

    /**
     * REINFORCEMENT PHASE
     * get the number of armies player will get for reinforcement phase for all the country player have.
     * @param currentPlayer current player
     * @return the number of armies. Minimum number of armies are 3
     */
    private int getArmiesFromAllConqueredCountries(Player currentPlayer) {
        ArrayList<Country> conqueredCountries = currentPlayer.getConqueredCountries();
        return Math.round(conqueredCountries.size() / 3);
    }

    /**
     * REINFORCEMENT PHASE
     * get the number of armies player will have for the conquered continent
     * @param currentPlayer current player
     * @return the number of armies. 0 if user don't own any continent.
     */
    private int getArmiesFromConqueredContinents(Player currentPlayer) {
        int armiesFromConqueredContinents = 0;

        for (Continent continent : GameMap.getInstance().getContinents()) {
            if (continent.getConquerer().equals(currentPlayer)) {
                armiesFromConqueredContinents = armiesFromConqueredContinents + continent.getArmy();
            }
        }

        return armiesFromConqueredContinents;
    }

    /**
     * REINFORCEMENT PHASE
     * calculate the number of armies a player will have for his reinforcement phase
     * @param currentPlayer the current player
     */
    private void calculateReinforcementArmies(Player currentPlayer) {
        int armiesFromAllConqueredCountries = getArmiesFromAllConqueredCountries(currentPlayer);
        int armiesFromConqueredContinents = getArmiesFromConqueredContinents(currentPlayer);

        if (armiesFromAllConqueredCountries < 3) {
            armiesFromAllConqueredCountries = 3;
        }

        int newUnplacedArmies = currentPlayer.getUnplacedArmies() + armiesFromAllConqueredCountries
                + armiesFromConqueredContinents;

        currentPlayer.setUnplacedArmies(newUnplacedArmies);
    }

    /**
     * REINFORCEMENT PHASE
     * enter reinforcement phase
     */
    public void enterReinforcement() {
        Player currentPlayer = getCurrentPlayer(true);
        calculateReinforcementArmies(currentPlayer);
    }

    /**
     * REINFORCEMENT PHASE
     * handle <code>reinforce</code> command
     * @param args [0] country name
     * @param args [1] number of armies to reinforce
     */
    public void handleReinforceCommand(String[] args) {
        Country country = GameMap.getInstance().getCountryFromName(args[0]);

        int numberOfArmies = Parser.parseWithDefault(args[1], 0);

        if (country == null) {
            ConsolePrinter.printFormat("Country %s is not existed", country);
            return;
        }

        Player currentPlayer = getCurrentPlayer();

        if (!country.getConquerer().equals(currentPlayer)) {
            ConsolePrinter.printFormat("The country %s is not belong to %s", country.getName(), currentPlayer.getName());
            return;
        }

        if (currentPlayer.getUnplacedArmies() != 0) {
            country.receiveArmiesFromUnPlacedArmies(numberOfArmies);
        } else {
            turnToNextPlayer();
            getCurrentPlayer(true);
        }

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
            turnToNextPlayer();
            getCurrentPlayer(true);
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