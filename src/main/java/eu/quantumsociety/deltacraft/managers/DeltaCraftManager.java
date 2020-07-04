package eu.quantumsociety.deltacraft.managers;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CachePlayer;
import eu.quantumsociety.deltacraft.classes.CacheRegion;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class DeltaCraftManager {
    private final DeltaCraft plugin;

    private HashMap<UUID, CachePlayer> spectateCache;
    private HashMap<String, CacheRegion> kelpCache;

    public DeltaCraftManager(DeltaCraft plugin) {
        this.plugin = plugin;
        this.spectateCache = new HashMap<>();
        this.kelpCache = new HashMap<>();
    }

    public CachePlayer addCachePlayer(Player player, Location origin, GameMode gm) {

        return this.addCachePlayer(new CachePlayer(player,
                origin,
                gm,
                new Date()
        ));
    }

    public CachePlayer addCachePlayer(CachePlayer toAdd) {
        UUID id = toAdd.getPlayer().getUniqueId();
        this.plugin.debugMsg("Adding player: " + id);

        this.spectateCache.put(id, toAdd);
        return this.getCachePlayer(id);
    }

    public CachePlayer getCachePlayer(UUID find) {
        return this.spectateCache.get(find);
    }

    public boolean isPlayerInCache(UUID uuid) {
        return this.spectateCache.containsKey(uuid);
    }

    public CachePlayer removeCachePlayer(UUID find) {
        this.plugin.debugMsg("Removing player: " + find);
        return this.spectateCache.remove(find);
    }

    public CacheRegion addCacheRegion(Location one, Location two,
                                      String name, UUID ownerId) {
        CacheRegion region = new CacheRegion(one, two, name, ownerId);

        return this.addCacheRegion(name, region);
    }

    public CacheRegion addCacheRegion(String name, CacheRegion region) {
        this.plugin.debugMsg("Adding region: " + name);

        this.kelpCache.put(name, region);
        return this.getCacheRegion(name);
    }

    public CacheRegion getCacheRegion(String find) {
        return this.kelpCache.get(find);
    }

    public CacheRegion getCacheRegion(Location l) {
        for (CacheRegion region : kelpCache.values()) {
            if (region.contains(l)) {
                return region;
            }
        }
        return null;
    }
}
