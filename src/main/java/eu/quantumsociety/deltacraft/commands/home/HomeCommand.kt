package eu.quantumsociety.deltacraft.commands.home

import eu.quantumsociety.deltacraft.managers.HomesManager
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import org.bukkit.Effect
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class HomeCommand(private val configManager: HomesManager) : CommandExecutor, TabCompleter {

    private val overrideString: String = "::override::"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command")
            return false
        }
        val p = sender as Player

        var overrideTp = false

        if (args.size > 1) {
            sender.sendMessage("Correct usage of this command is /home <name>")
            return false
        }
        var homeName = if (args.isEmpty()) "default" else args[0].toLowerCase()

        if (homeName.endsWith(overrideString)) {
            homeName = homeName.replace(overrideString, "", true)
            overrideTp = true
        }

        val homeLocation = configManager.getHome(p, homeName)




        if (homeLocation == null) {
            p.sendMessage("Home does not exist")
            return true
        }

        if (overrideTp) {
            p.teleport(homeLocation)
            p.sendMessage("Welcome home!")
            return true
        }

        val isObstructed = configManager.isObstructed(homeLocation);

        if (isObstructed.first) {
            val text = ComponentBuilder(isObstructed.second)
                    .color(ChatColor.DARK_RED)
                    .bold(true)
                    .append("\n")
                    .append("\n")
                    .append("[ CLICK HERE TO TELEPORT ANYWAY ]")
                    .color(ChatColor.DARK_AQUA)
                    .bold(false)
                    .event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home $homeName$overrideString"))
                    .event(HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("Proceed to teleport to '$homeName' anyway").create()))

            p.spigot().sendMessage(*text.create())
            return true;
        }


        p.teleport(homeLocation)
        p.sendMessage("Welcome home!")
        p.location.world?.playEffect(p.location, Effect.SMOKE, 0)

        return true
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command, p2: String, p3: Array<out String>): MutableList<String> {
        val list = mutableListOf<String>()

        if (cmd.name.equals("home", true) && p3.size >= 0) {


            if (sender !is Player) {
                return list
            }

            val player: Player = sender

            val homes = configManager.getPlayerHomes(player)

            for (h in homes) {
                list.add(h.homeName)
            }
        }

        return list
    }

}