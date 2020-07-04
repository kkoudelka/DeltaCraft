package eu.quantumsociety.DeltaCraft.utils.enums;

public enum Settings {
    DEBUG("system.debug"),
    MAXDISTANCE("settings.spectate.maxdistance");

    private String path;

    private Settings(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
