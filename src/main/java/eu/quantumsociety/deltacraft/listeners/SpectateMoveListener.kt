package eu.quantumsociety.deltacraft.listeners

import eu.quantumsociety.deltacraft.DeltaCraft
import eu.quantumsociety.deltacraft.utils.MathHelper
import eu.quantumsociety.deltacraft.utils.enums.Permissions
import eu.quantumsociety.deltacraft.utils.enums.Settings
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class SpectateMoveListener(private val plugin: DeltaCraft) : Listener {
    @EventHandler(ignoreCancelled = true)
    fun OnMoveEvent(e: PlayerMoveEvent?) {
        if (e == null) {
            return
        }
        val p = e.player
        val id = p.uniqueId
        val manager = plugin.manager
        if (!manager.isPlayerInCache(id)) {
            return
        }
        if (p.hasPermission(Permissions.SPECTATEUNLIMITED.getName())) {
            return
        }
        val maxDistance = plugin.config.getDouble(Settings.SPECTATEMAXDISTANCE.path)
        val cache = manager.getCachePlayer(id)
        val origin = cache.originalLocation
        val curr = p.location
/*        var distance = 0.0
        distance = try {
            MathHelper.calcDistance(origin, curr)
        } catch (ex: Exception) {
            plugin.logger.warning(ex.toString())
            p.sendMessage(ChatColor.RED.toString() + "ERROR in calculating distance: " + ex.toString())
            return
        }*/

        val distance = origin.distance(p.location)


        if (distance < maxDistance) {
            return
        }
        p.sendMessage(ChatColor.RED.toString() + "You've reached maximum distance (" + maxDistance + ")")
        // p.teleport(origin)
        val pDirection = p.location.direction

        val d = origin.clone().subtract(p.eyeLocation.toVector()).toVector()
        val l = p.location.setDirection(d)
        l.add(d.normalize().multiply(2))
        p.teleport(l)

        val newLoc = p.location.setDirection(pDirection)

        p.teleport(newLoc)






        return


//        e.setCancelled(true);
    }

}