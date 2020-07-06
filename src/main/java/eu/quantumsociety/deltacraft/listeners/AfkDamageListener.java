package eu.quantumsociety.deltacraft.listeners;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CacheAfk;
import eu.quantumsociety.deltacraft.managers.DeltaCraftManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class AfkDamageListener implements Listener {

    private final DeltaCraft plugin;

    private DeltaCraftManager getMgr() {
        return this.plugin.getManager();
    }

    public AfkDamageListener(DeltaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onAfkDamage(EntityDamageEvent e) {
        if (e == null) {
            return;
        }
        Entity ent = e.getEntity();
        if (!(ent instanceof Player)) {
            return;
        }

        Player p = (Player) ent;
        UUID id = p.getUniqueId();

        CacheAfk cache = this.getMgr().getAfkPlayer(id);
        if (cache == null) {
            return;
        }

        // this makes player invincible
        e.setCancelled(true);
    }
}
