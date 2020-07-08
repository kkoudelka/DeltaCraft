package eu.quantumsociety.deltacraft.listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ComposterListener implements Listener {

    private final Random random;

    public ComposterListener() {
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

        Levelled composter = (Levelled) b.getBlockData();
        if (composter.getLevel() != composter.getMaximumLevel()) {
            compostBlock(p, b, composter);
            e.setCancelled(true);
        }
    }

    private void compostBlock(Player p, Block composter, Levelled state) {
        int max = state.getMaximumLevel();
        int level = state.getLevel();

        double rnd = random.nextDouble();
        boolean isFilled = false;
        double fleshCompostChance = 0.4;
        if (rnd <= fleshCompostChance) {
            // Compost
            level++;
            state.setLevel(level);
            composter.setBlockData(state);

            isFilled = level == max;
        }
        // Play sound
        composter.getWorld().playSound(composter.getLocation(), (isFilled) ? Sound.BLOCK_COMPOSTER_FILL : Sound.BLOCK_COMPOSTER_FILL_SUCCESS, 1, 1);

        int amount = p.getInventory().getItemInMainHand().getAmount();
        if (p.getGameMode() != GameMode.CREATIVE) {
            p.getInventory().getItemInMainHand().setAmount(amount - 1);
        }
        composter.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, composter.getLocation().clone().add(0.5, 0.6, 0.5), 10, 0.25, 0.2, 0.25);

    }
}
