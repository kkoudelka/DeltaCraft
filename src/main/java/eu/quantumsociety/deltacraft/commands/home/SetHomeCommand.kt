package eu.quantumsociety.deltacraft.commands.home

import eu.quantumsociety.deltacraft.DeltaCraft
import eu.quantumsociety.deltacraft.managers.HomesManager
import eu.quantumsociety.deltacraft.utils.TextHelper
import eu.quantumsociety.deltacraft.utils.enums.Permissions
import eu.quantumsociety.deltacraft.utils.enums.Settings
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import org.bukkit.Particle
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetHomeCommand(private val configManager: HomesManager, val deltaCraft: DeltaCraft) : CommandExecutor {

    private val overrideString: String = "::override::"

    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (commandSender !is Player) {
            commandSender.sendMessage("Only players can use this command")
            return true
        }

        val player: Player = commandSender

        if (!player.hasPermission(Permissions.HOMESET.path)) {

            player.spigot().sendMessage(*TextHelper.insufficientPermissions(Permissions.HOMESET))
            return true
        }

        if (this.deltaCraft.manager.isPlayerSpectating(player.uniqueId)) {
            player.spigot().sendMessage(*TextHelper.infoText("You cannot set home while spectating"))
            return true
        }

        val maxHomes = deltaCraft.config.getInt(Settings.HOMEMAXHOMES.path)

        if (configManager.getPlayerHomesCount(player) > (maxHomes - 1)) {

            player.spigot().sendMessage(*TextHelper.infoText("You have reached quota of $maxHomes homes"))

            return true
        }


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
            val text = ComponentBuilder()
                    .append(TextHelper.getDivider())
                    .append(TextHelper.infoText("Home "))
                    .append(TextHelper.varText(homeName))
                    .append(TextHelper.infoText(" already exists."))
                    .append("\n")
                    .append("\n")
                    .append("\n")
                    .append(TextHelper.createActionButton(
                            ComponentBuilder("OVERWRITE")
                                    .event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sethome $homeName$overrideString"))
                                    .event(HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder()
                                            .append(TextHelper.infoText("Proceed to create home "))
                                            .append(TextHelper.varText(homeName))
                                            .append(TextHelper.infoText(".")).create()))
                                    .create(), ChatColor.DARK_GREEN
                    ))
                    .append("")
                    .reset()
                    .append(TextHelper.getDivider())


            player.spigot().sendMessage(*text.create())
            return true
        }

        //TODO: Check whether home with this name is already being used
        configManager.setHome(player, homeName)

        val output = ComponentBuilder()
                .append(TextHelper.infoText("Home "))
                .append(TextHelper.varText(homeName))
                .append(TextHelper.infoText(" has been saved successfully!"))
        player.spigot().sendMessage(*output.create())
        player.location.world?.spawnParticle(Particle.HEART, player.location.add((player.location.direction.multiply(2))).add(0.0, 1.0, 0.0), 1)
        return true
    }

}