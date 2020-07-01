package eu.quantumsociety.DeltaCraft.commands.spectate;

import eu.quantumsociety.DeltaCraft.DataManager;
import eu.quantumsociety.DeltaCraft.utils.KeyHelper;
import eu.quantumsociety.DeltaCraft.utils.PluginSubmodule;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getWorld;

public class SpectateCommand implements CommandExecutor {
    final DataManager dataMgr;

    public SpectateCommand(DataManager dataMgr) {
        this.dataMgr = dataMgr;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this commands");
            return false;
        }
        Player p = (Player) sender;
        GameMode currentMode = p.getGameMode();

        boolean isSpec = currentMode == GameMode.SPECTATOR;
        FileConfiguration config = dataMgr.getConfig();
        if (isSpec) {
            switchBack(p, config);
        }


        return false;
    }

    private boolean save(KeyHelper keys, Location l, GameMode gm, FileConfiguration config) {
        String xKey = keys.get("x");
        String yKey = keys.get("y");
        String zKey = keys.get("z");
        String pitchKey = keys.get("pitch");
        String yawKey = keys.get("yaw");
        String worldKey = keys.get("world");
        String modeKey = keys.get("mode");


        config.set(xKey, l.getX());
        config.set(yKey, l.getY());
        config.set(zKey, l.getZ());
        config.set(pitchKey, l.getPitch());
        config.set(yawKey, l.getYaw());
        config.set(worldKey, l.getWorld().getName());
        config.set(modeKey, gm);

        dataMgr.saveConfig();

        return true;
    }

    private Location getLocation(KeyHelper keys, FileConfiguration config) {
        String worldKey = keys.get("world");
        String xKey = keys.get("x");
        String yKey = keys.get("y");
        String zKey = keys.get("z");
        String yawKey = keys.get("yaw");
        String pitchKey = keys.get("pitch");

        String worldName = config.getString(worldKey);
        double x = config.getDouble(xKey);
        double y = config.getDouble(yKey);
        double z = config.getDouble(zKey);
        float yaw = (float) config.getDouble(yawKey);
        float pitch = (float) config.getDouble(pitchKey);

        World world = getWorld(worldName);

        return new Location(world, x, y, z, yaw, pitch);
    }

    private GameMode getGamemode(KeyHelper keys, FileConfiguration config) {
        String modeKey = keys.get("mode");

        String gameModeVal = config.getString(modeKey);

        return GameMode.valueOf(gameModeVal);
    }

    private boolean switchBack(Player p, FileConfiguration config) {
        KeyHelper keys = new KeyHelper(p.getUniqueId(), PluginSubmodule.SPECTATE);

        boolean exists = config.contains(keys.getPlayerKey());
        if (!exists) {
            p.sendMessage(ChatColor.RED + "Neexstuje. A co když tu je?");
            return false;
        }

        Location l = getLocation(keys, config);
        GameMode g = getGamemode(keys, config);

        return switchBack(p, l, g, config);
    }

    private boolean switchBack(Player p, Location l, GameMode gm, FileConfiguration config) {
        p.teleport(p);

        p.setGameMode(gm);

        return true;
    }

    private boolean switchToSpectate(Player p, FileConfiguration config) {
        KeyHelper keys = new KeyHelper(p.getUniqueId(), PluginSubmodule.SPECTATE);

        Location loc = p.getLocation();
        GameMode gm = p.getGameMode();

        boolean suc = this.save(keys, loc, gm, config);
        if (!suc) {
            return false;
        }

        p.setGameMode(GameMode.SPECTATOR);
        return true;
    }
}
