package eu.quantumsociety.DeltaCraft.utils;

import com.google.common.collect.Maps;

import java.util.Map;

public enum PluginSubmodule {
    HOME("home"),

    SPECTATE("spectate");

    private final String value;
    private static final Map<String, PluginSubmodule> BY_ID = Maps.newHashMap();

    PluginSubmodule(final String value) {
        this.value = value;
    }


    static {
        for (PluginSubmodule submodule : values()) {
            BY_ID.put(submodule.value, submodule);
        }
    }
}