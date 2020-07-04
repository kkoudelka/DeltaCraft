package eu.quantumsociety.deltacraft.managers;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.PlayerHome;
import eu.quantumsociety.deltacraft.utils.KeyHelper;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomesManager extends ConfigManager {

    public HomesManager(DeltaCraft plugin) {
        super(plugin, "home.yml");
    }

    public List<PlayerHome> getPlayerHomes(Player p) {
        FileConfiguration uwuwu = this.getConfig();
        KeyHelper k = new KeyHelper(p.getUniqueId());

        ConfigurationSection section = uwuwu.getConfigurationSection(k.getPlayerKey());

        List<PlayerHome> list = new ArrayList<>();
        for (String key : section.getKeys(false)) {
            Location loc = section.getLocation(key + ".location");
            PlayerHome home = new PlayerHome(p.getUniqueId(), key, loc);

            list.add(home);
        }

        return list;
    }

    public boolean setHome(Player p, String homeName) {
        Location l = p.getLocation();

        PlayerHome pl = new PlayerHome(p.getUniqueId(), homeName, l);

        FileConfiguration uwuwu = this.getConfig();
        KeyHelper kh = new KeyHelper(p.getUniqueId());

        uwuwu.set(kh.get(homeName, "location"), pl.getLocation());

        this.saveConfig();

        return true;
    }
}
