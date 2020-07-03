package eu.quantumsociety.DeltaCraft.commands.home;

import eu.quantumsociety.DeltaCraft.classes.PlayerHome;
import eu.quantumsociety.DeltaCraft.managers.ConfigManager;
import eu.quantumsociety.DeltaCraft.managers.HomesManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomesCommand implements CommandExecutor {
    private HomesManager configManager;

    public HomesCommand(HomesManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command");
            return true;
        }

        Player p = (Player) commandSender;
        ComponentBuilder b = new ComponentBuilder("=====================================").color(ChatColor.BOLD).color(ChatColor.GOLD);

        List<PlayerHome> homes = new ArrayList<PlayerHome>();


        b.append("=====================================").color(ChatColor.BOLD).color(ChatColor.GOLD);

        ComponentBuilder b2 = new ComponentBuilder("Hello").append(new TextComponent("a"));

        List<PlayerHome> list = configManager.getPlayerHomes(p);

        p.sendMessage(list.get(0).location.toString());

        return true;
    }
}
