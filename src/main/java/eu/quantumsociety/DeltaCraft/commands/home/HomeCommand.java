package eu.quantumsociety.DeltaCraft.commands.home;

import eu.quantumsociety.DeltaCraft.DataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HomeCommand implements CommandExecutor {
    private DataManager homeManager;

    public HomeCommand(DataManager homeManager) {
        this.homeManager = homeManager;
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
