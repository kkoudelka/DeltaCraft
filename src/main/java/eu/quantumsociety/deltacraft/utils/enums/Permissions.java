package eu.quantumsociety.deltacraft.utils.enums;

public enum Permissions {
    SPECTATEUSE("spectate.use"),
    SPECTATEUNLIMITED("spectate.unlimited"),
    KELPFARMCREATE("kelp.create"),
    KELPFARMUSE("kelp.use"),
    KELPFARMSETAGE("kelp.setage"),
    KELPFARMREMOVE("kelp.remove"),

    HOMESET("home.set"),
    HOMEUSE("home.use"),
    HOMELISTSELF("home.list.self"),
    HOMELISTANYONE("home.list.all"),
    HOMEDELETE("home.delete");

    private final String path;

    Permissions(String path) {
        this.path = "delta." + path;
    }


    /**
     * @return the permissions path/name
     */
    public String getName() {
        return this.path;
    }

    public String getValue() {
        return this.path;
    }

    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum type should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
