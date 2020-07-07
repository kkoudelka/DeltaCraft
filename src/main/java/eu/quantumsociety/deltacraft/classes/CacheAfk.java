package eu.quantumsociety.deltacraft.classes;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public class CacheAfk {

    private final UUID id;
    private final Location origin;
    private final Date start;

    public UUID getId() {
        return id;
    }

    public Location getOrigin() {
        return origin;
    }

    public Date getStart() {
        return start;
    }

    public CacheAfk(UUID id, Location origin, Date start) {
        this.id = id;
        this.origin = origin;
        this.start = start;
    }

    public CacheAfk(UUID id, Location origin) {
        this(id, origin, new Date());
    }

    public CacheAfk(Player p) {
        this(p.getUniqueId(), p.getLocation());
    }
}
