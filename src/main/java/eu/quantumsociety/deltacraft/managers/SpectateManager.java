package eu.quantumsociety.deltacraft.managers;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CachePlayer;
import eu.quantumsociety.deltacraft.managers.cache.SpectateCacheManager;
import eu.quantumsociety.deltacraft.managers.templates.CacheConfigManager;
import eu.quantumsociety.deltacraft.utils.KeyHelper;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class SpectateManager extends CacheConfigManager<SpectateCacheManager> {
    public final String PlayersPrefix = "players";
    public final String LocationKey = "location";
    public final String GamemodeKey = "mode";

    public SpectateManager(DeltaCraft plugin, SpectateCacheManager manager) {
        super(plugin, "spectate.yml", manager);
    }

    @Override
    public void loadCache() {
        HashMap<UUID, CachePlayer> players = this.getSpectators();
        this.getManager().loadCache(players);
    }

    public HashMap<UUID, CachePlayer> getSpectators() {
        FileConfiguration config = this.getConfig();
        ConfigurationSection section = config.getConfigurationSection(this.PlayersPrefix);
        if (section == null) {
            return new HashMap<>();
        }
        Set<String> keys = section.getKeys(false);
        if (keys.size() < 1) {
            return new HashMap<>();
        }

        HashMap<UUID, CachePlayer> cache = new HashMap<>();
        for (String key : keys) {
            KeyHelper kh = new KeyHelper(key);

            Location loc = this.getLocation(kh);
            GameMode gm = this.getGamemode(kh);

            UUID id = UUID.fromString(key);

            CachePlayer pl = new CachePlayer(id, loc, gm);
            cache.put(id, pl);
        }
        return cache;
    }

    public Location getLocation(KeyHelper keys) {
        FileConfiguration config = this.getConfig();
        String path = keys.get(this.LocationKey);
        if (!config.contains(path)) {
            return null;
        }

        return (Location) config.get(path);
    }

    public GameMode getGamemode(KeyHelper keys) {
        FileConfiguration config = this.getConfig();

        String modeKey = keys.get(this.GamemodeKey);

        String gameModeVal = config.getString(modeKey);

        return GameMode.valueOf(gameModeVal);
    }

    public boolean exists(KeyHelper keys) {
        return this.getConfig().contains(keys.getPlayerKey());
    }

    public void save(KeyHelper keys, Location l, GameMode gm) {
        FileConfiguration config = this.getConfig();

        config.set(keys.get(this.LocationKey), l);
        config.set(keys.get(this.GamemodeKey), gm.name());

        this.saveConfig();
    }

    public void delete(UUID id) {
        KeyHelper keys = new KeyHelper(id);

        this.getConfig().set(keys.getPlayerKey(), null);

        this.saveConfig();
    }
}
