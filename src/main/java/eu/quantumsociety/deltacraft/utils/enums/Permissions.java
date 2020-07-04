package eu.quantumsociety.deltacraft.utils.enums;

public enum Permissions {
    USESPECTATE("spectate.use"),
    UNLIMITEDDISTANCE("spectate.unlimiteddistance"),
    KELPFARMCREATE("kelp.create"),
    KELPFARMUSE("kelp.use"),
    KELPFARMREMOVE("kelp.remove");

    private String path;
    private final String prefix = "delta.";

    private Permissions(String path) {
        this.path = prefix + path;
    }

    public String getName() {
        return this.path;
    }
}
