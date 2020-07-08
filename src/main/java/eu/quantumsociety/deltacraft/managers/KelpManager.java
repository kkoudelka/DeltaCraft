package eu.quantumsociety.deltacraft.managers;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CacheRegion;
import eu.quantumsociety.deltacraft.managers.cache.KelpCacheManager;
import eu.quantumsociety.deltacraft.managers.templates.CacheConfigManager;
import eu.quantumsociety.deltacraft.utils.KeyHelper;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class KelpManager extends CacheConfigManager<KelpCacheManager> {
    public final String FarmPrefix = "farms";
    public final String PointOneKey = "pointOne";
    public final String PointTwoKey = "pointTwo";
    public final String OwnerKey = "owner";

    public KelpManager(DeltaCraft plugin, KelpCacheManager mgr) {
        super(plugin, "kelp.yml", mgr);
    }

    @Override
    public void loadCache() {
        HashMap<String, CacheRegion> regions = this.getFarms();
        this.getManager().loadCache(regions);
    }

    public boolean farmExists(String name) {
        return this.getConfig().contains(FarmPrefix + "." + name);
    }

    public HashMap<String, CacheRegion> getFarms() {
        FileConfiguration config = this.getConfig();
        ConfigurationSection section = config.getConfigurationSection(this.FarmPrefix);
        if (section == null) {
            return new HashMap<>();
        }
        Set<String> keys = section.getKeys(false);
        if (keys.size() < 1) {
            return new HashMap<>();
        }

        HashMap<String, CacheRegion> regions = new HashMap<>();
        for (String key : keys) {
            KeyHelper kh = new KeyHelper(key, this.FarmPrefix);

            Location one = config.getLocation(kh.get(this.PointOneKey));
            Location two = config.getLocation(kh.get(this.PointTwoKey));
            String id = config.getString(kh.get(this.OwnerKey));

            if (one == null || two == null || id == null) {
                return new HashMap<>();
            }
            UUID uid = UUID.fromString(id);

            CacheRegion region = new CacheRegion(one, two, key, uid);
            regions.put(key, region);
        }
        this.plugin.debugMsg("Loaded " + regions.size() + " farms");
        return regions;
    }
}
