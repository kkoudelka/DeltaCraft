package eu.quantumsociety.deltacraft.classes

import org.bukkit.Location
import java.util.*

data class PlayerHome(var playerId: UUID, var homeName: String, var location: Location)