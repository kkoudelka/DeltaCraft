package eu.quantumsociety.deltacraft.listeners;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.managers.DeltaCraftManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class AfkMoveListener implements Listener {

    private final DeltaCraft plugin;

    private DeltaCraftManager getMgr() {
        return this.plugin.getManager();
    }

    public AfkMoveListener(DeltaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onAfkMove(PlayerMoveEvent e) {
        if (e == null) {
            return;
        }
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();

        if (!this.getMgr().isPlayerAfk(id)) {
            return;
        }

        p.sendMessage("You're AFK, use " + ChatColor.YELLOW + " /kfc (or /afk)" + ChatColor.YELLOW + " to unstuck");
        e.setCancelled(true);
    }

}
