package eu.quantumsociety.deltacraft.commands.home

import eu.quantumsociety.deltacraft.managers.HomesManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HomeCommand(private val configManager: HomesManager) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command")
            return false
        }
        val p = sender as Player

        var homeName = "default"
        if (args != null) {
            if (args.size > 1) {
                sender.sendMessage("Correct usage of this command is /home <name>")
                return false
            }
            homeName = if (args.size < 1) "default" else args[0].toLowerCase()
        }

        val homeLocation = configManager.getHome(p, homeName)

        if (homeLocation == null) {
            p.sendMessage("Home does not exist")
            return true
        }

        p.teleport(homeLocation)
        p.sendMessage("Welcome home!")

        return true
    }

}