package eu.quantumsociety.deltacraft.commands.home

import eu.quantumsociety.deltacraft.DeltaCraft
import eu.quantumsociety.deltacraft.managers.HomesManager
import eu.quantumsociety.deltacraft.utils.Extensions
import eu.quantumsociety.deltacraft.utils.TextHelper
import eu.quantumsociety.deltacraft.utils.enums.Permissions
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class HomeCommand(private val configManager: HomesManager) : CommandExecutor, TabCompleter {

    private val overrideString: String = "::override::"

    private val plugin: DeltaCraft
        get() = this.configManager.plugin!!


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command")
            return false
        }
        val player: Player = sender

        if (!player.hasPermission(Permissions.HOMEUSE.path)) {

            player.spigot().sendMessage(*TextHelper.insufficientPermissions(Permissions.HOMEUSE))
            return true
        }

        if (this.plugin.manager.spectateCacheManager.isPlayerSpectating(player.uniqueId)) {
            player.spigot().sendMessage(*TextHelper.attentionText("You can't use this command while spectating!"))
            return true
        }

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

        val homeLocation = configManager.getHome(player, homeName)




        if (homeLocation == null) {
            val output = ComponentBuilder()
                    .append("Home ").color(ChatColor.YELLOW)
                    .append(homeName).color(ChatColor.WHITE)
                    .append(" not found").color(ChatColor.YELLOW)
            player.spigot().sendMessage(*output.create())
            return true
        }


        val isObstructed = configManager.isObstructed(homeLocation);

        if (isObstructed.first && !overrideTp) {
            val text = ComponentBuilder()
                    .append(isObstructed.second)
                    .color(ChatColor.DARK_RED)
                    .bold(true)
                    .append("\n")
                    .append(ComponentBuilder("").create())
                    .append("\n")
                    .append(TextHelper.createActionButton(ComponentBuilder("TELEPORT ANYWAY")
                            .event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home $homeName$overrideString"))
                            .event(HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder()
                                    .append(TextHelper.infoText("Proceed to teleport to "))
                                    .append(TextHelper.varText(homeName))
                                    .append(TextHelper.infoText(" anyway.")).create()))
                            .create()))

            player.spigot().sendMessage(*text.create())
            return true;
        }

        if (Extensions.isIdiot(player)) {
            player.spigot().sendMessage(*TextHelper.attentionText("You cannot use home, because you're an idiot!"))
            return true;
        }

        player.teleport(homeLocation)
        player.sendMessage("Welcome home!")

        // Effects on teleport
        val world = player.location.world!!

        world.spawnParticle(Particle.EXPLOSION_NORMAL, player.location.add(0.0, 0.1, 0.0), 10)
        world.playSound(player.location, Sound.UI_TOAST_IN, SoundCategory.MASTER, 10f, 1f)


        return true
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command, p2: String, p3: Array<out String>): MutableList<String> {
        val list = mutableListOf<String>()

        if (sender !is Player) {
            return list
        }

        val player: Player = sender

        if (cmd.name.equals("home", true) && p3.isNotEmpty() && p3.size < 2) {

            val typedIn = p3[0].toLowerCase()

            val homes = configManager.getPlayerHomes(player)

            for (h in homes) {
                if (h.homeName.startsWith(typedIn)) {
                    list.add(h.homeName)
                }
            }
        }



        return list
    }
}