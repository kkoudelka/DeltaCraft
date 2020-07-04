package eu.quantumsociety.deltacraft.utils;

import java.util.UUID;

public class KeyHelper {

    private final String pUid;
    private final String prefix;

    public KeyHelper(String id, String prefix) {
        this.pUid = id;
        this.prefix = prefix;
    }

    public KeyHelper(UUID id, String prefix) {
        this(id.toString(), prefix);
    }

    public KeyHelper(UUID pUid) {
        this(pUid, "players");
    }

    /**
     * Returns players key with submodule and subkeys <i>players.[UUID].[subkey].[...?]...</i>
     *
     * @param subkeys Subkeys
     * @return String
     */
    public String get(String... subkeys) {
        return this.getPlayerKey() + "." + String.join(".", subkeys);
    }

    /**
     * Returns players key <i>players.[UUID].</i>
     *
     * @return String
     */
    public String getPlayerKey() {
        String key = prefix + "." + this.pUid;
        return key;
    }
}

