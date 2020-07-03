package eu.quantumsociety.DeltaCraft.commands.kelp;

import eu.quantumsociety.DeltaCraft.DeltaCraft;
import eu.quantumsociety.DeltaCraft.managers.ConfigManager;
import eu.quantumsociety.DeltaCraft.managers.DeltaCraftManager;
import eu.quantumsociety.DeltaCraft.utils.enums.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KelpCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private final DeltaCraft plugin;

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


        return false;
    }

    private boolean setPointOne(Player p) {

        return true;
    }
}
