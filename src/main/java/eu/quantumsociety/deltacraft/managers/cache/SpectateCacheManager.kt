package eu.quantumsociety.deltacraft.managers.cache

import eu.quantumsociety.deltacraft.DeltaCraft
import eu.quantumsociety.deltacraft.classes.CachePlayer
import eu.quantumsociety.deltacraft.managers.templates.CacheManager
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

class SpectateCacheManager(plugin: DeltaCraft?) : CacheManager<UUID?, CachePlayer?>(plugin, true) {
    fun addItem(player: Player, origin: Location?, gm: GameMode?) {
        this.addItem(
                player.uniqueId,
                CachePlayer(
                        player,
                        origin,
                        gm
                ))
    }

    fun removeItem (player: Player) {
        this.removeItem(player.uniqueId)
    }

    fun isPlayerSpectating(uuid: UUID?): Boolean {
        return this.contains(uuid)
    }
}