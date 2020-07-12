package eu.quantumsociety.deltacraft.listeners

import eu.quantumsociety.deltacraft.DeltaCraft
import org.bukkit.Material
import org.bukkit.block.Campfire
import org.bukkit.block.Dispenser
import org.bukkit.block.data.Directional
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.inventory.ItemStack

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
        var position = -1
        when {
            campfire.getItem(0) == null -> {
                position = 0
            }
            campfire.getItem(1) == null -> {
                position = 1
            }
            campfire.getItem(2) == null -> {
                position = 2
            }
            campfire.getItem(3) == null -> {
                position = 3
            }
        }

        if (position >= 0) {
            item.amount = 1

            campfire.setCookTime(position, 0)
            campfire.setCookTimeTotal(position, 600)
            campfire.setItem(position, item)
            campfire.update()

            event.isCancelled = true

            plugin.server.scheduler.runTaskLater(plugin, Runnable {
                val origState = event.block.state
                if (origState is Dispenser) {
                    val inv = origState.inventory;
                    inv.removeItem(ItemStack(event.item.type, 1))
                }
            }, 2)
        }
    }
}