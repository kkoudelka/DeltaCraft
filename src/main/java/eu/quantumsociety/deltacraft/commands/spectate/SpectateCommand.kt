package eu.quantumsociety.deltacraft.commands.spectate

import eu.quantumsociety.deltacraft.DeltaCraft
import eu.quantumsociety.deltacraft.classes.SpectatePlayer
import eu.quantumsociety.deltacraft.managers.SpectateManager
import eu.quantumsociety.deltacraft.managers.cache.SpectateCacheManager
import eu.quantumsociety.deltacraft.managers.cache.FakePlayerManager
import eu.quantumsociety.deltacraft.utils.Extensions
import eu.quantumsociety.deltacraft.utils.TextHelper
import eu.quantumsociety.deltacraft.utils.enums.Permissions
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class SpectateCommand(private val configManager: SpectateManager, private val plugin: DeltaCraft, private val fakePlayerHelper: FakePlayerManager = FakePlayerManager(plugin)) : CommandExecutor {
    private val spectateCache: SpectateCacheManager
        get() = this.configManager.manager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this commands")
            return false
        }
        val p: Player = sender
        if (!p.hasPermission(Permissions.SPECTATEUSE.path)) {
            p.spigot().sendMessage(*TextHelper.insufficientPermissions(Permissions.SPECTATEUSE))
            return true
        }
        return executeSwitch(p)
    }

    private fun executeSwitch(p: Player): Boolean {
        val exists = configManager.exists(p.uniqueId)
        return if (!exists) {
            switchToSpectate(p)
        } else switchBack(p)
    }

    private fun switchBack(p: Player): Boolean {
        val spec = this.configManager.getPlayer(p.uniqueId)
        p.setPlayerListName(p.name)

        fakePlayerHelper.despawnFakePlayerToAll(p)
        return switchBack(p, spec)
    }

    private fun switchBack(p: Player, spec: SpectatePlayer): Boolean {
        return this.switchBack(p, spec.originalLocation, spec.prevGameMode, spec.originalVelocity, spec.fallDistance)
    }

    private fun switchBack(p: Player, l: Location, gm: GameMode, velocity: Vector, fallDistance: Float): Boolean {
        p.setMetadata(this.spectateCache.teleportBackKey, Extensions.getFakeMetadata(plugin))

        configManager.delete(p.uniqueId)

        p.teleport(l)
        p.gameMode = gm
        p.velocity = velocity
        p.fallDistance = fallDistance
        p.sendMessage(ChatColor.YELLOW.toString() + "You are no longer Spectating!")
        return true
    }

    private fun switchToSpectate(p: Player): Boolean {
        fakePlayerHelper.spawnFakePlayerToAll(p, p.location)

        configManager.addPlayer(SpectatePlayer(p.uniqueId, p.location, p.gameMode, p.velocity, p.fallDistance))

        p.gameMode = GameMode.SPECTATOR

        p.spigot().sendMessage(*TextHelper.infoText("You are now Spectating!"))
        p.setPlayerListName("${p.name} (Spectating) ")
        return true
    }

}