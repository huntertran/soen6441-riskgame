package soen6441riskgame.enums;

/**
 * enums for parts of the map
 */
public enum MapPart {
                     NAME("name"),
                     FILES("[files]"),
                     CONTINENTS("[continents]"),
                     COUNTRIES("[countries]"),
                     BORDERS("[borders]"),
                     NONE("");

    private String part;

    /**
     * private constructor
     * @param part part of the map
     */
    MapPart(final String part) {
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
    public static MapPart fromString(String part) {
        for (MapPart mapPart : values()) {
            if (mapPart.getPart().equals(part)) {
                return mapPart;
            }
        }

        return NONE;
    }
}