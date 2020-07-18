package eu.quantumsociety.deltacraft.managers.cache;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.KelpFarm;
import eu.quantumsociety.deltacraft.managers.templates.CacheManager;
import eu.quantumsociety.deltacraft.utils.enums.Settings;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KelpCacheManager extends CacheManager<String, KelpFarm> {
    private final boolean isDebug;

    public KelpCacheManager(DeltaCraft plugin) {
        super(plugin, true);

        this.isDebug = plugin.getConfig().getBoolean(Settings.KELPDEBUG.getPath());
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void addItem(Location one, Location two,
                        String name, UUID ownerId) {
        KelpFarm region = new KelpFarm(one, two, name, ownerId);

        this.addItem(name, region);
    }

    public KelpFarm getKelpFarm(Location l) {
        for (KelpFarm region : this.getValues()) {
            if (region.contains(l)) {
                return region;
            }
        }
        return null;
    }

    public boolean isInKelpFarm(Location l) {
        return this.getKelpFarm(l) != null;
    }

    public List<KelpFarm> getKelpFarms(Player p) {
        return this.getKelpFarms(p.getUniqueId());
    }

    public List<KelpFarm> getKelpFarms(UUID ownerId) {
        Collection<KelpFarm> all = this.getValues();

        Stream<KelpFarm> r = all.stream()
                .filter(x -> x.ownerId.equals(ownerId));

        return r.collect(Collectors.toList());
    }

    public List<String> getKelpFarmNames(UUID ownerId) {
        Collection<KelpFarm> farms = this.getKelpFarms(ownerId);

        Stream<String> res = farms.stream()
                .map(x -> x.name);

        return res.collect(Collectors.toList());
    }

    public int getKelpFarmCount(Player p) {
        return this.getKelpFarmCount(p.getUniqueId());
    }

    public int getKelpFarmCount(UUID ownerId) {
        Collection<KelpFarm> all = this.getValues();

        long count = all.stream()
                .filter(x -> x.ownerId.equals(ownerId))
                .count();

        return (int) count;
    }

    public void debugMsg(String message) {
        if (isDebug()) {
            plugin.getServer().getLogger().info("[Kelp DEBUG]: " + message);
        }
    }
}
