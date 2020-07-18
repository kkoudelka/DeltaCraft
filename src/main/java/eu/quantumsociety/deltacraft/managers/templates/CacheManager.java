package eu.quantumsociety.deltacraft.managers.templates;

import eu.quantumsociety.deltacraft.DeltaCraft;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;

public abstract class CacheManager<TKey, T extends Object> {
    private boolean isLoaded;
    private final boolean needsLoad;

    protected final DeltaCraft plugin;

    private HashMap<TKey, T> cache;

    public CacheManager(DeltaCraft plugin, boolean needsLoad) {
        this.plugin = plugin;
        this.cache = new HashMap<>();
        this.needsLoad = needsLoad;
    }

    public boolean isLoadNeed() {
        return this.needsLoad;
    }

    public void addItem(TKey id, T item) {
        this.cache.put(id, item);
    }

    public void removeItem(TKey id) {
        this.cache.remove(id);
    }

    @NotNull
    public HashMap<TKey, T> getCache() throws Exception {
        this.checkLoad();
        return cache;
    }

    @NotNull
    public Collection<T> getValues() {
        return this.cache.values();
    }

    @Nullable
    public T get(TKey key) throws Exception {
        this.checkLoad();
        return this.cache.get(key);
    }

    @NotNull
    public int getCount() {
        return this.cache.size();
    }

    @NotNull
    public boolean contains(TKey key) {
        return this.cache.containsKey(key);
    }


    public void loadCache(HashMap<TKey, T> toLoad) {
        if (!needsLoad) {
//            throw new Exception("This manager does not need loading!");
            return;
        }
        if (isLoaded) {
//            throw new Exception("This manager is already loaded");
            return;
        }
        this.cache = toLoad;
        this.isLoaded = true;
    }

    private void checkLoad() throws Exception {
        if (!needsLoad) {
            return;
        }
        if (!isLoaded) {
            throw new Exception("Manager is not loaded");
        }
    }
}
