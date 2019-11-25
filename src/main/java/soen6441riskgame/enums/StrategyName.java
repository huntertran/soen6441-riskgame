package soen6441riskgame.enums;

import soen6441riskgame.models.strategies.AggressiveStrategy;
import soen6441riskgame.models.strategies.BenevolentStrategy;
import soen6441riskgame.models.strategies.CheaterStrategy;
import soen6441riskgame.models.strategies.HumanStrategy;
import soen6441riskgame.models.strategies.RandomStrategy;
import soen6441riskgame.models.strategies.Strategy;

public enum StrategyName {
                          AGGRESSIVE("aggressive"),
                          BENEVOLENT("benevolent"),
                          RANDOM("random"),
                          CHEATER("cheater"),
                          HUMAN("human"),
                          INVALID("");

    /**
     * Strategy name
     */
    private String name;

    /**
     * constructor
     * 
     * @param name strategy name
     */
    StrategyName(final String name) {
        this.name = name;
    }

    /**
     * get name of the strategy enum
     * 
     * @return name of the strategy enum
     */
    public String getName() {
        return name;
    }

    /**
     * convert the string arg to enum
     *
     * @param argumentString the arg string
     * @return the arg converted to enum
     */
    public static Strategy fromString(String argumentString) {
        for (StrategyName argument : values()) {
            if (argument.getName().equalsIgnoreCase(argumentString)) {
                switch (argument) {
                    case AGGRESSIVE: {
                        return new AggressiveStrategy();
                    }
                    case BENEVOLENT: {
                        return new BenevolentStrategy();
                    }
                    case RANDOM: {
                        return new RandomStrategy();
                    }
                    case CHEATER: {
                        return new CheaterStrategy();
                    }
                    case HUMAN: {
                        return new HumanStrategy();
                    }
                    default: {
                        break;
                    }
                }
            }
        }

        return null;
    }
}