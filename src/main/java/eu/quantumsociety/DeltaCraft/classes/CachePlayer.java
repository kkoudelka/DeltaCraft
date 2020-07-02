package eu.quantumsociety.DeltaCraft.classes;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;

public class CachePlayer {
    private Player p;
    private Location originalLocation;
    private GameMode prevGameMode;
    private Date changed;

    public CachePlayer(Player p, Location originalLocation, GameMode prevGameMode, Date changed) {
        this.p = p;
        this.originalLocation = originalLocation;
        this.prevGameMode = prevGameMode;
        this.changed = changed;
    }

    public Player getPlayer() {
        return this.p;
    }

    public Location getOriginalLocation() {
        return this.originalLocation;
    }

    public GameMode getPrevGameMode() {
        return this.prevGameMode;
    }

    public Date getChanged() {
        return this.changed;
    }
}
