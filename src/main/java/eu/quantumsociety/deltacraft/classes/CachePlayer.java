package eu.quantumsociety.deltacraft.classes;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CachePlayer {
    private UUID id;
    private Location originalLocation;
    private GameMode prevGameMode;
    private Player player;

    public CachePlayer(Player p, Location originalLocation, GameMode prevGameMode) {
        this.id = p.getUniqueId();
        this.originalLocation = originalLocation;
        this.prevGameMode = prevGameMode;
        this.player = p;
    }

    public CachePlayer(UUID id, Location originalLocation, GameMode prevGameMode) {
        this(Bukkit.getPlayer(id), originalLocation, prevGameMode);
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

    public Player getPlayer() { return player; }
}
