package eu.quantumsociety.deltacraft.utils.enums;

public enum PluginSubmodule {
    HOME("home"),

    SPECTATE("spectate");

    private final String value;

    PluginSubmodule(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}