package eu.quantumsociety.deltacraft.classes;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CachePlayer {
    private UUID id;
    private Location originalLocation;
    private GameMode prevGameMode;

    public CachePlayer(Player p, Location originalLocation, GameMode prevGameMode) {
        this(p.getUniqueId(), originalLocation, prevGameMode);
    }

    public CachePlayer(UUID id, Location originalLocation, GameMode prevGameMode) {
        this.id = id;
        this.originalLocation = originalLocation;
        this.prevGameMode = prevGameMode;
    }

    public Location getOriginalLocation() {
        return this.originalLocation;
    }

    public UUID getId() {
        return id;
    }

    public GameMode getPrevGameMode() {
        return this.prevGameMode;
    }

}
