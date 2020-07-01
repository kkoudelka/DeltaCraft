package eu.quantumsociety.DeltaCraft.commands.home;

import eu.quantumsociety.DeltaCraft.DataManager;
import eu.quantumsociety.DeltaCraft.utils.KeyHelper;
import eu.quantumsociety.DeltaCraft.utils.PluginSubmodule;
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

        String homeName = "default";

        if (strings != null) {
            if (strings.length > 1) {
                commandSender.sendMessage("Correct usage of this command is /sethome <name>");
                return false;
            }

            homeName = strings.length < 1
                    ? "default"
                    : strings[0].toLowerCase();
        }


        //TODO: Check whether home with this name is already being used

        Player p = (Player) commandSender;
        Location l = p.getLocation();

        KeyHelper kh = new KeyHelper(p.getUniqueId(), PluginSubmodule.HOME);

        String x = kh.get("x");
        String y = kh.get("y");
        String z = kh.get("z");
        String pitch = kh.get("pitch");
        String yaw = kh.get("yaw");
        String world = kh.get("world");


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
