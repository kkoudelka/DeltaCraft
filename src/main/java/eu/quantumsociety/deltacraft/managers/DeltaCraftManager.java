package eu.quantumsociety.deltacraft.managers;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CacheAfk;
import eu.quantumsociety.deltacraft.classes.CachePlayer;
import eu.quantumsociety.deltacraft.managers.cache.KelpCacheManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class DeltaCraftManager {
    private final DeltaCraft plugin;

    private HashMap<UUID, CachePlayer> spectateCache;
    private HashMap<UUID, CacheAfk> afkCache;

    private final KelpCacheManager kelpCacheManager;

    public DeltaCraftManager(DeltaCraft plugin) {
        this.plugin = plugin;

        this.kelpCacheManager = new KelpCacheManager(plugin);

        this.spectateCache = new HashMap<>();
        this.afkCache = new HashMap<>();
    }

    public KelpCacheManager getKelpCacheManager() {
        return kelpCacheManager;
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


    public void loadSpectators(HashMap<UUID, CachePlayer> players) {
        this.spectateCache = players;
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
