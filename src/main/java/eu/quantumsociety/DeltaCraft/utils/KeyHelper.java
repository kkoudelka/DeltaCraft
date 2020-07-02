package eu.quantumsociety.DeltaCraft.utils;

import eu.quantumsociety.DeltaCraft.utils.enums.PluginSubmodule;

import java.util.UUID;

public class KeyHelper {

    private final UUID pUid;
    private final PluginSubmodule submodule;

    public KeyHelper(UUID pUid, PluginSubmodule submodule) {
        this.pUid = pUid;
        this.submodule = submodule;
    }

    /**
     * Returns players key with submodule and subkeys <i>players.[UUID].[subkey].[...?]...</i>
     * @param subkeys Subkeys
     * @return String
     */
    public String get(String... subkeys) {
        return this.getPlayerKey() + "." + String.join(".", subkeys);
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

