package eu.quantumsociety.DeltaCraft.utils;

import java.util.UUID;

public class KeyHelper {

    private final UUID pUid;
    private final PluginSubmodule submodule;

    public KeyHelper(UUID pUid, PluginSubmodule submodule) {
        this.pUid = pUid;
        this.submodule = submodule;
    }

    /**
     * Returns players key with submodule and subkey <i>players.[UUID].[subkey]</i>
     *
     * @param subkey Subkey at the end of the key
     * @return String
     */
    public String get(String subkey) {
        return this.getPlayerKey() + "." + subkey;
    }

    /**
     * Returns players key with submodule <i>players.[UUID].</i>
     * @return String
     */
    public String getPlayerKey() {
        String key = "players." + this.pUid.toString() + "." + this.submodule.toString();
        return key;
    }
}

