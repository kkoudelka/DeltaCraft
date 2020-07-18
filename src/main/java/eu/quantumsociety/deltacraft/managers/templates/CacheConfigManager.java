package eu.quantumsociety.deltacraft.managers.templates;

import eu.quantumsociety.deltacraft.DeltaCraft;
import org.jetbrains.annotations.NotNull;

public abstract class CacheConfigManager<T extends CacheManager> extends ConfigManager {

    @NotNull
    private final T manager;

    public CacheConfigManager(@NotNull DeltaCraft plugin, @NotNull String fileName, @NotNull T manager) {
        super(plugin, fileName);
        this.manager = manager;

        if (!this.manager.isLoadNeed()) {
            return;
        }
        this.loadCache();
    }

    @NotNull
    public T getManager() {
        return this.manager;
    }

    @Override
    public void reloadAll() {
        this.clearCache();

        super.reloadAll();

        if (!this.manager.isLoadNeed()) {
            return;
        }
        this.loadCache();
    }

    private void clearCache() {
        this.manager.clearCache();
    }

    public abstract void loadCache();
}
