package eu.quantumsociety.deltacraft.managers.cache;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CachePlayer;
import eu.quantumsociety.deltacraft.managers.templates.CacheManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpectateCacheManager extends CacheManager<UUID, CachePlayer> {
    public SpectateCacheManager(DeltaCraft plugin) {
        super(plugin, true);
    }

    public void addItem(Player player, Location origin, GameMode gm) {
        this.addItem(
                player.getUniqueId(),
                new CachePlayer(
                        player,
                        origin,
                        gm
                ));
    }

    public boolean isPlayerSpectating(UUID uuid) {
        return this.contains(uuid);
    }
}
