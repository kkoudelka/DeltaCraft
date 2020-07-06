package eu.quantumsociety.deltacraft.managers;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CacheRegion;
import eu.quantumsociety.deltacraft.utils.KeyHelper;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class KelpManager extends ConfigManager {
    private DeltaCraftManager getMgr() {
        return this.plugin.getManager();
    }

    public final String FarmPrefix = "farms";
    public final String PointOneKey = "pointOne";
    public final String PointTwoKey = "pointTwo";
    public final String OwnerKey = "owner";

    public KelpManager(DeltaCraft plugin) {
        super(plugin, "kelp.yml");

        this.loadRegions();
    }


    @Override
    public void reloadConfig() {
        super.reloadConfig();

        this.loadRegions();
    }

    public void loadRegions() {
        HashMap<String, CacheRegion> regions = this.getRegions();
        this.getMgr().loadRegions(regions);
    }

    public HashMap<String, CacheRegion> getRegions() {
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
        this.plugin.debugMsg("Loaded " + regions.size() + " regions");
        return regions;
    }
}
