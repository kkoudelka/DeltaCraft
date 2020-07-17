package eu.quantumsociety.deltacraft.commands.kelp;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.KelpFarm;
import eu.quantumsociety.deltacraft.managers.KelpManager;
import eu.quantumsociety.deltacraft.managers.cache.KelpCacheManager;
import eu.quantumsociety.deltacraft.utils.TextHelper;
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

public class KelpCommand implements CommandExecutor, TabCompleter {
    private final KelpManager manager;
    private final DeltaCraft plugin;

    private KelpCacheManager getCacheMgr() {
        return this.manager.getManager();
    }

    public KelpCommand(KelpManager dataMgr, DeltaCraft plugin) {
        this.manager = dataMgr;
        this.plugin = plugin;

        FileConfiguration config = this.manager.getConfig();
        config.set("players", null);
        this.manager.saveConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this commands");
            return false;
        }
        Player p = (Player) sender;

        if (!p.hasPermission(Permissions.KELPFARMUSE.getPath())) {
            p.spigot().sendMessage(TextHelper.insufficientPermissions(Permissions.KELPFARMUSE));
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
                    this.setPointOne(p);
                    break;
                case "2":
                case "two":
                case "second":
                    this.setPointTwo(p);
                    break;
                default:
                    p.sendMessage(ChatColor.YELLOW + "You can only set point '1' or '2'");
                    return true;
            }

