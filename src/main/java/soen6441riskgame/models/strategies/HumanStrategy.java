package soen6441riskgame.models.strategies;

import java.util.ArrayList;

import soen6441riskgame.enums.StrategyName;
import soen6441riskgame.models.Country;
import soen6441riskgame.models.Player;

public class HumanStrategy implements Strategy {
    /**
     * get strategy name
     * 
     * @return the name of the strategy as enum
     */
    @Override
    public StrategyName getName() {
        return StrategyName.HUMAN;
    }

    @Override
    public void reinforce(Player player, Country countryToReinforce) {
        // TODO Auto-generated method stub

    }

    @Override
    public ArrayList<Country> attack(Player player, Country attackingCountry) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void fortify(Country fromCountry, Country toCountry) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playTurn(Player player) {
        // TODO Auto-generated method stub

    }

}