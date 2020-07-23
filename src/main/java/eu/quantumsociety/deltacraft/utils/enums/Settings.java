package eu.quantumsociety.deltacraft.utils.enums;

public enum Settings {
    DEBUG("system.debug", "Use debug mode", "BOOL"),
    END("settings.end", "Allow access to end dimension", "BOOL"),
    SPECTATEMAXDISTANCE("settings.spectate.maxdistance", "Maximum distance can player travel in spectator mode from starting point", "INT"),
    HOMEMAXHOMES("settings.home.maxhomes", "Maximum homes player can create", "INT"),
    KELPDEBUG("settings.kelp.debug", "Use debug mode in kelp farms", "BOOL"),
    KELPMAXFARMS("settings.kelp.maxfarms", "Maximum kelp farms player can create", "INT"),
    KELPMAXDISTANCE("settings.kelp.maxdistance", "Maximum distance of 2 points of kelp farm", "INT");

    private final String path;
    private final String description;
    private final String type;

    Settings(String path, String description, String type) {
        this.path = path;
        this.description = description;
        this.type = type;
    }

    public String getPath() {
        return this.path;
    }

    // Cannot use getDescription because of JetBrains already using it somewhere
    public String getDesc() {
        return description;
    }

    public String getType() {
        return "[" + type + "]";
    }

    @Override
    public String toString() {
        return this.getPath();
    }
}
