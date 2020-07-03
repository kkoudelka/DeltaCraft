package eu.quantumsociety.DeltaCraft.commands.spectate;

import eu.quantumsociety.DeltaCraft.DeltaCraft;
import eu.quantumsociety.DeltaCraft.managers.ConfigManager;
import eu.quantumsociety.DeltaCraft.managers.DeltaCraftManager;
import eu.quantumsociety.DeltaCraft.utils.KeyHelper;
import eu.quantumsociety.DeltaCraft.utils.enums.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

import static org.bukkit.Bukkit.getWorld;

public class SpectateCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private final DeltaCraft plugin;

    private DeltaCraftManager getMgr() {
        return this.plugin.getManager();
    }

    public SpectateCommand(ConfigManager dataMgr, DeltaCraft plugin) {

        this.configManager = dataMgr;
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this commands");
            return false;
        }
        Player p = (Player) sender;

        if (!p.hasPermission(Permissions.USESPECTATE.getName())) {
            p.sendMessage(ChatColor.RED + "You don't have permission to use spectator mode!");
            return true;
        }
        return executeSwitch(p);
    }

    private boolean executeSwitch(Player p) {
        KeyHelper keys = new KeyHelper(p.getUniqueId());
        FileConfiguration config = configManager.getConfig();

        boolean exists = config.contains(keys.getPlayerKey());
        if (!exists) {
            return switchToSpectate(p, config);
        }

        return switchBack(p, config, keys);
    }

    private boolean save(KeyHelper keys, Location l, GameMode gm, FileConfiguration config) {
        config.set(keys.get("location"), l);
        config.set(keys.get("mode"), gm);

        configManager.saveConfig();
        return true;
    }

    private void delete(UUID id, FileConfiguration config) {
        KeyHelper keys = new KeyHelper(id);

        config.set(keys.getPlayerKey(), null);

        configManager.saveConfig();
    }

    private Location getLocation(KeyHelper keys, FileConfiguration config) {
        String path = keys.get("location");
        if (!config.contains(path)) {
            return null;
        }

        return (Location) config.get(path);
    }

    private GameMode getGamemode(KeyHelper keys, FileConfiguration config) {
        String modeKey = keys.get("mode");

        String gameModeVal = config.getString(modeKey);

        return GameMode.valueOf(gameModeVal);
    }

    private boolean switchBack(Player p, FileConfiguration config, KeyHelper keys) {
        Location l = getLocation(keys, config);
        GameMode g = getGamemode(keys, config);

        return switchBack(p, l, g, config);
    }

    private boolean switchBack(Player p, Location l, GameMode gm, FileConfiguration config) {
        p.teleport(l);

        p.setGameMode(gm);

        UUID id = p.getUniqueId();

        this.getMgr().removeCachePlayer(id);
        delete(id, config);

        p.sendMessage(ChatColor.YELLOW + "You are no longer Spectating!");

        return true;
    }

    private boolean switchToSpectate(Player p, FileConfiguration config) {
        KeyHelper keys = new KeyHelper(p.getUniqueId());

        Location loc = p.getLocation();
        GameMode gm = p.getGameMode();

        boolean suc = this.save(keys, loc, gm, config);
        if (!suc) {
            return false;
        }

        this.getMgr().addCachePlayer(p, loc, gm);

        p.setGameMode(GameMode.SPECTATOR);

        p.sendMessage(ChatColor.GREEN + "You are now Spectating!");

        return true;
    }
}
