package eu.quantumsociety.deltacraft.commands.home;

import eu.quantumsociety.deltacraft.managers.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {
    private ConfigManager configManager;

    public HomeCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return false;
        }

        String homeName = "default";

        if (args != null) {
            if (args.length > 1) {
                sender.sendMessage("Correct usage of this command is /home <name>");
                return false;
            }

            homeName = args.length < 1
                    ? "default"
                    : args[0].toLowerCase();
        }

        return true;
    }
}
