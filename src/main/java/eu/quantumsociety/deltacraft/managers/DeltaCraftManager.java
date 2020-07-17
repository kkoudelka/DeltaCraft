package eu.quantumsociety.deltacraft.managers;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.managers.cache.AfkCacheManager;
import eu.quantumsociety.deltacraft.managers.cache.FakePlayerManager;
import eu.quantumsociety.deltacraft.managers.cache.KelpCacheManager;
import eu.quantumsociety.deltacraft.managers.cache.SpectateCacheManager;
import eu.quantumsociety.deltacraft.utils.enums.Settings;

public class DeltaCraftManager {
    private final DeltaCraft plugin;

    private boolean endAccess;

    private final KelpCacheManager kelpCacheManager;
    private final SpectateCacheManager spectateCacheManager;
    private final AfkCacheManager afkCacheManager;
    private final FakePlayerManager fakePlayerHelper;

    public DeltaCraftManager(DeltaCraft plugin) {
        this.plugin = plugin;

        this.endAccess = plugin.getConfig().getBoolean(Settings.END.getPath());

        this.kelpCacheManager = new KelpCacheManager(plugin);
        this.spectateCacheManager = new SpectateCacheManager(plugin);
        this.afkCacheManager = new AfkCacheManager(plugin);
        this.fakePlayerHelper = new FakePlayerManager(plugin);
    }

    public KelpCacheManager getKelpCacheManager() {
        return kelpCacheManager;
    }

    public SpectateCacheManager getSpectateCacheManager() {
        return spectateCacheManager;
    }

    public AfkCacheManager getAfkCacheManager() {
        return afkCacheManager;
    }

    public FakePlayerManager getFakePlayerManager() {
        return fakePlayerHelper;
    }

    /**
     * @return If player can enter end dimension
     */
    public boolean getEndAccess() {
        return endAccess;
    }
}
