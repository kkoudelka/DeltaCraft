package eu.quantumsociety.DeltaCraft.commands.home;

import eu.quantumsociety.DeltaCraft.DataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {
    private DataManager homeManager;

    public HomeCommand(DataManager homeManager) {
        this.homeManager = homeManager;
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
