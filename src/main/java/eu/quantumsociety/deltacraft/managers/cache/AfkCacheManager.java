package eu.quantumsociety.deltacraft.managers.cache;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CacheAfk;
import eu.quantumsociety.deltacraft.managers.templates.CacheManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AfkCacheManager extends CacheManager<UUID, CacheAfk> {
    public AfkCacheManager(DeltaCraft plugin) {
        super(plugin, false);
    }

    public void addItem(Player player) {
        this.addItem(player.getUniqueId(), new CacheAfk(player));
    }

    public boolean isPlayerAfk(UUID uuid) {
        return this.contains(uuid);
    }


}
