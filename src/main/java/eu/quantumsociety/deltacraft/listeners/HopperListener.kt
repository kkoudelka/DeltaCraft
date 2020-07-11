package eu.quantumsociety.deltacraft.listeners

import eu.quantumsociety.deltacraft.DeltaCraft
import org.bukkit.Material
import org.bukkit.block.Hopper
import org.bukkit.block.data.Directional
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryType

class HopperListener(private val plugin: DeltaCraft): Listener {

    @EventHandler
    fun onHopperInteractEvent(e: InventoryMoveItemEvent) {
        val source = e.source
        // Only hoppers
        if (source.type != InventoryType.HOPPER ||
                source.holder !is Hopper) {
            return
        }
        // Only rotten flesh
        if (e.item.type != Material.ROTTEN_FLESH) {
            return
        }
        if (source.location == null) {
            return
        }
        val hopperB = source.location!!.block
        val hopper = hopperB.blockData as Directional
        val direction = hopper.facing.direction
        val destination = hopperB.location.clone()
                .add(direction).block

        // Only composters
        if (destination.type != Material.COMPOSTER) {
            return
        }
        //val success: Boolean = this.compostBlock(e.item, destination, false)
//        if (success) {
//            //e.setCancelled(true);
//            plugin.server.scheduler.runTaskLater(plugin, Runnable { e.source.removeItem(e.item) }, 1)
//        }
    }

}