package eu.quantumsociety.deltacraft.managers;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.SpectatePlayer;
import eu.quantumsociety.deltacraft.managers.cache.SpectateCacheManager;
import eu.quantumsociety.deltacraft.managers.templates.CacheConfigManager;
import eu.quantumsociety.deltacraft.utils.KeyHelper;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class SpectateManager extends CacheConfigManager<SpectateCacheManager> {
    private final String PlayersPrefix = "players";
    private final String LocationKey = "location";
    private final String VelocityKey = "velocity";
    private final String FallDistanceKey = "falldistance";
    private final String GamemodeKey = "mode";

    public SpectateManager(DeltaCraft plugin, SpectateCacheManager manager) {
        super(plugin, "spectate.yml", manager);
    }

    @Override
    public void loadCache() {
        HashMap<UUID, SpectatePlayer> players = this.getSpectators();
        this.getManager().loadCache(players);
    }

    public HashMap<UUID, SpectatePlayer> getSpectators() {
        FileConfiguration config = this.getConfig();
        ConfigurationSection section = config.getConfigurationSection(this.PlayersPrefix);
        if (section == null) {
            return new HashMap<>();
        }
        Set<String> keys = section.getKeys(false);
        if (keys.size() < 1) {
            return new HashMap<>();
        }

        HashMap<UUID, SpectatePlayer> cache = new HashMap<>();
        for (String key : keys) {
            KeyHelper kh = new KeyHelper(key);

            Location loc = this.getLocation(kh);
            GameMode gm = this.getGamemode(kh);
            Vector velocity = this.getVelocity(kh);
            float fallDis = this.getFallDistance(kh);

            UUID id = UUID.fromString(key);

            SpectatePlayer pl = new SpectatePlayer(id, loc, gm, velocity, fallDis);
            cache.put(id, pl);
        }
        return cache;
    }

    public SpectatePlayer getPlayer(UUID playerId) {
        KeyHelper keys = new KeyHelper(playerId);
        Location l = this.getLocation(keys);
        GameMode g = this.getGamemode(keys);
        Vector vel = this.getVelocity(keys);
        float fallDis = this.getFallDistance(keys);

        return new SpectatePlayer(playerId, l, g, vel, fallDis);
    }

    @Nullable
    private Location getLocation(KeyHelper keys) {
        String path = keys.get(this.LocationKey);

        return this.getConfig().getLocation(path);
    }

    @Nullable
    private GameMode getGamemode(KeyHelper keys) {
        String modeKey = keys.get(this.GamemodeKey);

        String gameModeVal = this.getConfig().getString(modeKey);

        return GameMode.valueOf(gameModeVal);
    }

    @Nullable
    private Vector getVelocity(KeyHelper keys) {
        String modeKey = keys.get(this.VelocityKey);

        return this.getConfig().getVector(modeKey);
    }

    @Nullable
    private float getFallDistance(KeyHelper keys) {
        String modeKey = keys.get(this.FallDistanceKey);

        return (float) this.getConfig().getDouble(modeKey);
    }

    public boolean exists(UUID id) {
        return this.exists(new KeyHelper(id));
    }

    private boolean exists(KeyHelper keys) {
        return this.getConfig().contains(keys.getPlayerKey());
    }

    public void addPlayer(SpectatePlayer p) {
        if (p == null || !p.isValid()) {
            return;
        }
        this.save(p.getId(), p.getOriginalLocation(), p.getPrevGameMode(), p.getOriginalVelocity(), p.getFallDistance());
    }

    private void save(UUID id, Location l, GameMode gm, Vector velocity, float fallDistance) {
        KeyHelper keys = new KeyHelper(id);

        FileConfiguration config = this.getConfig();

        config.set(keys.get(this.LocationKey), l);
        config.set(keys.get(this.GamemodeKey), gm.name());
        config.set(keys.get(this.VelocityKey), velocity);
        config.set(keys.get(this.FallDistanceKey), fallDistance);

        this.saveConfig();

        this.getManager().addItem(id, l, gm, velocity, fallDistance);
    }

    public void delete(UUID id) {
        KeyHelper keys = new KeyHelper(id);

        this.getConfig().set(keys.getPlayerKey(), null);

        this.saveConfig();

        this.getManager().removeItem(id);

    }
}
