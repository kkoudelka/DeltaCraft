package eu.quantumsociety.deltacraft.commands.spectate;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.managers.DeltaCraftManager;
import eu.quantumsociety.deltacraft.managers.SpectateManager;
import eu.quantumsociety.deltacraft.utils.KeyHelper;
import eu.quantumsociety.deltacraft.utils.enums.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpectateCommand implements CommandExecutor {
    private final SpectateManager configManager;
    private final DeltaCraft plugin;

    private DeltaCraftManager getMgr() {
        return this.plugin.getManager();
    }

    public SpectateCommand(SpectateManager dataMgr, DeltaCraft plugin) {

        this.configManager = dataMgr;
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this commands");
            return false;
        }
        Player p = (Player) sender;

        if (!p.hasPermission(Permissions.SPECTATEUSE.getName())) {
            p.sendMessage(ChatColor.RED + "You don't have permission to use spectator mode!");
            return true;
        }
        return executeSwitch(p);
    }

    private boolean executeSwitch(Player p) {
        KeyHelper keys = new KeyHelper(p.getUniqueId());

        boolean exists = this.configManager.exists(keys);
        if (!exists) {
            return switchToSpectate(p, keys);
        }

        return switchBack(p, keys);
    }

    private boolean switchBack(Player p, KeyHelper keys) {
        Location l = this.configManager.getLocation(keys);
        GameMode g = this.configManager.getGamemode(keys);

        return switchBack(p, l, g);
    }

    private boolean switchBack(Player p, Location l, GameMode gm) {
        p.teleport(l);

        p.setGameMode(gm);

        UUID id = p.getUniqueId();

        this.getMgr().removeCachePlayer(id);
        this.configManager.delete(id);

        p.sendMessage(ChatColor.YELLOW + "You are no longer Spectating!");

        return true;
    }

    private boolean switchToSpectate(Player p, KeyHelper keys) {

        Location loc = p.getLocation();
        GameMode gm = p.getGameMode();

        this.configManager.save(keys, loc, gm);

        this.getMgr().addCachePlayer(p, loc, gm);

        p.setGameMode(GameMode.SPECTATOR);

        p.sendMessage(ChatColor.GREEN + "You are now Spectating!");

        return true;
    }
}
