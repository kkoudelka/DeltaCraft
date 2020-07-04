package eu.quantumsociety.deltacraft.listeners;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CachePlayer;
import eu.quantumsociety.deltacraft.managers.DeltaCraftManager;
import eu.quantumsociety.deltacraft.utils.MathHelper;
import eu.quantumsociety.deltacraft.utils.enums.Permissions;
import eu.quantumsociety.deltacraft.utils.enums.Settings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class SpectateMoveListener implements Listener {
    private final DeltaCraft plugin;

    public SpectateMoveListener(DeltaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void OnMoveEvent(PlayerMoveEvent e) {
        if (e == null) {
            return;
        }
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();

        DeltaCraftManager manager = this.plugin.getManager();

        if (!manager.isPlayerInCache(id)) {
            return;
        }

        if (p.hasPermission(Permissions.UNLIMITEDDISTANCE.getName())) {
            return;
        }

        double maxDistance = this.plugin.getConfig().getDouble(Settings.MAXDISTANCE.getPath());

        CachePlayer cache = manager.getCachePlayer(id);
        Location origin = cache.getOriginalLocation();
        Location curr = p.getLocation();

        double distance = 0;
        try {
            distance = MathHelper.calcDistance(origin, curr);
        } catch (Exception ex) {
            plugin.getLogger().warning(ex.toString());
            p.sendMessage(ChatColor.RED + "ERROR in calculating distance: " + ex.toString());
            return;
        }

        if (distance < maxDistance) {
            return;
        }
        p.sendMessage(ChatColor.RED + "You've reached maximum distance (" + maxDistance + ")");
        p.teleport(origin);
//        e.setCancelled(true);
    }

}
