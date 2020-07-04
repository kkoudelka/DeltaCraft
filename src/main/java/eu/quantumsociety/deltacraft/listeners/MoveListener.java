package eu.quantumsociety.deltacraft.listeners;


import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CacheRegion;
import eu.quantumsociety.deltacraft.managers.DeltaCraftManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class MoveListener implements Listener {
    private final DeltaCraft plugin;

    public MoveListener(DeltaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void OnMoveEvent(PlayerMoveEvent e) {
        if (e == null) {
            return;
        }
        Player p = e.getPlayer();
        Location l = p.getLocation();

        DeltaCraftManager manager = this.plugin.getManager();

        CacheRegion reg = manager.getCacheRegion(l);

        if (reg == null) {
            return;
        }

        p.sendMessage("Region: " + reg.name);
    }

}
