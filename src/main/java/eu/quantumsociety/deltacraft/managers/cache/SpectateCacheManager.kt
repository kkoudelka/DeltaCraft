package eu.quantumsociety.deltacraft.managers.cache

import eu.quantumsociety.deltacraft.DeltaCraft
import eu.quantumsociety.deltacraft.classes.SpectatePlayer
import eu.quantumsociety.deltacraft.managers.templates.CacheManager
import eu.quantumsociety.deltacraft.utils.enums.Settings
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.util.Vector
import java.util.UUID

class SpectateCacheManager : CacheManager<UUID, SpectatePlayer> {
    val correctionKey = "teleportCorrection"
    val teleportBackKey = "teleportBack"
    val maxDistance: Double;

    constructor(plugin: DeltaCraft) : super(plugin, true) {
        maxDistance = plugin.config.getDouble(Settings.SPECTATEMAXDISTANCE.path)
    }

    fun addItem(id: UUID, origin: Location, gm: GameMode, velocity: Vector, fallDistance: Float) {
        this.addItem(
                id,
                SpectatePlayer(
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