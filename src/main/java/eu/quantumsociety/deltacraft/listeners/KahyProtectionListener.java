package eu.quantumsociety.deltacraft.listeners;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.utils.Extensions;
import eu.quantumsociety.deltacraft.utils.TextHelper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class KahyProtectionListener implements Listener {

    private final DeltaCraft plugin;

    public KahyProtectionListener(DeltaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onKahyDamage(EntityDamageByEntityEvent e) {
        Entity ent = e.getEntity();
        Entity damager = e.getDamager();
        if (!(ent instanceof Player)) {
            return;
        }
        if (!(damager instanceof Player)) {
            return;
        }
        Player kahy = (Player) damager;
        if (!Extensions.isKahy(kahy)) {
            return;
        }
        Player p = (Player) ent;
        if (!Extensions.hasProtection(p)) {
            return;
        }

        kahy.damage(e.getDamage());
        kahy.spigot().sendMessage(TextHelper.attentionText("You have damaged player under protection"));

        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void odProtectedMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (!Extensions.hasProtection(p)) {
            return;
        }

        p.removeMetadata(Extensions.kahyProtectionKey, plugin);
        p.spigot().sendMessage(TextHelper.attentionText("Kahy protection disabled because you have moved"));
    }

    @EventHandler(ignoreCancelled = true)
    public void odProtectedTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (!Extensions.hasProtection(p)) {
            return;
        }

        p.removeMetadata(Extensions.kahyProtectionKey, plugin);
        p.spigot().sendMessage(TextHelper.attentionText("Kahy protection disabled because you have teleported"));
    }
}
