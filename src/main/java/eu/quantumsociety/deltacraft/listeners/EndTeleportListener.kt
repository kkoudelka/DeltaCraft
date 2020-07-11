package eu.quantumsociety.deltacraft.listeners

import eu.quantumsociety.deltacraft.DeltaCraft
import eu.quantumsociety.deltacraft.utils.TextHelper
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.metadata.FixedMetadataValue

class EndTeleportListener(private val plugin: DeltaCraft) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        val player = event.player

        if (DeltaCraft.isIdiot(player)) {
            event.isCancelled = true;
            return;
        }

        //TODO: To config
        val canTravelToEnd = false

        if (event.to?.world?.environment == World.Environment.THE_END) {
            if (!canTravelToEnd) {
                event.isCancelled = true

                player.teleport(player.location.subtract(0.0, 250.0, 0.0))

                player.setMetadata("isIdiot", FixedMetadataValue(plugin, true))
                player.spigot().sendMessage(*TextHelper.attentionText("You can't travel to the End just yet"))
                player.spigot().sendMessage(*TextHelper.attentionText("Follow the rules pls"))
                player.server.spigot().broadcast(*ComponentBuilder("")
                        .append(TextHelper.varText(player.displayName))
                        .append(TextHelper.infoText(" is an idiot and tried to travel to the END, despite agreeing to the rules"))
                        .create())
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity;

        if (!DeltaCraft.isIdiot(player)) {
            return;
        }

        if (player.lastDamageCause?.cause == EntityDamageEvent.DamageCause.VOID) {
            player.removeMetadata("isIdiot", plugin);
        }
        event.deathMessage = "${player.displayName} died while reading the rules"
    }
}