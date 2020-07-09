package eu.quantumsociety.deltacraft.classes;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

public class CachePlayer {
    private UUID id;
    private Location originalLocation;
    private GameMode prevGameMode;
    private Player player;
    private Vector originalVelocity;
    private float fallDistance;

    public CachePlayer(Player p, Location originalLocation, GameMode prevGameMode, Vector originalVelocity, float fallDistance) {
        this.id = p.getUniqueId();
        this.originalLocation = originalLocation;
        this.prevGameMode = prevGameMode;
        this.player = p;
        this.originalVelocity = originalVelocity;
        this.fallDistance = fallDistance;
    }

    public CachePlayer(UUID id, Location originalLocation, GameMode prevGameMode, Vector originalVelocity, float fallDistance) {
        this(Bukkit.getPlayer(id), originalLocation, prevGameMode, originalVelocity, fallDistance);
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

    public Player getPlayer() {
        return player;
    }
}
