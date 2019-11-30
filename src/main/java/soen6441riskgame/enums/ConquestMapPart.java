package soen6441riskgame.enums;

/**
 * enums for parts of the map
 */
public enum ConquestMapPart {
                             MAP("[Map]"),
                             CONTINENTS("[Continents]"),
                             TERRITORIES("[Territories]"),
                             NONE("");

    private String part;

    /**
     * private constructor
     * 
     * @param part part of the map
     */
    ConquestMapPart(final String part) {
        this.part = part;
    }

    /**
     * get the part in string
     *
     * @return part in string
     */
    public String getPart() {
        return part;
    }

    /**
     * convert the string part to enum
     *
     * @param part part in string
     * @return part in enum
     */
    public static ConquestMapPart fromString(String part) {
        for (ConquestMapPart mapPart : values()) {
            if (mapPart.getPart().equals(part)) {
                return mapPart;
            }
        }

        return NONE;
    }
}