package soen6441riskgame.controllers;

import java.util.ArrayList;
import java.util.Random;

import soen6441riskgame.enums.CommonCommandArgs;
import soen6441riskgame.enums.GamePhase;
import soen6441riskgame.models.Card;
import soen6441riskgame.models.CardSet;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;
import soen6441riskgame.singleton.GameBoard;
import soen6441riskgame.singleton.GameBoardPlaying;
import soen6441riskgame.utils.ConsolePrinter;
import soen6441riskgame.utils.Parser;

/**
 * Control the game
 */
public class GameController {
    private static final int NUMBER_OF_CARD_IN_ONE_SET = 3;
    private static final int MAXIMUM_ARMY_IN_ONE_ATTACK = 3;
    private static final int MINIMUM_ARMY_TO_ATTACK = 2;
    private static final int MINIMUM_NUMBER_OF_PLAYER = 2;
    private static final int MAXIMUM_NUMBER_OF_PLAYER = 6;

    /**
     * The minimum number army amount on a country
     */
    private static final int MINIMUM_NUMBER_OF_ARMY_ON_COUNTRY = 1;

    /**
     * handle <code>gameplayer</code> command
     *
     * @param args [0] -add/-remove - [1] player name
     */
    public void handleGamePlayerCommand(String[] args) {
        if (args.length == 0) {
            ConsolePrinter.printFormat("Missing parameter(s)");
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
            case INVALID:
            case NONE: {
                ConsolePrinter.printFormat("Incorrect command");
                break;
            }
        }
    }

    /**
     * check if number of player is valid
     * 
     * @return if number of player is valid
     */
    public boolean isNumberOfPlayerValid() {
        ArrayList<Player> players = GameBoard.getInstance()
                                             .getGameBoardPlayer()
                                             .getPlayers();

        int numberOfPlayers = players.size();

        if (numberOfPlayers < MINIMUM_NUMBER_OF_PLAYER || numberOfPlayers > MAXIMUM_NUMBER_OF_PLAYER) {
            ConsolePrinter.printFormat("Number of player must between %d and %d, currently the game have %d players",
                                       MINIMUM_NUMBER_OF_PLAYER,
                                       MAXIMUM_NUMBER_OF_PLAYER,
                                       players.size());
            return false;
        }

        return true;
    }

    /**
     * random assign countries to players equally
     */
    public void populateCountries() {
        int totalPlayer = GameBoard.getInstance()
                                   .getGameBoardPlayer()
                                   .getPlayers()
                                   .size();
        int playerIndexToAssign = 0;

        Random random = new Random();

        ArrayList<Country> allCountries = GameBoard.getInstance().getGameBoardMap().getCountries();
        ArrayList<Country> unAssignedCountries = new ArrayList<Country>();

        for (Country country : allCountries) {
            if (!country.isConquered()) {
                unAssignedCountries.add(country);
            }
        }

        while (unAssignedCountries.size() > 0) {
            if (playerIndexToAssign == totalPlayer) {
                playerIndexToAssign = 0;
            }

            int nextIndexCountryToAssign = random.nextInt(unAssignedCountries.size());
            Country countryToAssign = unAssignedCountries.get(nextIndexCountryToAssign);

            Player player = GameBoard.getInstance()
                                     .getGameBoardPlayer()
                                     .getPlayers()
                                     .get(playerIndexToAssign);
            countryToAssign.setConquerer(player);

            // user need to place at least 1 army to the country he owned
            countryToAssign.setArmyAmount(MINIMUM_NUMBER_OF_ARMY_ON_COUNTRY);

            playerIndexToAssign++;
            unAssignedCountries.remove(countryToAssign);
        }

        ConsolePrinter.printFormat("All countries are randomly assigned to players");
    }

