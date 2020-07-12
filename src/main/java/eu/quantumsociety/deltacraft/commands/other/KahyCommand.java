package eu.quantumsociety.deltacraft.commands.other;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.utils.Extensions;
import eu.quantumsociety.deltacraft.utils.TextHelper;
import eu.quantumsociety.deltacraft.utils.enums.Permissions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KahyCommand implements CommandExecutor {
    private final DeltaCraft plugin;

    public KahyCommand(DeltaCraft plugin) {
        this.plugin = plugin;
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this commands");
            return false;
        }
        Player p = (Player) sender;
        if (!p.hasPermission(Permissions.KAHYUSE.getPath())) {
            p.spigot().sendMessage(TextHelper.insufficientPermissions(Permissions.KAHYUSE));
            return true;
        }

        if (p.getGameMode() != GameMode.SURVIVAL) {
            p.spigot().sendMessage(TextHelper.attentionText("This command can be used only in survival mode"));
            return true;
        }

        if (Extensions.isKahy(p)) {
            p.spigot().sendMessage(TextHelper.attentionText("Kahy, you cannot use protection against yourself"));
            return true;
        }

        if (Extensions.hasProtection(p)) {
            p.removeMetadata(Extensions.kahyProtectionKey, plugin);
            p.spigot().sendMessage(TextHelper.infoText("Kahy protection disabled"));
            return true;
        }

        p.setMetadata(Extensions.kahyProtectionKey, Extensions.getFakeMetadata(plugin));
        p.spigot().sendMessage(TextHelper.infoText("Kahy protection enabled", ChatColor.GREEN));
        return true;
    }
}
