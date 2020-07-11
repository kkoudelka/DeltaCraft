package eu.quantumsociety.deltacraft.listeners

import eu.quantumsociety.deltacraft.DeltaCraft
import org.bukkit.Material
import org.bukkit.block.Campfire
import org.bukkit.block.data.Directional
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent

class CampfireListener(private val plugin: DeltaCraft) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onBlockDispenseEvent(event: BlockDispenseEvent) {
        val emitter = event.block
        if (emitter.type != Material.DISPENSER) {
            return
        }

        val emitterDirectional = emitter.blockData as Directional
        val direction = emitterDirectional.facing.direction
        val destination = emitter.location.clone()
                .add(direction).block


        if (destination.type != Material.CAMPFIRE &&
                destination.type != Material.SOUL_CAMPFIRE) {
            return
        }

        val item = event.item.clone()
        val type = item.type
        if (type != Material.BEEF &&
                type != Material.CHICKEN &&
                type != Material.RABBIT &&
                type != Material.PORKCHOP &&
                type != Material.MUTTON &&
                type != Material.COD &&
                type != Material.SALMON &&
                type != Material.POTATO &&
                type != Material.KELP) {
            return
        }

        if (destination.state !is Campfire) {
            return
        }

        val campfire = destination.state as Campfire
        var pos = -1
        when {
            campfire.getItem(0) == null -> {
                pos = 0
            }
            campfire.getItem(1) == null -> {
                pos = 1
            }
            campfire.getItem(2) == null -> {
                pos = 2
            }
            campfire.getItem(3) == null -> {
                pos = 3
            }
        }
        if (pos >= 0) {

            item.amount = 1
            campfire.setItem(pos, item)
            plugin.server.scheduler.runTaskLater(plugin, Runnable {
                campfire.update(false)
                campfire.setCookTimeTotal(pos, 600)
            }, 1)
        }
    }
}