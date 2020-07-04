package eu.quantumsociety.deltacraft.classes;

import org.bukkit.Location;

import java.util.UUID;

public class CacheRegion {

    public final String name;
    public final UUID ownerId;
    public final UUID worldUniqueId;

    private final double maxX;
    private final double maxY;
    private final double maxZ;

    private final double minX;
    private final double minY;
    private final double minZ;


    public CacheRegion(Location firstPoint, Location secondPoint,
                       String name, UUID ownerId) {
        this.worldUniqueId = firstPoint.getWorld().getUID();
        this.name = name;
        this.ownerId = ownerId;

        this.maxX = Math.max(firstPoint.getX(), secondPoint.getX());
        this.maxY = Math.max(firstPoint.getY(), secondPoint.getY());
        this.maxZ = Math.max(firstPoint.getZ(), secondPoint.getZ());

        this.minX = Math.min(firstPoint.getX(), secondPoint.getX());
        this.minY = Math.min(firstPoint.getY(), secondPoint.getY());
        this.minZ = Math.min(firstPoint.getZ(), secondPoint.getZ());
    }

    public boolean contains(Location loc) {
        return loc.getWorld().getUID().equals(worldUniqueId)
                && loc.getX() >= minX && loc.getX() <= maxX
                && loc.getY() >= minY && loc.getY() <= maxY
                && loc.getZ() >= minZ && loc.getZ() <= maxZ;
    }

}
