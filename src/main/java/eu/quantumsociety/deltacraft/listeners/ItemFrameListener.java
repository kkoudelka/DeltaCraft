package eu.quantumsociety.deltacraft.listeners;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.managers.cache.ItemFrameCacheManager;
import eu.quantumsociety.deltacraft.utils.TextHelper;
import eu.quantumsociety.deltacraft.utils.enums.Permissions;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

@SuppressWarnings("DuplicatedCode")
public class ItemFrameListener implements Listener {

    private final ItemFrameCacheManager manager;

    public ItemFrameListener(DeltaCraft plugin) {
        manager = plugin.getManager().getItemFrameCacheManager();
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemFramePlace(HangingPlaceEvent e) {
        Entity placed = e.getEntity();
        if (!(placed instanceof ItemFrame)) {
            return;
        }
        Player p = e.getPlayer();
        if (p == null) {
            return;
        }
        UUID id = p.getUniqueId();
        if (!manager.contains(id)) {
            return;
        }
        if (!this.checkPermissions(p)) {
            return;
        }

        ItemFrame frame = (ItemFrame) placed;
        frame.setVisible(false);
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (!(entity instanceof ItemFrame)) {
            return;
        }
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        if (!manager.contains(id)) {
            return;
        }
        if (!this.checkPermissions(p)) {
            return;
        }
        if (!p.isSneaking()) {
            return;
        }
        if (p.getInventory().getItemInMainHand().getType() != Material.AIR){
            return;
        }

        ItemFrame frame = (ItemFrame) entity;
        frame.setVisible(!frame.isVisible());
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        if (!manager.contains(id)) {
            return;
        }
        manager.removeItem(id);
    }

    private boolean checkPermissions(Player p) {
        if (!p.hasPermission(Permissions.ITEMFRAMEUSE.getPath())) {
            manager.removeItem(p.getUniqueId());
            p.spigot().sendMessage(TextHelper.insufficientPermissions(Permissions.ITEMFRAMEUSE));
            return false;
        }
        return true;
    }


}
