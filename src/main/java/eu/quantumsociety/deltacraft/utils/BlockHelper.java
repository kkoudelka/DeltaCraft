package eu.quantumsociety.deltacraft.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlockHelper {

    public static Block getTopKelp(Block b) {
        if (b.getType() == Material.KELP) {
            return b;
        }
        if (b.getType() != Material.KELP_PLANT) {
            return b;
        }
        return getTopKelp(b.getRelative(BlockFace.UP));
    }
}
