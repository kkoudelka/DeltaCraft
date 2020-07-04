package eu.quantumsociety.DeltaCraft.commands.kelp;

import eu.quantumsociety.DeltaCraft.DeltaCraft;
import eu.quantumsociety.DeltaCraft.managers.ConfigManager;
import eu.quantumsociety.DeltaCraft.managers.DeltaCraftManager;
import eu.quantumsociety.DeltaCraft.utils.KeyHelper;
import eu.quantumsociety.DeltaCraft.utils.enums.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.File;
import java.util.UUID;

public class KelpCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private final DeltaCraft plugin;

    private final String TempKey = "temp";
    private final String PointOneKey = "pointOne";
    private final String PointTwoKey = "pointTwo";
    private final String FarmPrefix = "farms";

    private DeltaCraftManager getMgr() {
        return this.plugin.getManager();
    }

    public KelpCommand(ConfigManager dataMgr, DeltaCraft plugin) {

        this.configManager = dataMgr;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this commands");
            return false;
        }
        Player p = (Player) sender;

        if (!p.hasPermission(Permissions.KELPFARMUSE.getName())) {
            p.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        if (args.length < 2) {
            p.sendMessage(ChatColor.YELLOW + "You must pass some arguments");
            return true;
        }

        String cmd = args[0];
        if (cmd.equalsIgnoreCase("set")) {
            switch (args[1]) {
                case "1":
                    return setPointOne(p);
                case "2":
                    return setPointTwo(p);
                default:
                    p.sendMessage(ChatColor.YELLOW + "Only 1 or 2");
                    return true;
            }
        }

        if (cmd.equalsIgnoreCase("create")) {
            String name = args[1];
            return this.createFarm(p, name);
        }

        if (cmd.equalsIgnoreCase("delete") || cmd.equalsIgnoreCase("remove")) {
            String name = args[1];
            return this.deleteFarm(p, name);
        }

        return true;
    }

    private boolean setPointOne(Player p) {
        return saveTempLoc(p, PointOneKey, "1");
    }

    private boolean setPointTwo(Player p) {
        return saveTempLoc(p, PointTwoKey, "2");
    }

    private boolean saveTempLoc(Player p, String key, String pointName) {
        Location loc = p.getLocation();
        UUID id = p.getUniqueId();

        loc.setX(Math.floor(loc.getX()));
        loc.setY(Math.floor(loc.getY() - 1));
        loc.setZ(Math.floor(loc.getZ()));

        KeyHelper keys = new KeyHelper(id);

        this.configManager.setLocation(keys.get(TempKey, key), loc);

        this.configManager.saveConfig();

        p.sendMessage(ChatColor.GREEN + "Point " + ChatColor.YELLOW + pointName + ChatColor.GREEN + " saved");
        return true;
    }

    private boolean createFarm(Player p, String name) {
        UUID playerId = p.getUniqueId();
        KeyHelper keys = new KeyHelper(name, FarmPrefix);
        KeyHelper tempKeys = new KeyHelper(playerId);

        String tempKey = tempKeys.get(TempKey);
        String tempKeyOne = tempKeys.get(TempKey, PointOneKey);
        String tempKeyTwo = tempKeys.get(TempKey, PointTwoKey);

        Location one = this.configManager.getLocation(tempKeyOne);
        if (one == null) {
            p.sendMessage(ChatColor.RED + "Point 1 is not set");
            return true;
        }
        Location two = this.configManager.getLocation(tempKeyTwo);
        if (two == null) {
            p.sendMessage(ChatColor.RED + "Point 2 is not set");
            return true;
        }

        String keyOne = keys.get(PointOneKey);
        String keyTwo = keys.get(PointTwoKey);
        String ownerKey = keys.get("owner");

        FileConfiguration config = this.configManager.getConfig();

        config.set(keyOne, one);
        config.set(keyTwo, two);
        config.set(ownerKey, playerId.toString());
        config.set(tempKey, null);

        this.configManager.saveConfig();

        this.getMgr().addCacheRegion(one, two, name, playerId);

        p.sendMessage(ChatColor.GREEN + "Farm successfully created");
        return true;
    }

    private boolean deleteFarm(Player p, String name) {
        UUID playerId = p.getUniqueId();
        KeyHelper farmKeys = new KeyHelper(name, FarmPrefix);

        FileConfiguration config = this.configManager.getConfig();

        String ownerId = config.getString(farmKeys.get("owner"));

        if (!playerId.toString().equalsIgnoreCase(ownerId)) {
            p.sendMessage(ChatColor.RED + "You are not owner");
            return true;
        }

        config.set(farmKeys.getPlayerKey(), null);

        this.configManager.saveConfig();

        p.sendMessage(ChatColor.GREEN + "Farm successfully deleted");

        return true;
    }
}
