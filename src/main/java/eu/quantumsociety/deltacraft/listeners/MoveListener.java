package eu.quantumsociety.deltacraft.listeners;


import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.KelpFarm;
import eu.quantumsociety.deltacraft.managers.cache.KelpCacheManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class MoveListener implements Listener {
    private final DeltaCraft plugin;

    private KelpCacheManager getMgr() {
        return this.plugin.getManager().getKelpCacheManager();
    }

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

        KelpFarm reg = this.getMgr().getKelpFarm(l);

        if (reg == null) {
            return;
        }

        p.sendMessage("Region: " + reg.name);
    }

}
