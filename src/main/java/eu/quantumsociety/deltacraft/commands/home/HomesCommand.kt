package eu.quantumsociety.deltacraft.commands.home

import eu.quantumsociety.deltacraft.managers.HomesManager
import eu.quantumsociety.deltacraft.utils.TextHelper
import eu.quantumsociety.deltacraft.utils.enums.Permissions
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HomesCommand(private val configManager: HomesManager) : CommandExecutor {
    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (commandSender !is Player) {
            commandSender.sendMessage("Only players can use this command")
            return true;
        }
        val player: Player = commandSender

        if (!player.hasPermission(Permissions.HOMELISTSELF.path)) {

            player.spigot().sendMessage(*TextHelper.insufficientPermissions(Permissions.HOMELISTSELF))
            return true
        }


        val list = configManager.getPlayerHomes(player)


        val text = ComponentBuilder()
                .append(TextHelper.getDivider())
                .append("\n").bold(true)

        if (list.isEmpty()) {
            text
                    .append("You have no homes")
                    .color(ChatColor.DARK_RED)
                    .bold(true)
                    .append("\n")
        } else {
            for (ph in list) {
                text
                        .append(" - ").color(ChatColor.DARK_GRAY)
                        .append(TextHelper.createActionButton(ComponentBuilder("✗")
                                .event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/delhome " + ph.homeName))
                                .event(HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("Delete '" + ph.homeName + "'").create()))
                                .create(), ChatColor.RED))
                        .append("   ")
                        .reset()
                        .append(TextHelper.createActionButton(
                                ComponentBuilder(ph.homeName)
                                        .event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + ph.homeName))
                                        .event(HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("Teleport to '${ph.homeName}' in ${ph.location.world?.name}").create()))
                                        .create(),
                                ChatColor.YELLOW
                        ))

                        .append("\n").reset().bold(true)
            }
        }
        text
                .reset()
                .append(TextHelper.getDivider())
        player.spigot().sendMessage(*text.create())
        return true
    }

}