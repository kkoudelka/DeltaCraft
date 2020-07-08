package eu.quantumsociety.deltacraft.listeners

import eu.quantumsociety.deltacraft.DeltaCraft
import eu.quantumsociety.deltacraft.managers.cache.FakePlayerManager
import eu.quantumsociety.deltacraft.managers.cache.SpectateCacheManager
import eu.quantumsociety.deltacraft.utils.TextHelper
import eu.quantumsociety.deltacraft.utils.enums.Permissions
import eu.quantumsociety.deltacraft.utils.enums.Settings
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent


class SpectateListener(private val plugin: DeltaCraft) : Listener {
    private val spectateCacheManager: SpectateCacheManager
        get() = this.plugin.manager.spectateCacheManager
    private val fakePlayerManager: FakePlayerManager
        get() = this.plugin.manager.fakePlayerManager

    @EventHandler(ignoreCancelled = true)
    fun OnMoveEvent(e: PlayerMoveEvent?) {
        if (e == null) {
            return
        }
        val p = e.player
        val id = p.uniqueId
        if (!spectateCacheManager.isPlayerSpectating(id)) {
            return
        }
        if (p.hasPermission(Permissions.SPECTATEUNLIMITED.path)) {
            return
        }
        val maxDistance = plugin.config.getDouble(Settings.SPECTATEMAXDISTANCE.path)
        val cache = spectateCacheManager.get(id) ?: return

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

        teleportTowardsOrigin(p, origin, playerToOriginalDirection = true)
        return
    }

    fun teleportTowardsOrigin(player: Player, location: Location, blocks: Int = 2, playerToOriginalDirection: Boolean = false) {
        val pDirection = player.location.direction

        val d = location.clone().subtract(player.eyeLocation.toVector()).toVector()
        val l = player.location.setDirection(d)
        l.add(d.normalize().multiply(blocks))
        player.teleport(l)

        if (playerToOriginalDirection) {
            val newLoc = player.location.setDirection(pDirection)

            player.teleport(newLoc)
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val observer = event.player

        if (this.spectateCacheManager.isPlayerSpectating(observer.uniqueId)) {

            val pl = this.spectateCacheManager.get(observer.uniqueId)

            this.fakePlayerManager.spawnFakePlayerToAll(observer, pl?.originalLocation)

            return
        }

        val spectatePlayers = this.spectateCacheManager.values

        for (sp in spectatePlayers) {
            if (sp != null) {
                this.fakePlayerManager.spawnFakePlayer(sp.player, observer, sp.originalLocation)
            }
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        if (this.spectateCacheManager.isPlayerSpectating(player.uniqueId)) {
            this.fakePlayerManager.despawnFakePlayerToAll(player)
        }
    }

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        val player = event.player

        if (!this.spectateCacheManager.isPlayerSpectating(player.uniqueId)) {
            return
        }

        val playerLocation = player.location


        if (playerLocation.world != event.to?.world) {
            event.isCancelled = true
            player.spigot().sendMessage(*TextHelper.attentionText("You can't travel to another world while spectating!"))
            return
        }

        val origin = this.spectateCacheManager.get(player.uniqueId)!!.originalLocation

        val distance = origin.distance(playerLocation)
        val maxDistance = plugin.config.getDouble(Settings.SPECTATEMAXDISTANCE.path)
        if (distance > maxDistance) {
            teleportTowardsOrigin(player, origin, playerToOriginalDirection = true)
        }
    }

}