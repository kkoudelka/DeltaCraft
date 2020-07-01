package eu.quantumsociety.DeltaCraft.commands.home;

import eu.quantumsociety.DeltaCraft.DataManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class SetHomeCommand implements CommandExecutor {

    private DataManager homeManager;

    public SetHomeCommand(DataManager homeManager) {
        this.homeManager = homeManager;
    }


    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command");
            return false;
        }

        if (strings == null || strings.length > 0) {
            commandSender.sendMessage("Correct usage of this command is /sethome <name>");
            return false;
        }

        String homeName = strings.length < 1
                ? "default"
                : strings[0];

        Player p = (Player) commandSender;
        Location l = p.getLocation();

        String pKeyHome = "player." + p.getUniqueId().toString() + "." + homeName;

        String x = pKeyHome + ".x";
        String y = pKeyHome + ".y";
        String z = pKeyHome + ".z";
        String pitch = pKeyHome + ".pitch";
        String yaw = pKeyHome + ".yaw";
        String world = pKeyHome + ".world";


        homeManager.getConfig().set(x, l.getX());
        homeManager.getConfig().set(y, l.getY());
        homeManager.getConfig().set(z, l.getZ());
        homeManager.getConfig().set(pitch, l.getPitch());
        homeManager.getConfig().set(yaw, l.getYaw());
        homeManager.getConfig().set(world, l.getWorld().getName());

        homeManager.saveConfig();

        String output = "Home " + homeName + "has been saved successfully!";
        commandSender.sendMessage(output);

        return true;
    }
}
