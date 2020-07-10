package eu.quantumsociety.deltacraft.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("SpellCheckingInspection")
public class ShulkerKillListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onShulkerKill(EntityDeathEvent e) {
        if (e == null) {
            return;
        }
        LivingEntity ent = e.getEntity();
        if (ent == null) {
            return;
        }
        if (ent.getType() != EntityType.SHULKER) {
            return;
        }

        ItemStack toDrop = new ItemStack(Material.SHULKER_SHELL, 2);

        e.getDrops().clear();
        e.getDrops().add(toDrop);

    }
}
