package eu.quantumsociety.deltacraft.utils

import com.mojang.authlib.GameProfile
import eu.quantumsociety.deltacraft.DeltaCraft
import net.minecraft.server.v1_16_R1.*
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_16_R1.CraftServer
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.*


class FakePlayerHelper {
    companion object {
        @JvmStatic
        fun spawnFakePlayer(plugin: DeltaCraft, player: Player): EntityPlayer {
            val uuid = UUID.randomUUID()
            val server = (Bukkit.getServer() as CraftServer).server
            val world = (Bukkit.getWorlds()[0] as CraftWorld).handle
            val npc = EntityPlayer(server, world, GameProfile(uuid, player.displayName), PlayerInteractManager(world))
            val location = player.location
            npc.setLocation(location.x, location.y, location.z, location.yaw, location.pitch)

            val players = Bukkit.getOnlinePlayers()

            val connection = (player as CraftPlayer).handle.playerConnection

            connection.sendPacket(PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc))

            connection.sendPacket(PacketPlayOutNamedEntitySpawn(npc as EntityHuman))
            connection.sendPacket(PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,  npc))

            connection.sendPacket(PacketPlayOutNamedEntitySpawn(npc as EntityHuman))

            return npc
        }

        fun despawnFakePlayer(npc: EntityPlayer) {
            //connection.sendPacket(PacketPlayOutEntityDestroy(npc.id));
        }
    }
}