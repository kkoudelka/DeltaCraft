package eu.quantumsociety.deltacraft.managers

import eu.quantumsociety.deltacraft.DeltaCraft
import eu.quantumsociety.deltacraft.classes.PlayerHome
import eu.quantumsociety.deltacraft.utils.KeyHelper
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList

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

    fun getHome(p: Player, homeName: String): Location? {
        val kh = KeyHelper(p.uniqueId)

        if (!config.contains(kh[homeName]))
            return null
        val section = config.getConfigurationSection(kh.playerKey)!!
        return section.getLocation("$homeName.location")
    }

    fun setHome(p: Player, homeName: String): Boolean {
        val l = p.location
        val pl = PlayerHome(p.uniqueId, homeName, l)
        val kh = KeyHelper(p.uniqueId)
        config[kh[homeName, "location"]] = pl.location
        saveConfig()
        return true
    }

    fun delHome(p: Player, homeName: String): Boolean {
        val kh = KeyHelper(p.uniqueId)

        if (!config.contains(kh[homeName]))
            return false
        config[kh[homeName]] = null
        saveConfig()
        return true

    }
}