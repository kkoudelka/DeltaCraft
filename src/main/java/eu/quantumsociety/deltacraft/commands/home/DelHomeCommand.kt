package eu.quantumsociety.deltacraft.commands.home

import eu.quantumsociety.deltacraft.managers.HomesManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DelHomeCommand(private val homeConfigManager: HomesManager) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, p2: String, params: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command")
            return true;
        }

        val p: Player = sender


        val homeName = if (params.isEmpty()) "default" else params[0].toLowerCase()

        val success = homeConfigManager.delHome(p, homeName)

        p.sendMessage(if (success) "Home deleted" else "No such home");

        return true
    }
}