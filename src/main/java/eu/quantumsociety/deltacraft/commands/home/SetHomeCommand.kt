package eu.quantumsociety.deltacraft.commands.home

import eu.quantumsociety.deltacraft.managers.HomesManager
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import org.bukkit.Particle
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetHomeCommand(private val configManager: HomesManager) : CommandExecutor {

    private val overrideString: String = "::override::"

    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (commandSender !is Player) {
            commandSender.sendMessage("Only players can use this command")
            return true
        }

        val player: Player = commandSender

        var overrideSave: Boolean = false

        if (strings.size > 1) {
            player.sendMessage("Correct usage of this command is /sethome <name>")
            return true
        }
        var homeName = if (strings.isEmpty()) "default" else strings[0].toLowerCase()

        if (homeName == overrideString) {
            player.sendMessage("Home name is invalid");
            return true
        }

        if (homeName.endsWith(overrideString)) {
            homeName = homeName.replace(overrideString, "", true)
            overrideSave = true
        }

        if (configManager.homeExists(player, homeName) && !overrideSave) {
            val text = ComponentBuilder("Home '$homeName' already exists.")
                    .color(ChatColor.DARK_AQUA)
                    .bold(true)
                    .append("\n")
                    .append("\n")
                    .append("[ ").color(ChatColor.DARK_AQUA).bold(false)
                    .append("OVERWRITE").color(ChatColor.GREEN)
                    .event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sethome $homeName$overrideString"))
                    .event(HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("Proceed to teleport to '$homeName' anyway").create()))
                    .append(" ]").reset().color(ChatColor.DARK_AQUA).bold(false)
                    .color(ChatColor.DARK_AQUA)


            player.spigot().sendMessage(*text.create())
            return true
        }

        //TODO: Check whether home with this name is already being used
        configManager.setHome(player, homeName)

        val output = "Home $homeName has been saved successfully!"
        player.sendMessage(output)
        player.location.world?.spawnParticle(Particle.HEART, player.location.add((player.location.direction.multiply(2))).add(0.0,1.0,0.0), 1)
        return true
    }

}