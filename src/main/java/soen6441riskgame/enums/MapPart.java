package soen6441riskgame.enums;

public enum MapPart {
    NAME("name"), FILES("[files]"), CONTINENTS("[continents]"), COUNTRIES("[countries]"), BORDERS("[borders]"),
    NONE("");

    private String part;

    MapPart(final String part) {
        this.part = part;
    }

    public String getPart() {
        return part;
    }

    public static MapPart fromString(String part) {
        for (MapPart mapPart : values()) {
            if (mapPart.getPart().equals(part)) {
                return mapPart;
            }
        }

        return NONE;
    }
}