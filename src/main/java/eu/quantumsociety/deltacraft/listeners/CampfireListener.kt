package eu.quantumsociety.deltacraft.listeners

import eu.quantumsociety.deltacraft.DeltaCraft
import org.bukkit.Material
import org.bukkit.block.*
import org.bukkit.block.data.Directional
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.inventory.ItemStack

class CampfireListener(private val plugin: DeltaCraft): Listener {

    private fun fillCampfire(campfire: Campfire, item: ItemStack) {

    }

    @EventHandler
    fun onBlockDispenseEvent(event: BlockDispenseEvent) {

        // Teď sen používá - mělo být na samostatné branchi feature/campfire, ta se ale nevytvořila a já to pushnul
        // do devu. Všiml jsem si až když to bylo pushnuté
        return


        val emitter = event.block

        if (emitter.type != Material.DISPENSER && emitter.type != Material.DROPPER) {
            return
        }

        val emitterDirectional = emitter.blockData as Directional
        val direction = emitterDirectional.facing.direction

        val destination = emitter.location.clone()
                .add(direction).block


        if (destination.type != Material.CAMPFIRE
                && destination.type != Material.SOUL_CAMPFIRE) {

            return
        }


        val item = event.item.clone()

        val campfire = destination.state as Campfire


        //campfire.setItem(0, ItemStack(Material.CHICKEN, 1))

        //campfire.update(false)

        val emitterD = emitter.state as Dispenser

        val dispenserInv = emitterD.inventory



        val itemSlot = emitterD.inventory.first(item)
        val slotStack = dispenserInv.getItem(itemSlot)!!

        if(slotStack.amount > 1) {
            slotStack.amount = slotStack.amount -1;
            dispenserInv.setItem(itemSlot, slotStack);
        } else {
            dispenserInv.clear(itemSlot);
        }

        event.isCancelled = true

        val itemEntity = event.item as Entity
        itemEntity.teleport(itemEntity.location.add(0.0,20.0,0.0))

    }
}