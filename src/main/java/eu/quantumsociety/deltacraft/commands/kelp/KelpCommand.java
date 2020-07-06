package eu.quantumsociety.deltacraft.commands.kelp;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CacheRegion;
import eu.quantumsociety.deltacraft.managers.DeltaCraftManager;
import eu.quantumsociety.deltacraft.managers.KelpManager;
import eu.quantumsociety.deltacraft.utils.KeyHelper;
import eu.quantumsociety.deltacraft.utils.MathHelper;
import eu.quantumsociety.deltacraft.utils.enums.Permissions;
import eu.quantumsociety.deltacraft.utils.enums.Settings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KelpCommand implements CommandExecutor, TabCompleter {
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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this commands");
            return false;
        }
        Player p = (Player) sender;

        if (!p.hasPermission(Permissions.KELPFARMUSE.getName())) {
            p.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        if (args.length < 1 || args[0].isEmpty()) {
            p.sendMessage(ChatColor.GREEN + "Use " + ChatColor.YELLOW + "/kelp ? " + ChatColor.GREEN + "for help");
            return true;
        }
        String cmd = args[0].trim();

        if (cmd.equalsIgnoreCase("test")) {
            this.isInFarm(p);
            return true;
        }

        if (args.length < 2) {
            if (cmd.equalsIgnoreCase("age")) {
                this.getAge(p);
                return true;
            }

            if (cmd.equalsIgnoreCase("?") || cmd.equalsIgnoreCase("help")) {
                this.sendHelp(p);
                return true;
            }

            p.sendMessage(ChatColor.YELLOW + "You must pass some arguments");
            return true;
        }

        String arg = args[1].trim();
        if (cmd.equalsIgnoreCase("set")) {
            switch (arg) {
                case "1":
                case "one":
                case "first":
                    setPointOne(p);
                    break;
                case "2":
                case "two":
                case "second":
                    setPointTwo(p);
                    break;
                default:
                    p.sendMessage(ChatColor.YELLOW + "You can only set point '1' or '2'");
                    return true;
            }

            if (pointsAreSet(p)) {
                p.sendMessage(ChatColor.GREEN + "Well done! You can now create farm by " + ChatColor.YELLOW + "/kelp create <name of the farm>");
            }
            return true;
        }

        if (cmd.equalsIgnoreCase("create")) {
            if (!p.hasPermission(Permissions.KELPFARMCREATE.getName())) {
                p.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            return this.createFarm(p, arg);
        }

        if (cmd.equalsIgnoreCase("delete") || cmd.equalsIgnoreCase("remove")) {
            if (!p.hasPermission(Permissions.KELPFARMREMOVE.getName())) {
                p.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            return this.deleteFarm(p, arg);
        }

        if (cmd.equalsIgnoreCase("age")) {
            if (!p.hasPermission(Permissions.KELPFARMSETAGE.getName())) {
                p.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            int age = 0;
            try {
                age = Integer.parseInt(arg);
            } catch (NumberFormatException ignored) {

            }
            this.setAge(p, age);
        }

        return true;
    }

    private void setPointOne(Player p) {
        saveTempLoc(p, this.configManager.PointOneKey, "1");
    }

    private void setPointTwo(Player p) {
        saveTempLoc(p, this.configManager.PointTwoKey, "2");
    }

    private void saveTempLoc(Player p, String key, String pointName) {
        Location loc = p.getLocation();
        UUID id = p.getUniqueId();

        loc.setYaw(0);
        loc.setPitch(0);

        KeyHelper keys = new KeyHelper(id);

        this.configManager.setLocation(keys.get(TempKey, key), loc);

        this.configManager.saveConfig();

        p.sendMessage(ChatColor.GREEN + "Point " + ChatColor.YELLOW + pointName + ChatColor.GREEN + " saved");
    }

    private boolean pointsAreSet(Player p) {
        KeyHelper tempKeys = new KeyHelper(p.getUniqueId());
        String tempKeyOne = tempKeys.get(TempKey, this.configManager.PointOneKey);
        String tempKeyTwo = tempKeys.get(TempKey, this.configManager.PointTwoKey);

        Location one = this.configManager.getLocation(tempKeyOne);
        if (one == null) {
            return false;
        }
        Location two = this.configManager.getLocation(tempKeyTwo);
        if (two == null) {
            return false;
        }
        return true;
    }

    private boolean createFarm(Player p, String name) {
        UUID playerId = p.getUniqueId();
        KeyHelper tempKeys = new KeyHelper(playerId);

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

        double maxDistance = this.plugin.getConfig().getDouble(Settings.KELPMAXDISTANCE.getPath());
        double distance;
        try {
            distance = MathHelper.calcDistance(one, two);
        } catch (Exception ex) {
            p.sendMessage(ChatColor.RED + "Points cannot be in a different worlds!");
            return true;
        }

        if (distance > maxDistance) {
            p.sendMessage(ChatColor.RED + "Maximum distance between blocks is :" + maxDistance + ". Your distance is " + distance);
            return true;
        }

        KeyHelper keys = new KeyHelper(name, this.configManager.FarmPrefix);
        String tempKey = tempKeys.get(TempKey);

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

    private void getAge(Player p) {
        Block b = this.getBlock(p);
        BlockData bd = b.getBlockData();
        if (!(bd instanceof Ageable)) {
            return;
        }
        Ageable a = (Ageable) bd;

        p.sendMessage(ChatColor.GREEN + "Current age: " + ChatColor.YELLOW + a.getAge());
    }

    private Block getTop(Block b) {
        if (b.getType() == Material.KELP) {
            return b;
        }
        if (b.getType() != Material.KELP_PLANT) {
            return b;
        }
        return this.getTop(b.getRelative(BlockFace.UP));
    }

    private Block getBlock(Player p) {
        Set<Material> ignore = new HashSet<>(Arrays.asList(Material.OAK_SIGN, Material.OAK_WALL_SIGN, Material.WATER, Material.WATER_BUCKET));
        Block b = p.getTargetBlock(ignore, 4);
        return this.getTop(b);
    }

    private void setAge(Player p, int age) {
        Block b = this.getBlock(p);
        BlockData bd = b.getBlockData();
        if (!(bd instanceof Ageable)) {
            return;
        }
        Ageable a = (Ageable) bd;
        a.setAge(age);

        b.setBlockData(a);
        p.sendMessage(ChatColor.GREEN + "Age set to: " + ChatColor.YELLOW + age);
    }

    private void sendHelp(Player p) {
        String text = "Kelp farms =========================\n";
        text += ChatColor.YELLOW + "/kelp set <1 or 2> " + ChatColor.GREEN + " Set first and second point of a farm \n";
        text += ChatColor.YELLOW + "/kelp create <name> " + ChatColor.GREEN + " Create a farm \n";
        text += ChatColor.YELLOW + "/kelp remove <name> " + ChatColor.GREEN + " Remove a farm \n";
        text += ChatColor.YELLOW + "/kelp test" + ChatColor.GREEN + " Check if you are standing in a farm \n";
        text += ChatColor.WHITE + "====================================";
        p.sendMessage(text);
    }

    private void isInFarm(Player p) {
        Location l = p.getLocation();

        CacheRegion reg = this.getMgr().getCacheRegion(l);

        if (reg != null) {
            p.sendMessage("You " + ChatColor.GREEN + "are " + ChatColor.WHITE + "in a kelp farm "
                    + ChatColor.YELLOW + reg.name + ChatColor.WHITE + " owner by "
                    + ChatColor.YELLOW + reg.getOwnerName());
            return;
        }
        p.sendMessage("You " + ChatColor.RED + "are not " + ChatColor.WHITE + "in a kelp farm");
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (!(sender instanceof Player)) {
            return list;
        }

        if (!command.getName().equalsIgnoreCase("kelp")) {
            return list;
        }

        if (args.length < 1 || args[0].isEmpty() || args[0].length() < 3) {
            list.add("set");
//            list.add("age");
            list.add("test");
            list.add("create");
            list.add("remove");
            return list;
        }

        String first = args[0];

        switch (first.toLowerCase()) {
            case "set":
                list.add("1");
                list.add("2");
                break;
            case "remove":
                Player p = (Player) sender;
                UUID id = p.getUniqueId();

                Collection<CacheRegion> regs = this.getMgr().getRegions();

                Stream<String> r = regs.stream()
                        .filter(i -> i.ownerId.equals(id))
                        .map(x -> x.name);

                list = r.collect(Collectors.toList());
                break;
        }

        return list;
    }
}
