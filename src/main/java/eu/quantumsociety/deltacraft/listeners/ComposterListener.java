package eu.quantumsociety.deltacraft.listeners;

import eu.quantumsociety.deltacraft.DeltaCraft;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class ComposterListener implements Listener {

    private final Random random;
    private final DeltaCraft plugin;

    public ComposterListener(DeltaCraft plugin) {
        this.plugin = plugin;
        this.random = new Random();
    }

    @EventHandler(ignoreCancelled = true)
    public void onComposterInteract(PlayerInteractEvent e) {
        if (e == null) {
            return;
        }
        Block b = e.getClickedBlock();
        if (b == null) {
            return;
        }
        // Only right click
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        // Only composters
        if (b.getType() != Material.COMPOSTER) {
            return;
        }
        Player p = e.getPlayer();
        // Not sneaking
        if (p.isSneaking()) {
            return;
        }
        // Only hand
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        // Only levelled block (composter)
        if (!(b.getBlockData() instanceof Levelled)) {
            return;
        }
        // Only rotten flesh
        if (p.getInventory().getItemInMainHand().getType() != Material.ROTTEN_FLESH) {
            return;
        }

        boolean success = compostBlock(p.getInventory().getItemInMainHand(), b, p.getGameMode() != GameMode.CREATIVE);
        e.setCancelled(success);
    }

    @EventHandler(ignoreCancelled = true)
    public void onHopperInteractEvent(InventoryMoveItemEvent e) {
        Inventory source = e.getSource();
        // Only hoppers
        if (source.getType() != InventoryType.HOPPER ||
                !(source.getHolder() instanceof Hopper)) {
            return;
        }
        // Only rotten flesh
        if (e.getItem().getType() != Material.ROTTEN_FLESH) {
            return;
        }
        if (source.getLocation() == null) {
            return;
        }
        Block hopperB = source.getLocation().getBlock();
        Directional hopper = (Directional) hopperB.getBlockData();
        Vector direction = hopper.getFacing().getDirection();
        Block destination = hopperB.getLocation().clone()
                .add(direction).getBlock();

        // Only composters
        if (destination.getType() != Material.COMPOSTER) {
            return;
        }
        boolean success = this.compostBlock(e.getItem(), destination, false);

        if (success) {
            e.setCancelled(true);

            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> e.getSource().removeItem(e.getItem()), 1);
        }

    }

    private boolean compostBlock(ItemStack i, Block composter, boolean shouldRemove) {
        BlockData bd = composter.getBlockData();
        if (!(bd instanceof Levelled)) {
            return false;
        }
        Levelled state = (Levelled) bd;
        int max = state.getMaximumLevel();
        int level = state.getLevel();
        if (level == max) {
            return false;
        }

        double rnd = random.nextDouble();
        boolean isFilled = false;
        double fleshCompostChance = 0.65;
        if (rnd <= fleshCompostChance) {
            // Compost
            level++;
            state.setLevel(level);
            composter.setBlockData(state);

            isFilled = level == max;
        }
        // Play sound
        composter.getWorld().playSound(composter.getLocation(), (isFilled) ? Sound.BLOCK_COMPOSTER_FILL : Sound.BLOCK_COMPOSTER_FILL_SUCCESS, 1, 1);

        int amount = i.getAmount();
        if (shouldRemove) {
            i.setAmount(amount - 1);
        }
        composter.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, composter.getLocation().clone().add(0.5, 0.6, 0.5), 10, 0.25, 0.2, 0.25);
        return true;
    }
}
