package eu.quantumsociety.deltacraft.classes;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CachePlayer {
    private UUID id;
    private Location originalLocation;
    private GameMode prevGameMode;
    private Player player;
    private Vector originalVelocity;
    private float fallDistance;

    public CachePlayer(UUID id, Location originalLocation, GameMode prevGameMode, Vector originalVelocity, float fallDistance) {
        this.id = id;
        this.originalLocation = originalLocation;
        this.prevGameMode = prevGameMode;
        this.player = Bukkit.getPlayer(id);
        this.originalVelocity = originalVelocity;
        this.fallDistance = fallDistance;
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

    @Nullable
    public Player getPlayer() {
        return player;
    }
}
