package eu.quantumsociety.deltacraft.managers.cache;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CacheRegion;
import eu.quantumsociety.deltacraft.managers.templates.CacheManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KelpCacheManager extends CacheManager<String, CacheRegion> {
    public KelpCacheManager(DeltaCraft plugin) {
        super(plugin, true);
    }

    public void addItem(Location one, Location two,
                        String name, UUID ownerId) {
        CacheRegion region = new CacheRegion(one, two, name, ownerId);

        this.addItem(name, region);
    }

    public CacheRegion getKelpFarm(Location l) {
        for (CacheRegion region : this.getValues()) {
            if (region.contains(l)) {
                return region;
            }
        }
        return null;
    }

    public boolean isInKelpFarm(Location l) {
        return this.getKelpFarm(l) != null;
    }

    public List<CacheRegion> getKelpFarms(Player p) {
        return this.getKelpFarms(p.getUniqueId());
    }

    public List<CacheRegion> getKelpFarms(UUID ownerId) {
        Collection<CacheRegion> all = this.getValues();

        Stream<CacheRegion> r = all.stream()
                .filter(x -> x.ownerId.equals(ownerId));

        return r.collect(Collectors.toList());
    }

    public List<String> getKelpFarmNames(UUID ownerId) {
        Collection<CacheRegion> farms = this.getKelpFarms(ownerId);

        Stream<String> res = farms.stream()
                .map(x -> x.name);

        return res.collect(Collectors.toList());
    }

    public int getKelpFarmCount(Player p) {
        return this.getKelpFarmCount(p.getUniqueId());
    }

    public int getKelpFarmCount(UUID ownerId) {
        Collection<CacheRegion> all = this.getValues();

        long count = all.stream()
                .filter(x -> x.ownerId.equals(ownerId))
                .count();

        return (int) count;
    }

}
