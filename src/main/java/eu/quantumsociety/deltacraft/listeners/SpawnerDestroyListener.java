package eu.quantumsociety.deltacraft.listeners;

import eu.quantumsociety.deltacraft.utils.TextHelper;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class SpawnerDestroyListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onSpawnerDestroy(BlockBreakEvent e) {
        if (e == null) {
            return;
        }
        Block b = e.getBlock();
        if (b.getType() != Material.SPAWNER) {
            return;
        }
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        ItemStack item = p.getInventory().getItemInMainHand();
        Material type = item.getType();
        if (type != Material.WOODEN_PICKAXE &&
                type != Material.STONE_PICKAXE &&
                type != Material.IRON_PICKAXE &&
                type != Material.GOLDEN_PICKAXE &&
                type != Material.DIAMOND_PICKAXE &&
                type != Material.NETHERITE_PICKAXE) {
            p.spigot().sendMessage(TextHelper.infoText("Spawners can only be destroyed by pickaxes"));
            e.setCancelled(true);
            return;
        }
        if (!item.containsEnchantment(Enchantment.SILK_TOUCH)) {
            p.spigot().sendMessage(TextHelper.infoText("Spawners can only be destroyed by pickaxes with silk touch"));
            e.setCancelled(true);
            return;
        }
        BlockState state = b.getState();
        if (!(state instanceof CreatureSpawner)) {
            return;
        }
        CreatureSpawner spawner = (CreatureSpawner) state;
        EntityType entityType = spawner.getSpawnedType();
        String name = entityType.name();

        ItemStack toDrop = new ItemStack(Material.SPAWNER, 1);
        ItemMeta meta = toDrop.getItemMeta();
        meta.setDisplayName(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase() + " spawner");
        meta.setLore(Arrays.asList(name));
        toDrop.setItemMeta(meta);

        e.setExpToDrop(0);

        b.getWorld().dropItemNaturally(b.getLocation(), toDrop);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSpawnerPlace(BlockPlaceEvent e) {
        if (e == null) {
            return;
        }
        Block b = e.getBlock();
        if (b.getType() != Material.SPAWNER) {
            return;
        }
        BlockState state = b.getState();
        if (!(state instanceof CreatureSpawner)) {
            return;
        }
        CreatureSpawner spawner = (CreatureSpawner) state;

        ItemMeta meta = e.getItemInHand().getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null || lore.size() < 1) {
            if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                e.setCancelled(true);
            }
            return;
        }
        String name = lore.get(0);
        EntityType type = EntityType.valueOf(name);
        spawner.setSpawnedType(type);
        spawner.update();
    }
}
