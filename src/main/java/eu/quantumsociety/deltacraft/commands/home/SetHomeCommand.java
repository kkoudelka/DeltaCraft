package eu.quantumsociety.deltacraft.commands.home;

import eu.quantumsociety.deltacraft.managers.HomesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {

    private HomesManager configManager;

    public SetHomeCommand(HomesManager homeManager) {
        this.configManager = homeManager;
    }


    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command");
            return true;
        }

        String homeName = "default";

        if (strings != null) {
            if (strings.length > 1) {
                commandSender.sendMessage("Correct usage of this command is /sethome <name>");
                return true;
            }

            homeName = strings.length < 1
                    ? "default"
                    : strings[0].toLowerCase();
        }


        //TODO: Check whether home with this name is already being used

        Player p = (Player) commandSender;



        configManager.setHome(p, homeName);

       /* String x = kh.get(homeName, "x");
        String y = kh.get(homeName, "y");
        String z = kh.get(homeName, "z");
        String pitch = kh.get(homeName, "pitch");
        String yaw = kh.get(homeName, "yaw");
        String world = kh.get(homeName, "world");


        configManager.getConfig().set(x, l.getX());
        configManager.getConfig().set(y, l.getY());
        configManager.getConfig().set(z, l.getZ());
        configManager.getConfig().set(pitch, l.getPitch());
        configManager.getConfig().set(yaw, l.getYaw());
        configManager.getConfig().set(world, l.getWorld().getName());

        configManager.saveConfig();*/

        String output = "Home " + homeName + " has been saved successfully!";
        commandSender.sendMessage(output);

        return true;
    }
}
