package eu.quantumsociety.DeltaCraft.commands.spectate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;

public class SpectateCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        getLogger().info(String.join(" ", args));
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this commands");
            return false;
        }

        return false;
    }
}
