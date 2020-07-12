package eu.quantumsociety.deltacraft.commands.afk

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AfkCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, string: String, strings: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command")
            return true;
        }

//        val player: Player = sender

        return true
    }
}