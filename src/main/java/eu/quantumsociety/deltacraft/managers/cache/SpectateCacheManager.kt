package eu.quantumsociety.deltacraft.managers.cache

import eu.quantumsociety.deltacraft.DeltaCraft
import eu.quantumsociety.deltacraft.classes.CachePlayer
import eu.quantumsociety.deltacraft.managers.templates.CacheManager
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.util.Vector
import java.util.UUID

class SpectateCacheManager(plugin: DeltaCraft) : CacheManager<UUID, CachePlayer>(plugin, true) {
    val correctionKey = "teleportCorrection"
    val teleportBackKey = "teleportBack"

    fun addItem(id: UUID, origin: Location, gm: GameMode, velocity: Vector, fallDistance: Float) {
        this.addItem(
                id,
                CachePlayer(
                        id,
                        origin,
                        gm,
                        velocity,
                        fallDistance
                ))
    }

    fun isPlayerSpectating(uuid: UUID): Boolean {
        return this.contains(uuid)
    }
}