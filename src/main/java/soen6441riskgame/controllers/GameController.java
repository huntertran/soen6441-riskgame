package soen6441riskgame.controllers;

import java.util.ArrayList;
import java.util.Random;

import soen6441riskgame.enums.CommonCommandArgs;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.utils.ConsolePrinter;
import soen6441riskgame.utils.Parser;

/**
 * Control the game
 */
public class GameController {

    // TODO: issue #20: https://github.com/huntertran/soen6441-riskgame/issues/20
    public static int MAX_INITIAL_ARMY_AMOUNT = 50;
    public static final int MINIMUM_NUMBER_OF_ARMY_ON_COUNTRY = 1;
    public static int attacker_numDice = 0;
    public static int defender_numDice = 0;
    public static Country attackingCountry = null;
    public static Country defendingCountry = null;
    /**
     * handle <code>gameplayer</code> command
     *
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
                GameBoard.getInstance().getGameBoardPlayer().addPlayer(args[1]);
                break;
            }
            case REMOVE: {
                GameBoard.getInstance().getGameBoardPlayer().removePlayer(args[1]);
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
        int totalCountry = GameBoard.getInstance().getGameBoardMap().getCountries().size();
        int totalPlayer = GameBoard.getInstance().getGameBoardPlayer().getPlayers().size();
        int numberOfAssignedCountry = 0;
        int player_counter = 0;

        Random random = new Random();

        while (numberOfAssignedCountry < totalCountry) {

            if (player_counter == totalPlayer) {
                player_counter = 0;
            }

            int nextIndexCountryToAssign = random.nextInt(totalCountry);
            // int playerIndexToAssign = random.nextInt(totalPlayer);
            int playerIndexToAssign = player_counter;

            Country countryToAssign = GameBoard.getInstance().getGameBoardMap().getCountries()
                                               .get(nextIndexCountryToAssign);

            if (!countryToAssign.isConquered()) {
                Player player = GameBoard.getInstance().getGameBoardPlayer().getPlayers().get(playerIndexToAssign);
                countryToAssign.setConquerer(player);

                // user need to place at least 1 army to the country he owned
                countryToAssign.setArmyAmount(MINIMUM_NUMBER_OF_ARMY_ON_COUNTRY);
                numberOfAssignedCountry++;
                player_counter++;
            }
        }

        System.out.println("All countries are randomly assigned to players");
    }

    /**
     * allocate a number of initial armies to each players, depending on number of players
     */
    public void initPlayersUnplacedArmies() {
        ArrayList<Player> players = GameBoard.getInstance().getGameBoardPlayer().getPlayers();
        int unplacedArmiesEachPlayer = MAX_INITIAL_ARMY_AMOUNT / players.size();

        for (Player player : players) {
            player.setUnplacedArmies(unplacedArmiesEachPlayer);
        }
    }

    /**
     * Handle place army command for current player. The player is selected in a round-robin rule
     *
     * @param countryName name of the country to place army. The country must belong to current player
     */
    public void handlePlaceArmyCommand(String countryName) {
        Country country = GameBoard.getInstance().getGameBoardMap().getCountryFromName(countryName);
        Player currentPlayer = getCurrentPlayer();

        if (country == null) {
            ConsolePrinter.printFormat("Country %s not existed", countryName);
            return;
        }

        if (!country.getConquerer().isPlaying()) {
            System.out.println("Country not belong to the current player");
            return;
        }

        if (currentPlayer.getUnplacedArmies() != 0) {
            country.placeArmy(country.getConquerer());
        } else {
            turnToNextPlayer();
            getCurrentPlayer(true);
        }
    }

    /**
     * automatically randomly place all remaining unplaced armies for all players
     */
    public void handlePlaceAllCommand() {
        Random random = new Random();

        for (Player player : GameBoard.getInstance().getGameBoardPlayer().getPlayers()) {
            ArrayList<Country> conqueredCountries = player.getConqueredCountries();

            if (player.getUnplacedArmies() > 0) {
                for (Country country : conqueredCountries) {
                    int armiesToPlace = random.nextInt(player.getUnplacedArmies());
                    country.receiveArmiesFromUnPlacedArmies(armiesToPlace);
                }
            }
        }
    }

    /**
     * get current player without print the message
     *
     * @return current player
     */
    public Player getCurrentPlayer() {
        return getCurrentPlayer(false);
    }

    /**
     * start round-robin for list of players, exclude lost players
     */
    public Player startRoundRobinPlayers() {
        ArrayList<Player> players = GameBoard.getInstance().getGameBoardPlayer().getPlayers();

        // set first player
        for (Player player : players) {
            if (player.getCurrentPhase() != GamePhase.LOST) {
                player.setPlaying(true);
                return player;
            }
        }

        return null;
    }

