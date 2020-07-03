package eu.quantumsociety.DeltaCraft.classes;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.UUID;

public class PlayerHome {
    public UUID playerId;
    public String homeName;
    public Location location;

    public PlayerHome(UUID playerId, String homeName, Location location) {
        this.playerId = playerId;
        this.homeName = homeName;
        this.location = location;
    }
}
