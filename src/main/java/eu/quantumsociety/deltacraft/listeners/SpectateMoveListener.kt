package eu.quantumsociety.deltacraft.listeners

import eu.quantumsociety.deltacraft.DeltaCraft
import eu.quantumsociety.deltacraft.managers.cache.SpectateCacheManager
import eu.quantumsociety.deltacraft.utils.enums.Permissions
import eu.quantumsociety.deltacraft.utils.enums.Settings
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class SpectateMoveListener(private val plugin: DeltaCraft) : Listener {
    private val mgr: SpectateCacheManager
        get() = this.plugin.manager.spectateCacheManager

    @EventHandler(ignoreCancelled = true)
    fun OnMoveEvent(e: PlayerMoveEvent?) {
        if (e == null) {
            return
        }
        val p = e.player
        val id = p.uniqueId
        if (!mgr.isPlayerSpectating(id)) {
            return
        }
        if (p.hasPermission(Permissions.SPECTATEUNLIMITED.path)) {
            return
        }
        val maxDistance = plugin.config.getDouble(Settings.SPECTATEMAXDISTANCE.path)
        val cache = mgr.get(id) ?: return

        val origin = cache.originalLocation
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