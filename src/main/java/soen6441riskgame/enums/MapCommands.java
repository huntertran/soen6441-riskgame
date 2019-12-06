package soen6441riskgame.enums;

import soen6441riskgame.models.Continent;

public enum MapCommands {
                         EDIT_CONTINENT("editcontinent", Continent.class, Integer.class);

    MapCommands(String commandText,
                Class<?>... commandArguments) {
    }
}