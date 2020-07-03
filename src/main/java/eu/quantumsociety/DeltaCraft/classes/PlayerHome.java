package eu.quantumsociety.DeltaCraft.classes;

import org.bukkit.Location;

import java.util.UUID;

public class PlayerHome {
    private UUID playerId;
    private String homeName;
    private Location location;

    public PlayerHome(UUID playerId, String homeName, Location location) {
        this.playerId = playerId;
        this.homeName = homeName;
        this.location = location;
    }
}
