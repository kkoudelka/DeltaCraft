package eu.quantumsociety.deltacraft.listeners

import eu.quantumsociety.deltacraft.utils.TextHelper
import eu.quantumsociety.deltacraft.utils.enums.Permissions
import eu.quantumsociety.deltacraft.utils.enums.Settings
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent

class EndTeleportListener: Listener {

    @EventHandler(ignoreCancelled = true)
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        val player = event.player

        //TODO: To config
        val canTravelToEnd = false

        if (event.to?.world?.environment == World.Environment.THE_END)
        {
            if (!canTravelToEnd) {
                event.isCancelled = true
                player.teleport(player.location.subtract(0.0,250.0,0.0))
                player.spigot().sendMessage(*TextHelper.attentionText("You can't travel to the End just yet"))
                player.spigot().sendMessage(*TextHelper.attentionText("Follow the rules pls"))
                player.server.spigot().broadcast(*ComponentBuilder("")
                        .append(TextHelper.varText(player.displayName))
                        .append(TextHelper.infoText(" is an idiot and tried to travel to the END, despite agreeing to the rules"))
                        .create())
            }
        }

    }
}