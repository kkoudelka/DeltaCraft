package eu.quantumsociety.DeltaCraft.managers;

import eu.quantumsociety.DeltaCraft.DeltaCraft;
import eu.quantumsociety.DeltaCraft.classes.CachePlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class DeltaCraftManager {
    private final DeltaCraft plugin;

    private HashMap<UUID, CachePlayer> spectateCache;

    public DeltaCraftManager(DeltaCraft plugin) {
        this.plugin = plugin;
        this.spectateCache = new HashMap<UUID, CachePlayer>();
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

    public boolean isInCache(UUID uuid) {
        return this.spectateCache.containsKey(uuid);
    }

    public CachePlayer removeCachePlayer(UUID find) {
        this.plugin.debugMsg("Removing player: " + find);
        return this.spectateCache.remove(find);
    }
}
