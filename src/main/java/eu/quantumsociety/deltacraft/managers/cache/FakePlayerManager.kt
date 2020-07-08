package eu.quantumsociety.deltacraft.managers.cache

import com.mojang.authlib.GameProfile
import eu.quantumsociety.deltacraft.DeltaCraft
import eu.quantumsociety.deltacraft.managers.templates.CacheManager
import net.minecraft.server.v1_16_R1.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_16_R1.CraftServer
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.*


class FakePlayerManager(val plugin: DeltaCraft) : CacheManager<UUID, EntityPlayer>(plugin, false) {

    fun spawnFakePlayer(spectatePlayer: Player, playerObserver: Player, entityLocation: Location?): EntityPlayer {
        val uuid = UUID.randomUUID()

        val server = (Bukkit.getServer() as CraftServer).server
        val world = (Bukkit.getWorlds()[0] as CraftWorld).handle
        val npc = EntityPlayer(server, world, GameProfile(uuid, spectatePlayer.displayName), PlayerInteractManager(world))
        val location = entityLocation ?: spectatePlayer.location
        npc.setLocation(location.x, location.y, location.z, location.yaw, location.pitch)

        val connection = (playerObserver as CraftPlayer).handle.playerConnection

        connection.sendPacket(PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc))

        connection.sendPacket(PacketPlayOutNamedEntitySpawn(npc as EntityHuman))
        connection.sendPacket(PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc))
        this.addItem(npc.uniqueID, npc)
        return npc
    }

    fun spawnFakePlayerToAll(afkPlayer: Player, entityLocation: Location?) {

        val allPlayers = Bukkit.getOnlinePlayers()

        for (player in allPlayers) {
            spawnFakePlayer(afkPlayer, player, entityLocation)
        }

    }

    fun despawnFakePlayer(player: Player, entityID: Int) {
        val connection = (player as CraftPlayer).handle.playerConnection
        connection.sendPacket(PacketPlayOutEntityDestroy(entityID));
    }

    fun despawnFakePlayerToAll(afkPlayer: Player) {
        val allPlayers = Bukkit.getOnlinePlayers()

        val entities = this.cache.values.filter { value -> value.name == afkPlayer.name }

        for (entity in entities) {
            for (player in allPlayers) {
                despawnFakePlayer(player, entity.id)
            }
            this.removeItem(entity.uniqueID)
        }


    }


}