package eu.quantumsociety.DeltaCraft.utils.enums;

import com.google.common.collect.Maps;

import java.util.Map;

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