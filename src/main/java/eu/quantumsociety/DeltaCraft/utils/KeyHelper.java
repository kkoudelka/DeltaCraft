package eu.quantumsociety.DeltaCraft.utils;

import java.util.UUID;

public class KeyHelper {
    /**
     *
     * @param playerUid Players UUID
     * @param module Plugins submodule
     * @param subkey Subkey
     * @return
     */
    public static String getPlayerKey(UUID playerUid, PluginSubmodule module, String subkey) {
        String key = "players." + playerUid.toString() + "." + module.toString() + "." + subkey;
        return key;
    }
}

