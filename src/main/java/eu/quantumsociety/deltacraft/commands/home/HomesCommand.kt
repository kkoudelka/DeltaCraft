package eu.quantumsociety.deltacraft.commands.home

import eu.quantumsociety.deltacraft.managers.HomesManager
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
        val p = commandSender
        val list = configManager.getPlayerHomes(p)
        val divider = "===================================="
        val text = ComponentBuilder(divider).color(ChatColor.DARK_GRAY).append("\n").bold(true)
        for (ph in list) {
            text
                    .append(" - ").color(ChatColor.DARK_GRAY)
                    .append("[").color(ChatColor.DARK_GRAY).bold(false)
                    .append("✗").color(ChatColor.DARK_RED)
                    .event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/delhome " + ph.homeName))
                    .event(HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("Delete '" + ph.homeName + "'").create()))
                    .append("]").color(ChatColor.DARK_GRAY)
                    .reset()
                    .append("   ")
                    .append("[").color(ChatColor.DARK_AQUA).bold(true)
                    .append(ph.homeName).color(ChatColor.GOLD).bold(true)
                    .event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + ph.homeName))
                    .event(HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("Teleport to '" + ph.homeName + "'").create()))
                    .append("]").color(ChatColor.DARK_AQUA).bold(true)
                    .append("\n").reset().bold(true)
        }
        text.append(divider).color(ChatColor.DARK_GRAY)
        p.spigot().sendMessage(*text.create())
        return true
    }

}