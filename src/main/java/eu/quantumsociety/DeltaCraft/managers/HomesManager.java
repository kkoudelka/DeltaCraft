package eu.quantumsociety.DeltaCraft.managers;

import eu.quantumsociety.DeltaCraft.DeltaCraft;
import eu.quantumsociety.DeltaCraft.classes.PlayerHome;
import eu.quantumsociety.DeltaCraft.utils.KeyHelper;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class HomesManager extends ConfigManager {

    public HomesManager(DeltaCraft plugin) {
        super(plugin, "home.yml");
    }

    public List<PlayerHome> getPlayerHomes(Player p) {
        FileConfiguration uwuwu = this.getConfig();
        KeyHelper k = new KeyHelper(p.getUniqueId());
        return (List<PlayerHome>) uwuwu.get(k.getPlayerKey());
    }

    public boolean setHome(Player p, String homeName) {
        Location l = p.getLocation();

        PlayerHome pl = new PlayerHome(p.getUniqueId(), homeName, l);

        FileConfiguration uwuwu = this.getConfig();
        KeyHelper kh = new KeyHelper(p.getUniqueId());

        uwuwu.set(kh.getPlayerKey(), pl);

        this.saveConfig();

        return true;
    }
}
