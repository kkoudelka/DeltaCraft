package eu.quantumsociety.deltacraft.listeners;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.classes.CacheRegion;
import eu.quantumsociety.deltacraft.managers.DeltaCraftManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
        Material type = b.getType();

        if (type != Material.KELP && type != Material.KELP_PLANT) {
            return;
        }

//        this.plugin.debugMsg(b.getType().toString()); // KELP
//        this.plugin.debugMsg(b.getBlockData().getAsString()); //minecraft:kelp[age=1]

        BlockData bd = e.getSource().getBlockData();
        if (!(bd instanceof Ageable)) {
            this.plugin.debugMsg("Is not ageable");
            return;
        }
        Ageable a = (Ageable) e.getSource().getBlockData();;

        int age = a.getAge() + 1;
//        this.plugin.debugMsg("" + age + ": " + b.getLocation().toString());
        if (age == 25) {
            a.setAge(1);
            e.getSource().setBlockData(a);
//            Block top = this.getTop(b);
//            this.setAge(top);
            this.plugin.debugMsg("Block with age 25 set to 1 on: " + b.getLocation().toString());
            this.plugin.debugMsg(a.toString());
        }

        Location loc = b.getLocation();

        CacheRegion reg = this.getMgr().getCacheRegion(loc);

        if (reg == null) {
            return;
        }


    }

    private Block getTop(Block b) {
        if (b.getType() == Material.KELP) {
            return b;
        }
        if (b.getType() != Material.KELP_PLANT) {
            return b;
        }
        return this.getTop(b.getRelative(BlockFace.UP, 1));
    }

    private void setAge(Block b) {
        if (b.getType() != Material.KELP) {
            return;
        }
        BlockData bd = b.getBlockData();
        if (!(bd instanceof Ageable)) {
            this.plugin.debugMsg("Is not ageable");
            return;
        }
        Ageable a = (Ageable) bd;

        a.setAge(1);
    }

}