    /**
     * give turn to the next player in list
     */
    public void turnToNextPlayer() {
        Player currentPlayer = getCurrentPlayer();

        if (currentPlayer != null) {
            currentPlayer.setPlaying(false);
            currentPlayer.setCurrentPhase(GamePhase.WAITING_TO_TURN);
            currentPlayer.getNextPlayer().setPlaying(true);
        }
    }

    /**
     * print the current player to console
     */
    public void showCurrentPlayer() {
        Player player = getCurrentPlayer();
        ConsolePrinter.printFormat("Player %s is in turn", player.getName());
        ConsolePrinter.printFormat("Current phase: %s", player.getCurrentPhase().toString());
        ConsolePrinter.printFormat("    Un-placed Armies: %d", player.getUnplacedArmies());
        ConsolePrinter.printFormat("    Countries conquered:");

        for (Country country : player.getConqueredCountries()) {
            country.viewWithoutNeighbors(2);
        }
    }

    /**
     * get current player
     *
     * @param isShowMessage if true, print the current player to screen
     * @return current player
     */
    private Player getCurrentPlayer(boolean isShowMessage) {
        Player currentPlayer = null;
        ArrayList<Player> players = GameBoard.getInstance().getGameBoardPlayer().getPlayers();

        for (Player player : players) {
            if (player.isPlaying()) {
                currentPlayer = player;
            }
        }

        if (currentPlayer == null) {
            currentPlayer = startRoundRobinPlayers();
        }

        if (isShowMessage) {
            ConsolePrinter.printFormat("Player %s is in turn", currentPlayer.getName());
        }

        return currentPlayer;
    }

    /**
     * REINFORCEMENT PHASE enter reinforcement phase
     *
     * @return true if enter reinforcement phase successfully or the current player is already in
     *         reinforcement phase
     */
    public boolean enterReinforcement() {
        Player currentPlayer = getCurrentPlayer(true);

        if (currentPlayer.getCurrentPhase() == GamePhase.WAITING_TO_TURN) {
            currentPlayer.calculateReinforcementArmies(this);
            currentPlayer.setCurrentPhase(GamePhase.REINFORCEMENT);
        } else {
            ConsolePrinter.printFormat("Player %s cannot reinforce armies in %s phase",
                                       currentPlayer.getName(),
                                       currentPlayer.getCurrentPhase().toString());
        }

        return currentPlayer.getCurrentPhase() == GamePhase.REINFORCEMENT;
    }

