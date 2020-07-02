package eu.quantumsociety.DeltaCraft.utils.enums;

public enum Permissions {
    USESPECTATE("spectate.use"),
    UNLIMITEDDISTANCE("spectate.unlimiteddistance");

    private String path;
    private final String prefix = "delta.";

    private Permissions(String path) {
        this.path = prefix + path;
    }

    public String getName() {
        return this.path;
    }
}
