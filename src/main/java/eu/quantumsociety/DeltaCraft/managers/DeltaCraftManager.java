package eu.quantumsociety.DeltaCraft.managers;

import eu.quantumsociety.DeltaCraft.DeltaCraft;
import eu.quantumsociety.DeltaCraft.classes.CachePlayer;
import eu.quantumsociety.DeltaCraft.classes.CacheRegion;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class DeltaCraftManager {
    private final DeltaCraft plugin;

    private HashMap<UUID, CachePlayer> spectateCache;
    private HashMap<UUID, CacheRegion> kelpCache;

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

    public CacheRegion addCacheRegion(UUID id, Location one, Location two) {
        CacheRegion region = new CacheRegion(one, two);

        return this.addCacheRegion(id, region);
    }

    public CacheRegion addCacheRegion(UUID id, CacheRegion region) {
        this.plugin.debugMsg("Adding region: " + id);

        this.kelpCache.put(id, region);
        return this.getCacheRegion(id);
    }

    public CacheRegion getCacheRegion(UUID find) {
        return this.kelpCache.get(find);
    }
}
