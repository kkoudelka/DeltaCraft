package eu.quantumsociety.deltacraft.commands.itemframe;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.managers.cache.ItemFrameCacheManager;
import eu.quantumsociety.deltacraft.utils.TextHelper;
import eu.quantumsociety.deltacraft.utils.enums.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class InvCommand implements CommandExecutor {

    private final ItemFrameCacheManager manager;

    private final DeltaCraft plugin;

    public InvCommand(DeltaCraft plugin) {
        this.plugin = plugin;
        this.manager = plugin.getManager().getItemFrameCacheManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this commands");
            return false;
        }
        Player p = (Player) sender;
        if (!p.hasPermission(Permissions.ITEMFRAMEUSE.getPath())) {
            p.spigot().sendMessage(TextHelper.insufficientPermissions(Permissions.ITEMFRAMEUSE));
            return true;
        }
        UUID id = p.getUniqueId();
        if (manager.contains(id)) {
            manager.removeItem(id);
            p.spigot().sendMessage(TextHelper.infoText("You are no longer placing invisible item frames"));
            return true;
        }

        manager.addItem(id, true);
        p.spigot().sendMessage(TextHelper.infoText("You are placing invisible item frames"));
        return true;
    }
}
