package eu.quantumsociety.deltacraft.commands.home

import eu.quantumsociety.deltacraft.managers.HomesManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetHomeCommand(private val configManager: HomesManager) : CommandExecutor {
    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (commandSender !is Player) {
            commandSender.sendMessage("Only players can use this command")
            return true
        }
        var homeName = "default"

        val player: Player = commandSender

        if (strings.size > 1) {
            player.sendMessage("Correct usage of this command is /sethome <name>")
            return true
        }
        homeName = if (strings.size < 1) "default" else strings[0].toLowerCase()


        //TODO: Check whether home with this name is already being used
        configManager.setHome(player, homeName)

        /* String x = kh.get(homeName, "x");
        String y = kh.get(homeName, "y");
        String z = kh.get(homeName, "z");
        String pitch = kh.get(homeName, "pitch");
        String yaw = kh.get(homeName, "yaw");
        String world = kh.get(homeName, "world");


        configManager.getConfig().set(x, l.getX());
        configManager.getConfig().set(y, l.getY());
        configManager.getConfig().set(z, l.getZ());
        configManager.getConfig().set(pitch, l.getPitch());
        configManager.getConfig().set(yaw, l.getYaw());
        configManager.getConfig().set(world, l.getWorld().getName());

        configManager.saveConfig();*/
        val output = "Home $homeName has been saved successfully!"
        player.sendMessage(output)
        return true
    }

}