package eu.quantumsociety.deltacraft.listeners;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CacheRegion;
import eu.quantumsociety.deltacraft.managers.DeltaCraftManager;
import eu.quantumsociety.deltacraft.runnables.KelpRunnable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class KelpGrowListener implements Listener {

    private final DeltaCraft plugin;

    private DeltaCraftManager getMgr() {
        return this.plugin.getManager();
    }

    public KelpGrowListener(DeltaCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void OnBlockGrow(BlockSpreadEvent e) {
        if (e == null) {
            return;
        }

        Block b = e.getSource();
        if (b.getType() != Material.KELP) {
            return;
        }

//        this.plugin.debugMsg(b.getType().toString()); // KELP
//        this.plugin.debugMsg(b.getBlockData().getAsString()); //minecraft:kelp[age=1]

        Location loc = b.getLocation();

        if (this.getMgr().isInKelpFarm(loc)) {
            return;
        }

        BlockData bd = e.getNewState().getBlockData();
        if (!(bd instanceof Ageable)) {
            this.plugin.debugMsg(bd.toString() + " is not ageable");
            return;
        }
        Ageable a = (Ageable) bd;

        if (a.getAge() == 25) {
            this.plugin.debugMsg("Repairing kelp in location " + loc.toString());
            // Run with delay 1 tick
            new KelpRunnable(this.plugin, b).runTaskLater(this.plugin, 1);
        }


    }
}
