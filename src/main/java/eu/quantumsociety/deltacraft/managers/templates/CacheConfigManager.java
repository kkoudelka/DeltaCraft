package eu.quantumsociety.deltacraft.managers.templates;

import eu.quantumsociety.deltacraft.DeltaCraft;

public abstract class CacheConfigManager<T extends CacheManager> extends ConfigManager {

    private final T manager;

    public CacheConfigManager(DeltaCraft plugin, String fileName, T manager) {
        super(plugin, fileName);
        this.manager = manager;

        if (!this.manager.isLoadNeed()) {
            return;
        }
        this.loadCache();
    }

    public T getManager() {
        return this.manager;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        if (!this.manager.isLoadNeed()) {
            return;
        }
        this.loadCache();
    }

    public abstract void loadCache();

}
