package eu.quantumsociety.deltacraft.commands.home

import eu.quantumsociety.deltacraft.managers.HomesManager
import eu.quantumsociety.deltacraft.utils.TextHelper
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import org.bukkit.Effect
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.awt.Component

class HomeCommand(private val configManager: HomesManager) : CommandExecutor, TabCompleter {

    private val overrideString: String = "::override::"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command")
            return false
        }
        val p: Player = sender

        var overrideTp = false

        if (args.size > 1) {
            sender.sendMessage("Correct usage of this command is /home <name>")
            return false
        }
        var homeName = if (args.isEmpty()) "default" else args[0].toLowerCase()

        if (homeName == overrideString) {
            sender.sendMessage("Home name is invalid");

            return true
        }

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
                    .append(ComponentBuilder("").create())
                    .append("\n")
                    .append(TextHelper.createActionButton(ComponentBuilder("TELEPORT ANYWAY")
                            .event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home $homeName$overrideString"))
                            .event(HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("Proceed to teleport to '$homeName' anyway")
                                    .create()))
                            .create()))


            p.spigot().sendMessage(*text.create())
            return true;
        }


        p.teleport(homeLocation)
        p.sendMessage("Welcome home!")


        // Effects on teleport
        val world = p.location.world!!

        world.spawnParticle(Particle.EXPLOSION_NORMAL, p.location.add(0.0, 0.1, 0.0), 10)
        world.playSound(p.location, Sound.UI_TOAST_IN, SoundCategory.MASTER, 10f, 1f)


        return true
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command, p2: String, p3: Array<out String>): MutableList<String> {
        val list = mutableListOf<String>()

        if (sender !is Player) {
            return list
        }

        val player: Player = sender

        if (cmd.name.equals("home", true) && p3.isNotEmpty() && p3.size < 2) {


            if (configManager.homeExists(player, "default")) {
                list.add("default")
            }


            val homes = configManager.getPlayerHomes(player)

            for (h in homes) {
                list.add(h.homeName)
            }
        }

        return list
    }

}