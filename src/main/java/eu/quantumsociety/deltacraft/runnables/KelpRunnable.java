package eu.quantumsociety.deltacraft.runnables;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.utils.BlockHelper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;

public class KelpRunnable extends BukkitRunnable {

    private final DeltaCraft plugin;
    private final Block originalBlock;

    public KelpRunnable(DeltaCraft plugin, Block b) {
        this.plugin = plugin;
        this.originalBlock = b;
    }

    @Override
    public void run() {
        Block b = BlockHelper.getTopKelp(originalBlock);
        this.plugin.debugMsg("Block with age 25 set to 1 on: " + b.getLocation().toString());
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
