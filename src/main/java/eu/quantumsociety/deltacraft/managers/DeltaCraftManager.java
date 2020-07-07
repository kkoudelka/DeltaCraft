package eu.quantumsociety.deltacraft.managers;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CacheAfk;
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
    private HashMap<UUID, CacheAfk> afkCache;

    public DeltaCraftManager(DeltaCraft plugin) {
        this.plugin = plugin;
        this.spectateCache = new HashMap<>();
        this.kelpCache = new HashMap<>();
        this.afkCache = new HashMap<>();
    }

    public void addSpectatePlayer(Player player, Location origin, GameMode gm) {
        this.addSpectatePlayer(new CachePlayer(player,
                origin,
                gm
        ));
    }

    public void addSpectatePlayer(CachePlayer toAdd) {
        UUID id = toAdd.getId();
        this.plugin.debugMsg("Adding player: " + id);

        this.spectateCache.put(id, toAdd);
        this.getSpectatePlayer(id);
    }

    public CachePlayer getSpectatePlayer(UUID find) {
        return this.spectateCache.get(find);
    }

    public boolean isPlayerSpectating(UUID uuid) {
        return this.spectateCache.containsKey(uuid);
    }

    public void removeSpectatePlayer(UUID find) {
        this.plugin.debugMsg("Removing player: " + find);
        this.spectateCache.remove(find);
    }

    public void addKelpRegion(Location one, Location two,
                              String name, UUID ownerId) {
        CacheRegion region = new CacheRegion(one, two, name, ownerId);

        this.addKelpRegion(name, region);
    }

    public void addKelpRegion(String name, CacheRegion region) {
        this.plugin.debugMsg("Adding region: " + name);

        this.kelpCache.put(name, region);
        this.getKelpRegion(name);
    }

    public void getKelpRegion(String find) {
        this.kelpCache.get(find);
    }

    public CacheRegion getKelpRegion(Location l) {
        for (CacheRegion region : kelpCache.values()) {
            if (region.contains(l)) {
                return region;
            }
        }
        return null;
    }

    public void removeKelpRegion(String name) {
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

    public CacheAfk addAfkPlayer(Player player) {
        return this.addAfkPlayer(new CacheAfk(player));
    }

    public CacheAfk addAfkPlayer(CacheAfk toAdd) {
        UUID id = toAdd.getId();
        this.afkCache.put(id, toAdd);
        return this.getAfkPlayer(id);
    }

    public CacheAfk getAfkPlayer(UUID find) {
        return this.afkCache.get(find);
    }

    public boolean isPlayerAfk(UUID uuid) {
        return this.afkCache.containsKey(uuid);
    }

    public CacheAfk removeAfkPlayer(UUID find) {
        this.plugin.debugMsg("Removing player: " + find);
        return this.afkCache.remove(find);
    }

}
