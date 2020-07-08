package eu.quantumsociety.deltacraft.managers

import eu.quantumsociety.deltacraft.DeltaCraft
import eu.quantumsociety.deltacraft.classes.PlayerHome
import eu.quantumsociety.deltacraft.managers.templates.ConfigManager
import eu.quantumsociety.deltacraft.utils.KeyHelper
import eu.quantumsociety.deltacraft.utils.TextHelper
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor

class HomesManager(plugin: DeltaCraft?) : ConfigManager(plugin, "home.yml") {
    fun getPlayerHomes(p: Player): List<PlayerHome> {
        val kh = KeyHelper(p.uniqueId)
        if (!config.contains(kh.playerKey))
            return emptyList()
        val section = config.getConfigurationSection(kh.playerKey)!!
        val list: MutableList<PlayerHome> = ArrayList()
        for (key in section.getKeys(false)) {
            val loc = section.getLocation("$key.location")
            val home = PlayerHome(p.uniqueId, key, loc!!)
            list.add(home)
        }
        return list
    }

    fun getPlayerHomesCount(p: Player): Int {
        val kh = KeyHelper(p.uniqueId)
        if (!config.contains(kh.playerKey))
            return 0
        val section = config.getConfigurationSection(kh.playerKey)!!

        return section.getKeys(false).size
    }

    fun getHome(p: Player, homeName: String): Location? {
        val kh = KeyHelper(p.uniqueId)

        if (!config.contains(kh[homeName]))
            return null
        val section = config.getConfigurationSection(kh.playerKey)!!
        return section.getLocation("$homeName.location")
    }

    fun homeExists(p: Player, homeName: String): Boolean {
        val kh = KeyHelper(p.uniqueId)

        return config.contains(kh[homeName])
    }

    fun isObstructed(location: Location): Pair<Boolean, Array<BaseComponent>?> {
        val blockUnder = location.block.getRelative(BlockFace.DOWN)
        if (blockUnder.isEmpty) {
            return Pair(true, TextHelper.attentionText("A block is missing under the home location!"))
        }

        if (blockUnder.type == Material.LAVA || blockUnder.type == Material.LAVA_BUCKET) {
            return Pair(true, TextHelper.attentionText("There is a lava under the home position"))
        }

        val block = location.block
        val up = block.getRelative(BlockFace.UP)

        if (up.isEmpty && block.isEmpty) {
            return Pair(false, null)
        }

        return Pair(true, ComponentBuilder()
                .append(TextHelper.attentionText("Home location is obstructed"))
                .event(HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("Obstructed by: '${block.type} and ${up.type}'").create()))
                .create())
    }

    fun setHome(p: Player, homeName: String): Boolean {
        return this.setHome(p.uniqueId, homeName, p.location)
    }

    fun setHome(playerId: UUID, homeName: String, location: Location): Boolean {
        location.x = floor(location.x)
        location.z = floor(location.z)
        val centred = location.add(0.5, 0.0, 0.5)
        val pl = PlayerHome(playerId, homeName, centred)
        val kh = KeyHelper(playerId)
        config[kh[homeName, "location"]] = pl.location
        saveConfig()
        return true
    }

    fun delHome(p: Player, homeName: String): Pair<Boolean, Location?> {
        val kh = KeyHelper(p.uniqueId)

        if (!homeExists(p, homeName))
            return Pair(false, null)
        val location = getHome(p, homeName)?.clone()
        config[kh[homeName]] = null
        saveConfig()
        return Pair(true, location)

    }
}