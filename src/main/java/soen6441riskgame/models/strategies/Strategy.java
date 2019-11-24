package soen6441riskgame.models.strategies;

import java.util.ArrayList;

import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;

public interface Strategy {
    void execute(Player player);

    void reinforce(Player player, Country countryToReinforce);

    ArrayList<Country> attack(Player player, Country attackingCountry);

    void fortify(Country fromCountry, Country toCountry);
}