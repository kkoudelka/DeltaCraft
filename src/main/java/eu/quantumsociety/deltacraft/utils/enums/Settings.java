package eu.quantumsociety.deltacraft.utils.enums;

public enum Settings {
    DEBUG("system.debug"),
    SPECTATEMAXDISTANCE("settings.spectate.maxdistance"),
    HOMEMAXHOMES("settings.home.maxhomes"),
    KELPMAXDISTANCE("settings.kelp.maxdistance");

    private String path;

    Settings(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    @Override
    public String toString() {
        return this.getPath();
    }
}
