package eu.quantumsociety.DeltaCraft.listeners;

import eu.quantumsociety.DeltaCraft.DeltaCraft;
import eu.quantumsociety.DeltaCraft.classes.CachePlayer;
import eu.quantumsociety.DeltaCraft.managers.DeltaCraftManager;
import eu.quantumsociety.DeltaCraft.utils.enums.Permissions;
import eu.quantumsociety.DeltaCraft.utils.enums.Settings;
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

        if (!manager.isInCache(id)) {
            return;
        }

//        if (p.isOp()) {
//            return;
//        }

        if (p.hasPermission(Permissions.UNLIMITEDDISTANCE.getName())) {
            return;
        }

        double maxDistance = this.plugin.getConfig().getDouble(Settings.MAXDISTANCE.getPath());

        CachePlayer cache = manager.getCachePlayer(id);
        Location origin = cache.getOriginalLocation();
        Location curr = p.getLocation();

        // d = sqrt((x2-x1)^2 + (y2-y1)^2 + (z2-z1)^2)

        double xDiff = curr.getX() - origin.getX();
        double yDiff = curr.getY() - origin.getY();
        double zDiff = curr.getZ() - origin.getZ();

        double x = xDiff * xDiff;
        double y = yDiff * yDiff;
        double z = zDiff * zDiff;

        double distance = Math.sqrt(x + y + z);

        if (distance < maxDistance) {
            return;
        }
        p.sendMessage("Distance: " + distance);
        p.sendMessage("You've reached maximum distance (" + maxDistance + ")");
        p.teleport(origin);
//        e.setCancelled(true);
    }

}
