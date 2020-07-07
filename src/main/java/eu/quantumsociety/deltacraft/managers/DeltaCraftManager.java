package eu.quantumsociety.deltacraft.managers;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CacheAfk;
import eu.quantumsociety.deltacraft.classes.CachePlayer;
import eu.quantumsociety.deltacraft.managers.cache.KelpCacheManager;
import eu.quantumsociety.deltacraft.managers.cache.SpectateCacheManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class DeltaCraftManager {
    private final DeltaCraft plugin;

    private HashMap<UUID, CacheAfk> afkCache;

    private final KelpCacheManager kelpCacheManager;
    private final SpectateCacheManager spectateCacheManager;

    public DeltaCraftManager(DeltaCraft plugin) {
        this.plugin = plugin;

        this.kelpCacheManager = new KelpCacheManager(plugin);
        this.spectateCacheManager = new SpectateCacheManager(plugin);

        this.afkCache = new HashMap<>();
    }

    public KelpCacheManager getKelpCacheManager() {
        return kelpCacheManager;
    }

    public SpectateCacheManager getSpectateCacheManager() {
        return spectateCacheManager;
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
