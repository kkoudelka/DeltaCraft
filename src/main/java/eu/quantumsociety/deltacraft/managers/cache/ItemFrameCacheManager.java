package eu.quantumsociety.deltacraft.managers.cache;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.managers.templates.CacheManager;

import java.util.UUID;

public class ItemFrameCacheManager extends CacheManager<UUID, Boolean> {
    public ItemFrameCacheManager(DeltaCraft plugin) {
        super(plugin, false);
    }
}
