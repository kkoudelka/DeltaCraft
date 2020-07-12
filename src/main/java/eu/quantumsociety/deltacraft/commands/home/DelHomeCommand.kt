package eu.quantumsociety.deltacraft.commands.home

import eu.quantumsociety.deltacraft.managers.HomesManager
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.Effect
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class DelHomeCommand(private val homeConfigManager: HomesManager) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, p2: String, params: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command")
            return true;
        }

        val player: Player = sender


        val homeName = if (params.isEmpty()) "default" else params[0].toLowerCase()



        val success = homeConfigManager.delHome(player, homeName)

        if (!success.first) {
            val output = ComponentBuilder()
                    .append("Home ").color(ChatColor.YELLOW)
                    .append(homeName).color(ChatColor.WHITE)
                    .append(" not found").color(ChatColor.YELLOW)
            player.spigot().sendMessage(*output.create())

            return true
        }

        val output = ComponentBuilder()
                .append("Home ").color(ChatColor.YELLOW)
                .append(homeName).color(ChatColor.WHITE)
                .append(" has been deleted!").color(ChatColor.YELLOW)
        player.spigot().sendMessage(*output.create())



        val location = success.second
        val world = location?.world!!

        world.spawnParticle(Particle.VILLAGER_ANGRY, location.add(0.0, 0.5, 0.0), 1)
        world.playSound(location, Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 2f, 1f)

        return true
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command, p2: String, p3: Array<out String>): MutableList<String> {
        val list = mutableListOf<String>()

        if (sender !is Player) {
            return list
        }
        val player: Player = sender

        // first argument autocomplete
        if (cmd.name.equals("delhome", true) && p3.isNotEmpty() && p3.size < 2) {

            val typedIn = p3[0].toLowerCase()

            val homes = homeConfigManager.getPlayerHomes(player)

            for (h in homes) {
                if (h.homeName.startsWith(typedIn)) {
                    list.add(h.homeName)
                }
            }
        }

        return list
    }
}