package eu.quantumsociety.deltacraft.managers;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CacheAfk;
import eu.quantumsociety.deltacraft.classes.CachePlayer;
import eu.quantumsociety.deltacraft.classes.CacheRegion;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public void addKelpFarm(Location one, Location two,
                            String name, UUID ownerId) {
        CacheRegion region = new CacheRegion(one, two, name, ownerId);

        this.addKelpFarm(name, region);
    }

    public void addKelpFarm(String name, CacheRegion region) {
        this.plugin.debugMsg("Adding region: " + name);

        this.kelpCache.put(name, region);
        this.getKelpFarm(name);
    }

    public void getKelpFarm(String find) {
        this.kelpCache.get(find);
    }

    public CacheRegion getKelpFarm(Location l) {
        for (CacheRegion region : kelpCache.values()) {
            if (region.contains(l)) {
                return region;
            }
        }
        return null;
    }

    public boolean isInKelpFarm(Location l) {
        return this.getKelpFarm(l) != null;
    }

    public void removeKelpFarm(String name) {
        this.plugin.debugMsg("Removing region: " + name);
        this.kelpCache.remove(name);
    }

    public void loadSpectators(HashMap<UUID, CachePlayer> players) {
        this.spectateCache = players;
    }

    public void loadKelpFarms(HashMap<String, CacheRegion> regions) {
        this.kelpCache = regions;
    }


    public int getKelpCacheSize() {
        return this.kelpCache.size();
    }

    public Collection<CacheRegion> getKelpFarms() {
        return this.kelpCache.values();
    }

    public List<CacheRegion> getKelpFarms(Player p) {
        return this.getKelpFarms(p.getUniqueId());
    }

    public List<CacheRegion> getKelpFarms(UUID ownerId) {
        Collection<CacheRegion> all = this.getKelpFarms();

        Stream<CacheRegion> r = all.stream()
                .filter(x -> x.ownerId.equals(ownerId));

        return r.collect(Collectors.toList());
    }

    public List<String> getKelpFarmNames(UUID ownerId) {
        Collection<CacheRegion> farms = this.getKelpFarms(ownerId);

        Stream<String> res = farms.stream()
                .map(x -> x.name);

        return res.collect(Collectors.toList());
    }

    public int getKelpFarmCount(Player p) {
        return this.getKelpFarmCount(p.getUniqueId());
    }

    public int getKelpFarmCount(UUID ownerId) {
        Collection<CacheRegion> all = this.getKelpFarms();

        long count = all.stream()
                .filter(x -> x.ownerId.equals(ownerId))
                .count();

        return (int) count;
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
