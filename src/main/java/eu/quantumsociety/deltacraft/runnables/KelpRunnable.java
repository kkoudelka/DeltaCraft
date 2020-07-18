package eu.quantumsociety.deltacraft.runnables;

import eu.quantumsociety.deltacraft.managers.cache.KelpCacheManager;
import eu.quantumsociety.deltacraft.utils.BlockHelper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;

public class KelpRunnable extends BukkitRunnable {

    private final KelpCacheManager manager;
    private final Block originalBlock;

    public KelpRunnable(KelpCacheManager manager, Block b) {
        this.originalBlock = b;
        this.manager = manager;
    }

    @Override
    public void run() {
        Block b = BlockHelper.getTopKelp(originalBlock);
        this.manager.debugMsg("Block with age 25 set to 1 on: " + b.getLocation().toString());
        if (b.getType() != Material.KELP) {
            return;
        }
        BlockData bd = b.getBlockData();
        if (!(bd instanceof Ageable)) {
            return;
        }
        Ageable a = (Ageable) bd;
        a.setAge(1);

        b.setBlockData(a);
    }
}
