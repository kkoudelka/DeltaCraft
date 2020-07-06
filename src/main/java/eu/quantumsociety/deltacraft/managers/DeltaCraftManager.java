package eu.quantumsociety.deltacraft.managers;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CachePlayer;
import eu.quantumsociety.deltacraft.classes.CacheRegion;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class DeltaCraftManager {
    private final DeltaCraft plugin;

    private HashMap<UUID, CachePlayer> spectateCache;
    private HashMap<String, CacheRegion> kelpCache;

    public DeltaCraftManager(DeltaCraft plugin) {
        this.plugin = plugin;
        this.spectateCache = new HashMap<>();
        this.kelpCache = new HashMap<>();
    }

    public void addCachePlayer(Player player, Location origin, GameMode gm) {
        this.addCachePlayer(new CachePlayer(player,
                origin,
                gm
        ));
    }

    public void addCachePlayer(CachePlayer toAdd) {
        UUID id = toAdd.getId();
        this.plugin.debugMsg("Adding player: " + id);

        this.spectateCache.put(id, toAdd);
        this.getCachePlayer(id);
    }

    public CachePlayer getCachePlayer(UUID find) {
        return this.spectateCache.get(find);
    }

    public boolean isPlayerInCache(UUID uuid) {
        return this.spectateCache.containsKey(uuid);
    }

    public void removeCachePlayer(UUID find) {
        this.plugin.debugMsg("Removing player: " + find);
        this.spectateCache.remove(find);
    }

    public void addCacheRegion(Location one, Location two,
                               String name, UUID ownerId) {
        CacheRegion region = new CacheRegion(one, two, name, ownerId);

        this.addCacheRegion(name, region);
    }

    public void addCacheRegion(String name, CacheRegion region) {
        this.plugin.debugMsg("Adding region: " + name);

        this.kelpCache.put(name, region);
        this.getCacheRegion(name);
    }

    public void getCacheRegion(String find) {
        this.kelpCache.get(find);
    }

    public CacheRegion getCacheRegion(Location l) {
        for (CacheRegion region : kelpCache.values()) {
            if (region.contains(l)) {
                return region;
            }
        }
        return null;
    }

    public void removeCacheRegion(String name) {
        this.plugin.debugMsg("Removing region: " + name);
        this.kelpCache.remove(name);
    }

    public void loadSpectators(HashMap<UUID, CachePlayer> players) {
        this.spectateCache = players;
    }

    public void loadRegions(HashMap<String, CacheRegion> regions) {
        this.kelpCache = regions;
    }


    public int getKelpCacheSize() {
        return this.kelpCache.size();
    }

    public Collection<CacheRegion> getRegions() {
        return this.kelpCache.values();
    }
}