    /**
     * REINFORCEMENT PHASE handle <code>reinforce</code> command
     *
     * @param args [0] country name
     * @param args [1] number of armies to reinforce
     */
    public void handleReinforceCommand(String[] args) {
        Country country = GameBoard.getInstance().getGameBoardMap().getCountryFromName(args[0]);

        int numberOfArmies = Parser.parseWithDefault(args[1], 0);

        if (country == null) {
            ConsolePrinter.printFormat("Country %s is not existed", country);
            return;
        }

        Player currentPlayer = getCurrentPlayer(false);

        if (!country.getConquerer().equals(currentPlayer)) {
            ConsolePrinter.printFormat("The country %s is not belong to %s", country.getName(),
                                       currentPlayer.getName());
            return;
        }

        if (currentPlayer.getUnplacedArmies() != 0) {
            country.receiveArmiesFromUnPlacedArmies(numberOfArmies);
            currentPlayer.addCurrentPhaseAction("Reinforce: " + country.getName() + " with " + numberOfArmies);
        }

        if (currentPlayer.getUnplacedArmies() == 0) {
            ConsolePrinter.printFormat("Player %s enter FORTIFICATION phase", currentPlayer.getName());
            currentPlayer.setCurrentPhase(GamePhase.FORTIFICATION);
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

        Country fromCountry = GameBoard.getInstance().getGameBoardMap().getCountryFromName(args[0]);
        Country toCountry = GameBoard.getInstance().getGameBoardMap().getCountryFromName(args[1]);
        int numberOfArmies = Parser.parseWithDefault(args[2], 0);

        if (fromCountry == null || toCountry == null) {
            System.out.println("The country name(s) not existed");
            return;
        }

        fromCountry.moveArmies(toCountry, numberOfArmies);
    }
    
    public boolean enterAttackPhase() {
        Player currentPlayer = getCurrentPlayer(true);

        if (currentPlayer.getCurrentPhase() == GamePhase.REINFORCEMENT) {
            currentPlayer.setCurrentPhase(GamePhase.ATTACK);
        } else if(currentPlayer.getCurrentPhase() != GamePhase.ATTACK) {
            ConsolePrinter.printFormat("Player %s cannot use attack in %s phase",
                                       currentPlayer.getName(),currentPlayer.getCurrentPhase());
        }
        
        return currentPlayer.getCurrentPhase() == GamePhase.ATTACK;
    }
    
    public void handleAttackCommand(String[] args) {
        ConsolePrinter.printFormat("attack conditions testing");
        //Player currentPlayer = getCurrentPlayer(false);
        
        //System.out.println(args[0]+args[1]+args[2]);
        //check if its no attack
        if(args[0].toLowerCase().equals("-noattack")) {
            endAttackPhase();
            return;
        }
        //need to check whether these countries are existent or not
        attackingCountry = GameBoard.getInstance().getGameBoardMap().getCountryFromName(args[0]);
        defendingCountry = GameBoard.getInstance().getGameBoardMap().getCountryFromName(args[1]);

        int numDice = Integer.parseInt(args[2]);
        attacker_numDice = numDice; //saving the number of dice
        
        //check if attack is valid
        if(!isAttackValid()) {
            return;
        }
    }
    
    public void handleDefendCommand(String[] args) {
        defender_numDice = Integer.parseInt(args[0]);
        System.out.println(args[0]);
        if(!isAttackValid()) {
            ConsolePrinter.printFormat("Defend command not allowed as attack is invalid");
            return;
        }
       
        else if(defender_numDice > 2) {
            ConsolePrinter.printFormat("You can only defend with a maximum of 2 armies at a time");
            return;
        }
        else if(defender_numDice > defendingCountry.getArmyAmount()) {
            ConsolePrinter.printFormat("Error. Number of dice rolls should be less than or equal to the number of armies in the defending country but atmost 2.");
            return;
        }
        
        int[] attackerDiceValues = new int[attacker_numDice];
        int[] defenderDiceValues = new int[defender_numDice];
        
        String printDiceValues = "Attacker: ";
        
        for(int i = 0; i < attacker_numDice; i++) {
            attackerDiceValues[i] = rollDice();
            printDiceValues += attackerDiceValues[i] + "    ";
        }
        printDiceValues += "\nDefender: ";
        for(int i = 0; i < defender_numDice; i++) {
            defenderDiceValues[i] = rollDice();
            printDiceValues += defenderDiceValues[i] + "    ";
        }
        ConsolePrinter.printFormat("%s", printDiceValues);
        
        //now we will check who loses an army
        if(defender_numDice == 1) {
            if(getMax(attackerDiceValues, false) > getMax(defenderDiceValues, false)) {
                //defending army is lost
                ConsolePrinter.printFormat("The defender %s has lost 1 army from %s. %d armies left.", defendingCountry.getConquerer().getName(), defendingCountry.getName(), defendingCountry.getArmyAmount());
                defendingCountry.setArmyAmount(defendingCountry.getArmyAmount() - 1);
                }
            else {
                //attacking army is lost
                ConsolePrinter.printFormat("The attacker %s has lost 1 army from %s. %d armies left.",attackingCountry.getConquerer().getName(), attackingCountry.getName(), attackingCountry.getArmyAmount());
                attackingCountry.setArmyAmount(attackingCountry.getArmyAmount() - 1);
                }
        }
        else {
            if(getMax(attackerDiceValues, false) > getMax(defenderDiceValues, false)) {
                //defending army is lost
                ConsolePrinter.printFormat("The defender %s has lost 1 army from %s. %d armies left.", defendingCountry.getConquerer().getName(), defendingCountry.getName(), defendingCountry.getArmyAmount());
                defendingCountry.setArmyAmount(defendingCountry.getArmyAmount() - 1);
                }
            else {
                //attacking army is lost
                ConsolePrinter.printFormat("The attacker %s has lost 1 army from %s. %d armies left.",attackingCountry.getConquerer().getName(), attackingCountry.getName(), attackingCountry.getArmyAmount());
                attackingCountry.setArmyAmount(attackingCountry.getArmyAmount() - 1);
                }
            if(getMax(attackerDiceValues, true) > getMax(defenderDiceValues, true)) {
                //defending army is lost
                ConsolePrinter.printFormat("The defender %s has lost 1 army from %s. %d armies left.", defendingCountry.getConquerer().getName(), defendingCountry.getName(), defendingCountry.getArmyAmount());
                defendingCountry.setArmyAmount(defendingCountry.getArmyAmount() - 1);
                }
            else {
                //attacking army is lost
                ConsolePrinter.printFormat("The attacker %s has lost 1 army from %s. %d armies left.",attackingCountry.getConquerer().getName(), attackingCountry.getName(), attackingCountry.getArmyAmount());
                attackingCountry.setArmyAmount(attackingCountry.getArmyAmount() - 1);
                }
        }
        //now check if defender's armies left is 0, set conquerer as attacker
        if(defendingCountry.getArmyAmount() == 0) {
            ConsolePrinter.printFormat("The attacker %s has conquered the country %s successfully. He has %s army available to move.", attackingCountry.getConquerer().getName(), attackingCountry.getName(), attackingCountry.getArmyAmount());
            defendingCountry.setConquerer(attackingCountry.getConquerer());
            //check if defender has any countries that he has conquered. if not remove him from the game.
            if(defendingCountry.getConquerer().getConqueredCountries().isEmpty()) {
                //remove player
                GameBoard.getInstance().getGameBoardPlayer().removePlayer(defendingCountry.getConquerer().getName());
            };
            if(checkHasGameEnded(attackingCountry.getConquerer())) {
                setEndOfGamePhase();
            }
            else {
                //move armies
            }
        }
        //reinitialize variables to null
        defendingCountry = null;
        attackingCountry = null;
        attacker_numDice = 0;
        defender_numDice = 0;
    }
    
    private boolean checkHasGameEnded(Player p) {
        //check whether this player has won the game
        Player currentPlayer = getCurrentPlayer(false);
        boolean flag = true;
        ArrayList<Country> countries = GameBoard.getInstance().getGameBoardMap().getCountries();
        for(Country country : countries) {
            if(country.getConquerer() != currentPlayer) {
                flag = false;
            }
        }
   
        return flag;
    }
    
    // Method for getting the maximum value and second max value
    private int getMax(int[] inputArray,boolean second_max){ 
      int maxValue = inputArray[0]; 
      int maxIndex = 0;
      for(int i=1;i < inputArray.length;i++){ 
          if(inputArray[i] > maxValue){ 
             maxValue = inputArray[i]; 
             maxIndex = i;
          } 
      }
    
      if(second_max) {
          int secondMaxValue = -1;
          for(int i=0;i < inputArray.length;i++){ 
              if(inputArray[i] > secondMaxValue && maxIndex != i){ 
                  secondMaxValue = inputArray[i]; 
              } 
          }
          maxValue = secondMaxValue;
      }
      
      return maxValue;   
    }
    
    public boolean isAttackValid() {
        Player currentPlayer = getCurrentPlayer(false);
        if(attackingCountry == null) {
            ConsolePrinter.printFormat("The country %s you have entered is non-existent", attackingCountry.getName());
            return false;
        }
        else if(defendingCountry == null) {
            ConsolePrinter.printFormat("The country %s you have entered is non-existent", defendingCountry.getName());
            return false;
        }
        
        //check if attacking country belongs to the current player
        else if (!attackingCountry.getConquerer().equals(currentPlayer)) {
            ConsolePrinter.printFormat("The country %s does not belong to %s", attackingCountry.getName(),
                                       currentPlayer.getName());
            return false;
        }
        //check if the defending country does not belong to the current player
        else if(defendingCountry.getConquerer().equals(currentPlayer)) {
            ConsolePrinter.printFormat("The country %s belongs to %s. You cannot attack your own country.", defendingCountry.getName(),
                                       currentPlayer.getName());
            return false;
        }
        //the countries have to be neighbours
        else if(!attackingCountry.isNeighboringCountries(defendingCountry)) {
            ConsolePrinter.printFormat("The countries %s and %s are not neighbouring countries. You can only attack neighbouring countries.", attackingCountry.getName(),
                                       defendingCountry.getName());
            return false;
        }
        //check if you have more than 2 army in attacking country
        else if(attackingCountry.getArmyAmount() < 2) {
            ConsolePrinter.printFormat("The country %s has less than 2 armies. You need atleast 2 armies to attack a country.", attackingCountry.getName());
            return false;
        }
        //check if the player has more armies than the numDice. numDice can be atmost 3.
        else if( (attackingCountry.getArmyAmount()-1) < attacker_numDice) {
            ConsolePrinter.printFormat("The number of armies available to attack are less than %s", attacker_numDice);
            return false;
        }
        //also numdice has to be less than 3
        else if(attacker_numDice > 3) {
            ConsolePrinter.printFormat("You can only attack with a maximum of 3 armies at a time");
            return false;
        }
       
        //attack execution
        else {
            if(defender_numDice == 0) {
                ConsolePrinter.printFormat("Attack conditions met. Enter defend command");
            }
            return true;
        }
    }
    
    private int rollDice() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }
    
    public void endAttackPhase() {
        attacker_numDice = 0;
        Player currentPlayer = getCurrentPlayer(true);
        ConsolePrinter.printFormat("End of attack Phase. Player %s has entered fortification phase", currentPlayer.getName());
        currentPlayer.setCurrentPhase(GamePhase.FORTIFICATION);
    }
    
    public void setEndOfGamePhase() {
        Player currentPlayer = getCurrentPlayer(true);
        ConsolePrinter.printFormat("Congratulations, The player %s has won the game.", currentPlayer.getName());
        currentPlayer.setCurrentPhase(GamePhase.END_OF_GAME);
    }
}