    /**
     * allocate a number of initial armies to each players, depending on number of players, from the
     * game rule
     *
     * If 2 are playing, each player counts out 40 Infantry.
     *
     * If 3 are playing, each player counts out 35 Infantry.
     *
     * If 4 are playing, each player counts out 30 Infantry.
     *
     * If 5 are playing, each player counts out 25 Infantry.
     *
     * If 6 are playing, each player counts out 20 Infantry.
     */
    public void initPlayersUnplacedArmies() {
        ArrayList<Player> players = GameBoard.getInstance()
                                             .getGameBoardPlayer()
                                             .getPlayers();

        int unplacedArmiesEachPlayer = 0;

        switch (players.size()) {
            case 2: {
                unplacedArmiesEachPlayer = 40;
                break;
            }
            case 3: {
                unplacedArmiesEachPlayer = 35;
                break;
            }
            case 4: {
                unplacedArmiesEachPlayer = 30;
                break;
            }
            case 5: {
                unplacedArmiesEachPlayer = 25;
                break;
            }
            case 6: {
                unplacedArmiesEachPlayer = 20;
                break;
            }
        }

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
            ConsolePrinter.printFormat("Country not belong to the current player");
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

            // if (player.getUnplacedArmies() > 0) {
            for (Country country : conqueredCountries) {
                if (player.getUnplacedArmies() > 0) {
                    int armiesToPlace = random.nextInt(player.getUnplacedArmies());
                    if (armiesToPlace == 0 && player.getUnplacedArmies() == 1) {
                        armiesToPlace = 1;
                    }

                    country.receiveArmiesFromUnPlacedArmies(armiesToPlace);
                }
            }
            // }
        }
    }

    /**
     * This get the current player without print the message
     *
     * @return current player
     */
    public Player getCurrentPlayer() {
        return getCurrentPlayer(false);
    }

    /**
     * start round-robin for list of players, exclude lost players
     *
     * @return the player in turn
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
     * gives turn to the next player in list
     */
    void turnToNextPlayer() {
        Player currentPlayer = getCurrentPlayer();

        if (currentPlayer != null) {
            currentPlayer.setPlaying(false);

            if (currentPlayer.getCurrentPhase() != GamePhase.LOST) {
                currentPlayer.setCurrentPhase(GamePhase.WAITING_TO_TURN);
            }

            for (int index = 0; index < GameBoard.getInstance().getGameBoardPlayer().getPlayers().size(); index++) {
                if (currentPlayer.getNextPlayer().getCurrentPhase() != GamePhase.LOST) {
                    currentPlayer.getNextPlayer().setPlaying(true);
                    break;
                }
            }
        }
    }

    /**
     * print the current player to console
     */
    public void showCurrentPlayer() {
        Player player = getCurrentPlayer();

        if (player == null) {
            ConsolePrinter.printFormat("There is no player in game. Add some!");
            return;
        }

        ConsolePrinter.printFormat("Player %s is in turn", player.getName());
        ConsolePrinter.printFormat("Current phase: %s", player.getCurrentPhase().toString());
        ConsolePrinter.printFormat("    Un-placed Armies: %d", player.getUnplacedArmies());
        ConsolePrinter.printFormat("    Countries conquered:");

        for (Country country : player.getConqueredCountries()) {
            country.viewWithoutNeighbors(2);
        }

        ArrayList<Card> cards = player.getHoldingCards();
        if (!cards.isEmpty()) {
            ConsolePrinter.printFormat("Player cards:");

            int cardPosition = 1;
            for (Card card : player.getHoldingCards()) {
                ConsolePrinter.printFormat(GameBoard.getInstance().standardPrintStream,
                                           false,
                                           String.valueOf(cardPosition));
                card.view(1);
                cardPosition++;
            }
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
            if (player.isPlaying() && player.getCurrentPhase() != GamePhase.LOST) {
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
            currentPlayer.setCurrentPhase(GamePhase.REINFORCEMENT);
            currentPlayer.calculateReinforcementArmies();
        } else if (currentPlayer.getCurrentPhase() != GamePhase.REINFORCEMENT) {
            ConsolePrinter.printFormat("Player %s cannot reinforce armies in %s phase",
                                       currentPlayer.getName(),
                                       currentPlayer.getCurrentPhase().toString());
        }

        return currentPlayer.getCurrentPhase() == GamePhase.REINFORCEMENT;
    }

    /**
     * REINFORCEMENT PHASE handle <code>reinforce</code> command
     *
     * @param args [0] country name - [1] number of armies to reinforce
     */
    public void handleReinforceCommand(String[] args) {
        Country country = GameBoard.getInstance().getGameBoardMap().getCountryFromName(args[0]);

        int numberOfArmies = Parser.parseWithDefault(args[1], 0);

        if (country == null) {
            ConsolePrinter.printFormat("Country %s is not existed", args[0]);
            return;
        }

        Player currentPlayer = getCurrentPlayer(false);
        currentPlayer.reinforce(country, numberOfArmies);
    }

    /**
     * FORTIFICATION PHASE enter fortification phase
     *
     * @return true if enter fortification phase successfully or the current player is already in
     *         fortification phase
     */
    public boolean enterFortifyPhase() {
        Player currentPlayer = getCurrentPlayer(true);

        if (currentPlayer.getCurrentPhase() == GamePhase.ATTACK
            && !GameBoard.getInstance().getGameBoardPlaying().isAttackMoveCmdRequired()) {
            currentPlayer.setCurrentPhase(GamePhase.FORTIFICATION);
        } else if (currentPlayer.getCurrentPhase() != GamePhase.FORTIFICATION) {
            ConsolePrinter.printFormat("Player %s cannot fortify armies in %s phase",
                                       currentPlayer.getName(),
                                       currentPlayer.getCurrentPhase().toString());
        }

        return currentPlayer.getCurrentPhase() == GamePhase.FORTIFICATION;
    }

    /**
     * move any number of armies from one country to another if they are connected
     *
     * If last argument is "none" or "-none" then user choose not to do a move.
     *
     * The arguments can be divided by a set of 3 as below. User can enter multiple set of arguments.
     *
     * @param args [0] from country - [1] to country - [2] number of armies
     */
    public void handleMultipleFortificationCommand(String[] args) {
        int numberOfCommandSet = args.length / 3;
        int numberOfLeftOverArgs = args.length % 3;

        for (int index = 0; index < numberOfCommandSet; index++) {
            String[] commandSet = new String[] { args[(3 * index)],
                                                 args[(3 * index) + 1],
                                                 args[(3 * index) + 2] };
            handleFortifyCommand(commandSet);
        }

        if (numberOfLeftOverArgs > 0) {
            if (numberOfLeftOverArgs > 1) {
                ConsolePrinter.printFormat("You have some invalid command arguments");
            }

            String lastArg = args[args.length - 1].toLowerCase();

            if (lastArg.equals("-none") || lastArg.equals("none")) {
                turnToNextPlayer();
                getCurrentPlayer(true);
            }
        }
    }

    /**
     * move any number of armies from one country to another if they are connected
     *
     * If args[0] is "none" then user choose not to do a move
     *
     * @param args [0] from country - [1] to country - [2] number of armies
     */
    private void handleFortifyCommand(String[] args) {
        Country fromCountry = GameBoard.getInstance()
                                       .getGameBoardMap()
                                       .getCountryFromName(args[0]);
        Country toCountry = GameBoard.getInstance()
                                     .getGameBoardMap()
                                     .getCountryFromName(args[1]);

        int numberOfArmies = Parser.parseWithDefault(args[2], 0);

        if (fromCountry == null || toCountry == null) {
            ConsolePrinter.printFormat("The country name(s) not existed");
            return;
        }

        Player currentPlayer = getCurrentPlayer(false);

        if (!fromCountry.isCountryBelongToPlayer(currentPlayer)
            || !toCountry.isCountryBelongToPlayer(currentPlayer)) {
            return;
        }

        currentPlayer.fortify(fromCountry, toCountry, numberOfArmies);
    }

    /**
     * ATTACK PHASE enter attack phase
     *
     * @return true if enter attack phase successfully or the current player is already in attack phase
     */
    public boolean enterAttackPhase() {
        Player currentPlayer = getCurrentPlayer(true);

        if (currentPlayer.getCurrentPhase() == GamePhase.REINFORCEMENT) {
            currentPlayer.setCurrentPhase(GamePhase.ATTACK);
        } else if (currentPlayer.getCurrentPhase() != GamePhase.ATTACK) {
            ConsolePrinter.printFormat("Player %s cannot use attack in %s phase",
                                       currentPlayer.getName(),
                                       currentPlayer.getCurrentPhase());
        }

        return currentPlayer.getCurrentPhase() == GamePhase.ATTACK;
    }

    /**
     * it handles the attack command
     *
     * If args[0] is "-noattack" then user choose not to attack and the player moves on to next phase
     * fortification
     *
     * @param args [0] from country - [1] to country - [2] number of dice rolls or -allout option
     */
    public void handleAttackCommand(String[] args) {
        // ConsolePrinter.printFormat("attack conditions testing");

        GameBoardPlaying gameBoardPlaying = GameBoard.getInstance().getGameBoardPlaying();

        if (gameBoardPlaying.isAttackMoveCmdRequired()) {
            ConsolePrinter.printFormat("Player %s need to move armies into your conquered country %s",
                                       getCurrentPlayer(false).getName(),
                                       gameBoardPlaying.getDefendingCountry().getName());
            return;
        }

        // check if its no attack
        if (args[0].toLowerCase().equals("-noattack") || args[0].toLowerCase().equals("noattack")) {
            endAttackPhase();
            return;
        }

        Country attackingCountry = GameBoard.getInstance()
                                            .getGameBoardMap()
                                            .getCountryFromName(args[0]);
        Country defendingCountry = GameBoard.getInstance()
                                            .getGameBoardMap()
                                            .getCountryFromName(args[1]);

        // need to check whether these countries are existent or not
        gameBoardPlaying.setAttackingCountry(attackingCountry);
        gameBoardPlaying.setDefendingCountry(defendingCountry);

        if (!isAttackPreconditionsValid(attackingCountry,
                                        defendingCountry)) {
            ConsolePrinter.printFormat("Invalid Input");
            return;
        }

        if (args[2].equals("allout") || args[2].equals("-allout")) {
            gameBoardPlaying.setAttackerNumDice(Math.min((attackingCountry.getArmyAmount()
                                                          - 1),
                                                         MAXIMUM_ARMY_IN_ONE_ATTACK));
            gameBoardPlaying.setAlloutFlag(true);
            if (!isAttackValid(attackingCountry,
                               gameBoardPlaying.getAttackerNumDice(),
                               gameBoardPlaying.getDefenderNumDice(),
                               gameBoardPlaying.isAlloutFlag())) {
                gameBoardPlaying.setAlloutFlag(false);
            } else {
                gameBoardPlaying.setAlloutFlag(true);
                simulateAttack(gameBoardPlaying);
            }
        } else {
            if (Parser.checkValidInputNumber(args[2])) {
                // saving the number of dice
                gameBoardPlaying.setAttackerNumDice(Integer.parseInt(args[2]));
            } else {
                ConsolePrinter.printFormat("Invalid Input");
            }
            // check if attack is valid
            if (!isAttackValid(attackingCountry,
                               gameBoardPlaying.getAttackerNumDice(),
                               gameBoardPlaying.getDefenderNumDice(),
                               gameBoardPlaying.isAlloutFlag())) {
            }
        }
    }

    /**
     * it simulates the attack command for -allout option
     *
     * @param gameBoardPlaying the singleton class holding playing data
     */
    private void simulateAttack(GameBoardPlaying gameBoardPlaying) {
        while (gameBoardPlaying.isAlloutFlag()) {
            gameBoardPlaying.setAttackerNumDice(Math.min((gameBoardPlaying.getAttackingCountry()
                                                                          .getArmyAmount()
                                                          - 1),
                                                         MAXIMUM_ARMY_IN_ONE_ATTACK));
            int defender_dice = Math.min(gameBoardPlaying.getDefendingCountry()
                                                         .getArmyAmount(),
                                         2);
            if (gameBoardPlaying.isAttackMoveCmdRequired()) {
                gameBoardPlaying.setAlloutFlag(false);
            } else if (isAttackValid(gameBoardPlaying.getAttackingCountry(),
                                     gameBoardPlaying.getAttackerNumDice(),
                                     gameBoardPlaying.getDefenderNumDice(),
                                     gameBoardPlaying.isAlloutFlag())) {
                handleDefendCommand(new String[] { Integer.toString(defender_dice) });
            } else {
                gameBoardPlaying.setAlloutFlag(false);
                gameBoardPlaying.reset();
                ConsolePrinter.printFormat("The attack has ended as no other move is possible.");
            }
        }
    }

    /**
     * it handles the defend command and executes the entire attack and calls the dice roll method.
     *
     * @param args [0] number of dice that defender rolls
     *
     */
    public void handleDefendCommand(String[] args) {
        GameBoardPlaying gameBoardPlaying = GameBoard.getInstance().getGameBoardPlaying();
        Country defendingCountry = gameBoardPlaying.getDefendingCountry();
        Country attackingCountry = gameBoardPlaying.getAttackingCountry();
        int attackerNumDice = gameBoardPlaying.getAttackerNumDice();

        if (gameBoardPlaying.isAttackMoveCmdRequired()) {
            ConsolePrinter.printFormat("Player %s need to move armies into your conquered country %s",
                                       getCurrentPlayer(false).getName(),
                                       defendingCountry
                                                       .getName());
            return;
        }
        if (!Parser.checkValidInputNumber(args[0])) {
            ConsolePrinter.printFormat("Invalid Input");
            return;
        }

        gameBoardPlaying.setDefenderNumDice(Integer.parseInt(args[0]));
        int defenderNumDice = gameBoardPlaying.getDefenderNumDice();

        if (attackingCountry == null
            || defendingCountry == null) {
            ConsolePrinter.printFormat("You cannot use defend command without attack command.");
            return;
        }

        if (!isAttackValid(attackingCountry,
                           attackerNumDice,
                           defenderNumDice,
                           gameBoardPlaying.isAlloutFlag())) {
            ConsolePrinter.printFormat("Defend command not allowed as attack is invalid");
            return;
        } else if (defenderNumDice > 2) {
            ConsolePrinter.printFormat("You can only defend with a maximum of 2 armies at a time");
            return;
        } else if (defenderNumDice > defendingCountry.getArmyAmount()) {
            ConsolePrinter.printFormat("Error. Number of dice rolls should be less than or equal to the number of armies in the defending country but atmost 2.");
            return;
        }

        Player currentPlayer = getCurrentPlayer(false);
        currentPlayer.attack(attackingCountry,
                             defendingCountry,
                             attackerNumDice,
                             defenderNumDice);

        attackResult(defendingCountry, attackingCountry);
    }

    /**
     * it handles the attack move command and moves the army from attacking country to defending country
     * after the attacker has won the defending country.
     *
     * @param args [0] number of armies to move from attacking country to defending country
     *
     */
    public void handleAttackMoveCommand(String[] args) {
        GameBoardPlaying gameBoardPlaying = GameBoard.getInstance().getGameBoardPlaying();
        if (gameBoardPlaying.isAttackMoveCmdRequired()) {
            if (Parser.checkValidInputNumber(args[0])) {
                int army_to_be_moved = Integer.parseInt(args[0]);
                Country attackingCountry = gameBoardPlaying.getAttackingCountry();
                int attackerNumDice = gameBoardPlaying.getAttackerNumDice();
                if (army_to_be_moved < attackerNumDice
                    && attackingCountry.getArmyAmount()
                       - 1 > army_to_be_moved) {
                    ConsolePrinter.printFormat("You need to move atleast %s army to the conquered country.",
                                               attackerNumDice);
                } else if (attackingCountry.getArmyAmount()
                           - 1 < army_to_be_moved) {
                    ConsolePrinter.printFormat("You only have %s army available to move. You cannot move more armies than what you have.",
                                               attackerNumDice);
                } else {
                    Player currentPlayer = getCurrentPlayer(false);
                    currentPlayer.attackMove(attackingCountry,
                                             gameBoardPlaying.getDefendingCountry(),
                                             army_to_be_moved);
                    // attackingCountry.moveArmies(defendingCountry, army_to_be_moved);
                    // reinitialize variables to null
                    gameBoardPlaying.reset();
                    gameBoardPlaying.setAlloutFlag(false);
                    gameBoardPlaying.setAttackMoveCmdRequired(false);

                    if (currentPlayer.furtherAttackPossible()) {
                        ConsolePrinter.printFormat("The attack has ended. You can continue to attack other countries or type attack -noattack to end attack phase.");
                    } else {
                        ConsolePrinter.printFormat("No other attack is possible from any country.");
                        endAttackPhase();
                    }
                }

            } else {
                ConsolePrinter.printFormat("Invalid Input");
            }
        }
    }

    /**
     * it checks whether the game has ended or not.
     *
     * @return boolean if game has ended, then true , else false
     *
     */
    private boolean isGameEnded() {
        // check whether this player has won the game
        Player currentPlayer = getCurrentPlayer(false);
        boolean gameEnded = true;
        ArrayList<Country> countries = GameBoard.getInstance()
                                                .getGameBoardMap()
                                                .getCountries();
        for (Country country : countries) {
            if (country.getConquerer() != currentPlayer) {
                gameEnded = false;
                break;
            }
        }

        return gameEnded;
    }

    /**
     * check if attack pre-conditions valid. The conditions are:
     *
     * valid attacking country and defending country
     *
     * attacking country belong to attacker
     *
     * defending country does not belong to attacker
     *
     * attacking country and defending country is neighbor
     *
     * 
     * @param attackingCountry attacking country
     * @param defendingCountry defending country
     * 
     * @return is valid or not
     */
    private boolean isAttackPreconditionsValid(Country attackingCountry, Country defendingCountry) {
        Player currentPlayer = getCurrentPlayer(false);

        if (attackingCountry == null
            || defendingCountry == null) {
            ConsolePrinter.printFormat("The countries you have entered is non-existent");
            return false;
        }

        // check if attacking country belongs to the current player
        if (!attackingCountry.getConquerer()
                             .equals(currentPlayer)) {
            ConsolePrinter.printFormat("The country %s does not belong to %s",
                                       attackingCountry.getName(),
                                       currentPlayer.getName());
            return false;
        }

        // check if the defending country does not belong to the current player
        if (defendingCountry.getConquerer().equals(currentPlayer)) {
            ConsolePrinter.printFormat("The country %s belongs to %s. You cannot attack your own country.",
                                       defendingCountry.getName(),
                                       currentPlayer.getName());
            return false;
        }

        // the countries have to be neighbours
        if (!attackingCountry.isNeighboringCountries(defendingCountry)) {
            ConsolePrinter.printFormat("The countries %s and %s are not neighbouring countries. You can only attack neighbouring countries.",
                                       attackingCountry.getName(),
                                       defendingCountry.getName());
            return false;
        }

        return true;
    }

    /**
     *
     * it returns whether the attack is valid (true) or not (false)
     *
     * @return boolean it checks whether attack is valid or not and returns true or false based on that.
     * @param attackingCountry attacking country
     * @param attackerNumDice  attacker number of dice
     * @param defenderNumDice  defender number of dice
     * @param alloutFlag       is all out
     */
    boolean isAttackValid(Country attackingCountry,
                          int attackerNumDice,
                          int defenderNumDice,
                          boolean alloutFlag) {
        // check if you have more than 2 army in attacking country
        if (attackingCountry.getArmyAmount() < MINIMUM_ARMY_TO_ATTACK) {
            ConsolePrinter.printFormat("The country %s has less than 2 armies. You need at least 2 armies to attack a country.",
                                       attackingCountry.getName());
            return false;
        }
        // check if the player has more armies than the numDice. numDice can be at most 3.
        else if ((attackingCountry.getArmyAmount()
                  - 1) < attackerNumDice) {
            ConsolePrinter.printFormat("The number of armies available to attack are less than %s",
                                       attackerNumDice);
            return false;
        }
        // also numdice has to be less than 3
        else if (attackerNumDice > MAXIMUM_ARMY_IN_ONE_ATTACK) {
            ConsolePrinter.printFormat("You can only attack with a maximum of %d armies at a time",
                                       MAXIMUM_ARMY_IN_ONE_ATTACK);
            return false;
        }

        // attack execution
        else {
            if (defenderNumDice == 0
                && !alloutFlag) {
                ConsolePrinter.printFormat("Enter defend command");
            }
            return true;
        }
    }

    /**
     * it prints the result of the attack.
     * 
     * @param defendingCountry attacking country
     * @param attackingCountry defending country
     */
    private void attackResult(Country defendingCountry, Country attackingCountry) {
        // now check if defender's armies left is 0, set conquerer as attacker
        Player attacker = attackingCountry.getConquerer();
        Player defender = defendingCountry.getConquerer();

        if (defendingCountry.getArmyAmount() == 0) {
            ConsolePrinter.printFormat("The attacker %s has conquered the country %s successfully. He has %s army available to move.",
                                       attacker.getName(),
                                       defendingCountry.getName(),
                                       attackingCountry.getArmyAmount()
                                                                   - 1);

            defendingCountry.setConquerer(attacker);

            // set attacker can be reward a card when attack phase end
            attacker.setPlayerBeAwardCard(true);

            // check if defender has any countries that he has conquered. If not, the player lost the game.
            if (defender.getConqueredCountries().isEmpty()) {
                defender.setCurrentPhase(GamePhase.LOST);
                // GameBoard.getInstance()
                // .getGameBoardPlayer()
                // .removePlayer(defendingCountry.getConquerer().getName());
            }

            // check if player has conquered entire continent
            if (isGameEnded()) {
                GameBoard.getInstance()
                         .getGameBoardPlaying()
                         .setAlloutFlag(false);
                GameBoard.getInstance()
                         .getGameBoardPlaying()
                         .reset();
                GameBoard.getInstance()
                         .getGameBoardPlaying()
                         .setAttackMoveCmdRequired(false);
                setEndOfGamePhase();
            } else {
                // move armies
                ConsolePrinter.printFormat("Player %s needs to move armies into your conquered country %s",
                                           attacker.getName(),
                                           defendingCountry.getName());
                GameBoard.getInstance()
                         .getGameBoardPlaying()
                         .setAttackMoveCmdRequired(true);
            }
        } else {

            if (!GameBoard.getInstance()
                          .getGameBoardPlaying()
                          .isAlloutFlag()) {
                // reinitialize variables to null
                GameBoard.getInstance()
                         .getGameBoardPlaying()
                         .reset();
            }
        }

        Player currentPlayer = getCurrentPlayer(false);

        if (!isGameEnded()
            && !currentPlayer.furtherAttackPossible()
            && !GameBoard.getInstance().getGameBoardPlaying()
                         .isAttackMoveCmdRequired()) {
            if (GameBoard.getInstance()
                         .getGameBoardPlaying()
                         .isAlloutFlag()) {
                isAttackValid(attackingCountry,
                              GameBoard.getInstance().getGameBoardPlaying().getAttackerNumDice(),
                              GameBoard.getInstance().getGameBoardPlaying().getDefenderNumDice(),
                              GameBoard.getInstance().getGameBoardPlaying().isAlloutFlag());
            }

            ConsolePrinter.printFormat("No other attack is possible from any country.");
            GameBoard.getInstance()
                     .getGameBoardPlaying()
                     .reset();
            GameBoard.getInstance()
                     .getGameBoardPlaying()
                     .setAlloutFlag(false);
            endAttackPhase();
        }
    }

    /**
     * it ends the attack phase and sets the current game phase to fortification
     */
    private void endAttackPhase() {
        GameBoard.getInstance()
                 .getGameBoardPlaying()
                 .reset();
        Player currentPlayer = getCurrentPlayer(false);
        ConsolePrinter.printFormat("End of attack Phase. Player %s has entered fortification phase",
                                   currentPlayer.getName());
        currentPlayer.setCurrentPhase(GamePhase.FORTIFICATION);
    }

    /**
     * it sets the game phase to end of game when a player has won the game
     */
    void setEndOfGamePhase() {
        Player currentPlayer = getCurrentPlayer(false);
        ConsolePrinter.printFormat("Congratulations, The player %s has won the game.", currentPlayer.getName());
        currentPlayer.setCurrentPhase(GamePhase.END_OF_GAME);
    }

    /**
     * build set a card
     *
     * @param args {@value #NUMBER_OF_CARD_IN_ONE_SET} number as position of card in a set, -none at the
     *             end to not exchange
     * @return a set of card
     */
    private CardSet buildCardSet(String[] args) {
        Player currentPlayer = getCurrentPlayer();

        if (currentPlayer.getCurrentPhase() != GamePhase.REINFORCEMENT
            && currentPlayer.getCurrentPhase() != GamePhase.WAITING_TO_TURN) {
            ConsolePrinter.printFormat("Cannot exchange cards in %s phase",
                                       currentPlayer.getCurrentPhase().toString());
            return null;
        }

        if (args.length > NUMBER_OF_CARD_IN_ONE_SET) {
            ConsolePrinter.printFormat("A set of card only have %d card", NUMBER_OF_CARD_IN_ONE_SET);
            return null;
        }

        Card[] cards = new Card[NUMBER_OF_CARD_IN_ONE_SET];

        for (int index = 0; index < NUMBER_OF_CARD_IN_ONE_SET; index++) {
            if (Parser.checkValidInputNumber(args[index])) {
                int cardPosition = Parser.parseWithDefault(args[index], 0);
                cards[index] = currentPlayer.getHoldingCard(cardPosition);

                if (cards[index].isExchanged()) {
                    ConsolePrinter.printFormat("Card(s) you want to trade is already exchanged for armies");
                    return null;
                }
            }
        }

        return new CardSet(cards[0], cards[1], cards[2]);
    }

    /**
     * handle exchange card command
     *
     * @param args {@value #NUMBER_OF_CARD_IN_ONE_SET} number as position of card in a set, -none at the
     *             end to not exchange
     */
    public void exchangeCard(String[] args) {
        Player currentPlayer = getCurrentPlayer();

        int paramLeftOver = args.length % NUMBER_OF_CARD_IN_ONE_SET;
        int size = args.length - paramLeftOver;

        ArrayList<CardSet> cardSets = new ArrayList<>();

        for (int index = 0; index < size; index = index + NUMBER_OF_CARD_IN_ONE_SET) {

            CardSet cardSet = buildCardSet(new String[] {
                                                          args[index],
                                                          args[index + 1],
                                                          args[index + 2]
            });

            cardSets.add(cardSet);
        }

        currentPlayer.exchangeCardSets(cardSets);
    }
}
