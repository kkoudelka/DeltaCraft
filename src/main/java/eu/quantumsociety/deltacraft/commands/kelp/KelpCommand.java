package eu.quantumsociety.deltacraft.commands.kelp;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.managers.DeltaCraftManager;
import eu.quantumsociety.deltacraft.managers.KelpManager;
import eu.quantumsociety.deltacraft.utils.KeyHelper;
import eu.quantumsociety.deltacraft.utils.enums.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class KelpCommand implements CommandExecutor {
    private final KelpManager configManager;
    private final DeltaCraft plugin;

    private final String TempKey = "temp";

    private DeltaCraftManager getMgr() {
        return this.plugin.getManager();
    }

    public KelpCommand(KelpManager dataMgr, DeltaCraft plugin) {
        this.configManager = dataMgr;
        this.plugin = plugin;

        FileConfiguration config = this.configManager.getConfig();
        config.set("players", null);
        this.configManager.saveConfig();
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
        return saveTempLoc(p, this.configManager.PointOneKey, "1");
    }

    private boolean setPointTwo(Player p) {
        return saveTempLoc(p, this.configManager.PointTwoKey, "2");
    }

    private boolean saveTempLoc(Player p, String key, String pointName) {
        Location loc = p.getLocation();
        UUID id = p.getUniqueId();

        loc.setX(Math.floor(loc.getX()));
        loc.setY(Math.floor(loc.getY() - 1));
        loc.setZ(Math.floor(loc.getZ()));

        loc.setYaw(0);
        loc.setPitch(0);

        KeyHelper keys = new KeyHelper(id);

        this.configManager.setLocation(keys.get(TempKey, key), loc);

        this.configManager.saveConfig();

        p.sendMessage(ChatColor.GREEN + "Point " + ChatColor.YELLOW + pointName + ChatColor.GREEN + " saved");
        return true;
    }

    private boolean createFarm(Player p, String name) {
        UUID playerId = p.getUniqueId();
        KeyHelper keys = new KeyHelper(name, this.configManager.FarmPrefix);
        KeyHelper tempKeys = new KeyHelper(playerId);

        String tempKey = tempKeys.get(TempKey);
        String tempKeyOne = tempKeys.get(TempKey, this.configManager.PointOneKey);
        String tempKeyTwo = tempKeys.get(TempKey, this.configManager.PointTwoKey);

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

        String keyOne = keys.get(this.configManager.PointOneKey);
        String keyTwo = keys.get(this.configManager.PointTwoKey);
        String ownerKey = keys.get(this.configManager.OwnerKey);

        FileConfiguration config = this.configManager.getConfig();

        config.set(keyOne, one);
        config.set(keyTwo, two);
        config.set(ownerKey, playerId.toString());
        config.set(tempKey, null);

        this.configManager.saveConfig();

        this.getMgr().addCacheRegion(one, two, name, playerId);

        p.sendMessage(ChatColor.GREEN + "Farm " + ChatColor.YELLOW + name + ChatColor.GREEN + " successfully created");
        return true;
    }

    private boolean deleteFarm(Player p, String name) {
        UUID playerId = p.getUniqueId();
        KeyHelper farmKeys = new KeyHelper(name, this.configManager.FarmPrefix);

        FileConfiguration config = this.configManager.getConfig();

        String ownerId = config.getString(farmKeys.get(this.configManager.OwnerKey));

        if (!playerId.toString().equalsIgnoreCase(ownerId)) {
            p.sendMessage(ChatColor.RED + "You are not owner");
            return true;
        }

        config.set(farmKeys.getPlayerKey(), null);

        this.configManager.saveConfig();

        this.getMgr().removeCacheRegion(name);

        p.sendMessage(ChatColor.GREEN + "Farm " + ChatColor.YELLOW + name + ChatColor.GREEN + " successfully deleted");

        return true;
    }
}
