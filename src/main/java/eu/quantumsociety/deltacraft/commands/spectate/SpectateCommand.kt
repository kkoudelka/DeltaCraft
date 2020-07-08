package eu.quantumsociety.deltacraft.commands.spectate

import eu.quantumsociety.deltacraft.DeltaCraft
import eu.quantumsociety.deltacraft.managers.SpectateManager
import eu.quantumsociety.deltacraft.managers.cache.SpectateCacheManager
import eu.quantumsociety.deltacraft.managers.cache.FakePlayerManager
import eu.quantumsociety.deltacraft.utils.KeyHelper
import eu.quantumsociety.deltacraft.utils.TextHelper
import eu.quantumsociety.deltacraft.utils.enums.Permissions
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

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
            p.spigot().sendMessage(*TextHelper.insufficientPermissions(Permissions.SPECTATEUSE));
            return true
        }
        return executeSwitch(p)
    }

    private fun executeSwitch(p: Player): Boolean {
        val keys = KeyHelper(p.uniqueId)
        val exists = configManager.exists(keys)
        return if (!exists) {
            switchToSpectate(p, keys)
        } else switchBack(p, keys)
    }

    private fun switchBack(p: Player, keys: KeyHelper): Boolean {
        val l = configManager.getLocation(keys)
        val g = configManager.getGamemode(keys)
        p.setPlayerListName(p.name)

        fakePlayerHelper.despawnFakePlayerToAll(p)

        return switchBack(p, l, g)
    }

    private fun switchBack(p: Player, l: Location, gm: GameMode): Boolean {
        p.teleport(l)
        p.gameMode = gm
        val id = p.uniqueId
        spectateCache.removeItem(id)
        configManager.delete(id)
        p.sendMessage(ChatColor.YELLOW.toString() + "You are no longer Spectating!")
        return true
    }

    private fun switchToSpectate(p: Player, keys: KeyHelper): Boolean {
        fakePlayerHelper.spawnFakePlayerToAll(p, p.location)

        val loc = p.location
        val gm = p.gameMode
        configManager.save(keys, loc, gm)
        spectateCache.addItem(p, loc, gm)
        p.gameMode = GameMode.SPECTATOR
        p.spigot().sendMessage(*TextHelper.infoText("You are now Spectating!"))
        p.setPlayerListName("${p.name} (Spectating) ")
        return true
    }

}