            if (this.pointsAreSet(p)) {
                p.sendMessage(ChatColor.GREEN + "Well done! You can now create farm by " + ChatColor.YELLOW + "/kelp create <name of the farm>");
            }
            return true;
        }

        if (cmd.equalsIgnoreCase("create")) {
            if (!p.hasPermission(Permissions.KELPFARMCREATE.getPath())) {
                p.spigot().sendMessage(TextHelper.insufficientPermissions(Permissions.KELPFARMCREATE));
                return true;
            }
            this.createFarm(p, arg);
            return true;
        }

        if (cmd.equalsIgnoreCase("delete") || cmd.equalsIgnoreCase("remove")) {
            if (!p.hasPermission(Permissions.KELPFARMREMOVE.getPath())) {
                p.spigot().sendMessage(TextHelper.insufficientPermissions(Permissions.KELPFARMREMOVE));
                return true;
            }
            return this.deleteFarm(p, arg);
        }

        if (cmd.equalsIgnoreCase("age")) {
            if (!p.hasPermission(Permissions.KELPFARMSETAGE.getPath())) {
                p.spigot().sendMessage(TextHelper.insufficientPermissions(Permissions.KELPFARMSETAGE));
                return true;
            }
            int age = 0;
            try {
                age = Integer.parseInt(arg);
            } catch (NumberFormatException ignored) {
                p.spigot().sendMessage(TextHelper.attentionText("This is not valid number"));
            }
            this.setAge(p, age);
        }
        return true;
    }

    private void setPointOne(Player p) {
        saveTempLoc(p, this.manager.PointOneKey, "1");
    }

    private void setPointTwo(Player p) {
        saveTempLoc(p, this.manager.PointTwoKey, "2");
    }

    private void saveTempLoc(Player p, String key, String pointName) {
        if (this.isSpectating(p)) {
            p.spigot().sendMessage(TextHelper.infoText("You cannot set point while spectating"));
            return;
        }

        Location loc = p.getLocation();
        if (this.getCacheMgr().isInKelpFarm(loc)) {
            p.spigot().sendMessage(TextHelper.infoText("This location is already in farm"));
            return;
        }

        this.manager.saveTempLocation(p.getUniqueId(), loc, key);
        p.sendMessage(ChatColor.GREEN + "Point " + ChatColor.YELLOW + pointName + ChatColor.GREEN + " saved");
    }

    private boolean pointsAreSet(Player p) {
        return this.pointsAreSet(p.getUniqueId());
    }

    private boolean pointsAreSet(UUID id) {
        Location one = this.manager.getPointOne(id);
        if (one == null) {
            return false;
        }
        Location two = this.manager.getPointTwo(id);
        //noinspection RedundantIfStatement
        if (two == null) {
            return false;
        }
        return true;
    }

    private void createFarm(Player p, String name) {
        UUID playerId = p.getUniqueId();
        if (this.isSpectating(p)) {
            p.spigot().sendMessage(TextHelper.infoText("You cannot set home while spectating"));
            return;
        }

        int maxFarms = this.plugin.getConfig().getInt(Settings.KELPMAXFARMS.getPath());
        int existing = this.getCacheMgr().getKelpFarmCount(playerId);
        if (existing >= maxFarms) {
            p.spigot().sendMessage(TextHelper.infoText("You have reached quota of +" + maxFarms + " farms"));
            return;
        }

        if (this.manager.farmExists(name)) {
            p.spigot().sendMessage(TextHelper.infoText("Farm with this name already exists"));
            return;
        }

        Location one = this.manager.getPointOne(playerId);
        if (one == null) {
            p.sendMessage(ChatColor.RED + "Point 1 is not set");
            return;
        }
        Location two = this.manager.getPointTwo(playerId);
        if (two == null) {
            p.sendMessage(ChatColor.RED + "Point 2 is not set");
            return;
        }

        double maxDistance = this.plugin.getConfig().getDouble(Settings.KELPMAXDISTANCE.getPath());
        double distance;
        try {
            distance = one.distance(two);
        } catch (IllegalArgumentException ex) {
            p.sendMessage(ChatColor.RED + "Points cannot be in a different worlds!");
            return;
        }

        if (distance > maxDistance) {
            p.sendMessage(ChatColor.RED + "Maximum distance between blocks is " + maxDistance + ". Your distance is " + distance);
            return;
        }

        this.manager.addFarm(one, two, name, playerId);

        p.sendMessage(ChatColor.GREEN + "Farm " + ChatColor.YELLOW + name + ChatColor.GREEN + " successfully created");
    }

    private boolean deleteFarm(Player p, String name) {
        UUID playerId = p.getUniqueId();

        String ownerId = this.manager.getOwnerId(name);
        if (!playerId.toString().equalsIgnoreCase(ownerId)) {
            p.sendMessage(ChatColor.RED + "You are not owner");
            return true;
        }

        this.manager.removeFarm(name);

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

        KelpFarm reg = this.getCacheMgr().getKelpFarm(l);

        if (reg != null) {
            p.sendMessage("You " + ChatColor.GREEN + "are " + ChatColor.WHITE + "in a kelp farm "
                    + ChatColor.YELLOW + reg.name + ChatColor.WHITE + " owner by "
                    + ChatColor.YELLOW + reg.getOwnerName());
            return;
        }
        p.sendMessage("You " + ChatColor.RED + "are not " + ChatColor.WHITE + "in a kelp farm");
    }

    private boolean isSpectating(Player p) {
        return this.isSpectating(p.getUniqueId());
    }

    private boolean isSpectating(UUID id) {
        return this.plugin.getManager().getSpectateCacheManager().isPlayerSpectating(id);
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

        if (args.length < 2 || args[0].isEmpty()) {
            String typedIn = "";
            if (args.length == 1) {
                typedIn = args[0].toLowerCase();
            }
            String[] cmds = {"set", "test", "create", "remove"};

            for (String cmd : cmds) {
                if (cmd.startsWith(typedIn)) {
                    list.add(cmd);
                }
            }
            return list;
        }

        String first = args[0].toLowerCase();

        switch (first) {
            case "set":
                list.add("1");
                list.add("2");
                break;
            case "remove":
                String typedIn = "";
                if (args.length > 1) {
                    typedIn = args[1].toLowerCase();
                }

                Player p = (Player) sender;
                UUID id = p.getUniqueId();

                List<String> names = this.getCacheMgr().getKelpFarmNames(id);
                for (String name : names) {
                    if (name.toLowerCase().startsWith(typedIn)) {
                        list.add(name);
                    }
                }
                break;
        }

        return list;
    }
}
