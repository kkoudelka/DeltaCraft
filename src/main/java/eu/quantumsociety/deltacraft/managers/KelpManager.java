package eu.quantumsociety.deltacraft.managers;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.KelpFarm;
import eu.quantumsociety.deltacraft.managers.cache.KelpCacheManager;
import eu.quantumsociety.deltacraft.managers.templates.CacheConfigManager;
import eu.quantumsociety.deltacraft.utils.KeyHelper;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class KelpManager extends CacheConfigManager<KelpCacheManager> {
    public final String FarmPrefix = "farms";
    public final String PointOneKey = "pointOne";
    public final String PointTwoKey = "pointTwo";
    public final String OwnerKey = "owner";
    public final String TempKey = "temp";

    public KelpManager(DeltaCraft plugin, KelpCacheManager mgr) {
        super(plugin, "kelp.yml", mgr);
    }

    @Override
    public void loadCache() {
        HashMap<String, KelpFarm> regions = this.getFarms();
        this.getManager().loadCache(regions);
    }

    public boolean farmExists(String name) {
        return this.getConfig().contains(FarmPrefix + "." + name);
    }

    public HashMap<String, KelpFarm> getFarms() {
        FileConfiguration config = this.getConfig();
        ConfigurationSection section = config.getConfigurationSection(this.FarmPrefix);
        if (section == null) {
            return new HashMap<>();
        }
        Set<String> keys = section.getKeys(false);
        if (keys.size() < 1) {
            return new HashMap<>();
        }

        HashMap<String, KelpFarm> regions = new HashMap<>();
        for (String key : keys) {
            KeyHelper kh = new KeyHelper(key, this.FarmPrefix);

            Location one = config.getLocation(kh.get(this.PointOneKey));
            Location two = config.getLocation(kh.get(this.PointTwoKey));
            String id = config.getString(kh.get(this.OwnerKey));

            if (one == null || two == null || id == null) {
                return new HashMap<>();
            }
            UUID uid = UUID.fromString(id);

            KelpFarm region = new KelpFarm(one, two, key, uid);
            regions.put(key, region);
        }
        this.plugin.debugMsg("Loaded " + regions.size() + " farms");
        return regions;
    }

    public void saveTempLocation(UUID id, Location loc, String key) {
        loc.setYaw(0);
        loc.setPitch(0);

        KeyHelper keys = new KeyHelper(id);

        this.setLocation(keys.get(TempKey, key), loc);

        this.saveConfig();
    }


    @Nullable
    public Location getPointOne(UUID id) {
        return this.getPoint(id, this.PointOneKey);
    }

    @Nullable
    public Location getPointTwo(UUID id) {
        return this.getPoint(id, this.PointTwoKey);
    }

    @Nullable
    private Location getPoint(UUID id, String key) {
        return this.getPoint(new KeyHelper(id), key);
    }

    @Nullable
    private Location getPoint(KeyHelper tempKeys, String key) {
        String tempKey = tempKeys.get(TempKey, key);

        return getLocation(tempKey);
    }

    public void addFarm(Location one, Location two, String name, UUID ownerId) {
        KeyHelper keys = new KeyHelper(name, this.FarmPrefix);
        KeyHelper tempKeys = new KeyHelper(ownerId);

        String tempKey = tempKeys.get(TempKey);

        String keyOne = keys.get(this.PointOneKey);
        String keyTwo = keys.get(this.PointTwoKey);
        String ownerKey = keys.get(this.OwnerKey);

        FileConfiguration config = this.getConfig();

        config.set(keyOne, one);
        config.set(keyTwo, two);
        config.set(ownerKey, ownerId.toString());
        config.set(tempKey, null);

        this.saveConfig();

        this.getManager().addItem(one, two, name, ownerId);
    }

    @Nullable
    public String getOwnerId(String name) {
        KeyHelper farmKeys = new KeyHelper(name, this.FarmPrefix);

        return this.getConfig().getString(farmKeys.get(this.OwnerKey));
    }

    public void removeFarm(String name) {
        KeyHelper farmKeys = new KeyHelper(name, this.FarmPrefix);

        this.getConfig().set(farmKeys.getPlayerKey(), null);

        this.saveConfig();

        this.getManager().removeItem(name);
    }
}
