package eu.quantumsociety.DeltaCraft.commands.spectate;

import eu.quantumsociety.DeltaCraft.DataManager;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;

public class SpectateCommand implements CommandExecutor  {
    final DataManager dataMgr;

    public SpectateCommand(DataManager dataMgr) {
        this.dataMgr = dataMgr;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = dataMgr.getConfig();

        getLogger().info(config.getString("Test"));

        config.set("Test", "AA");

        dataMgr.saveConfig();

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this commands");
            return false;
        }

        Player p = (Player) sender;

        GameMode currentMode = p.getGameMode();

        return false;
    }
}
