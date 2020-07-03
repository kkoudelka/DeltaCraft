package eu.quantumsociety.DeltaCraft.commands.home;

import eu.quantumsociety.DeltaCraft.classes.PlayerHome;
import eu.quantumsociety.DeltaCraft.managers.ConfigManager;
import eu.quantumsociety.DeltaCraft.managers.HomesManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
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
        List<PlayerHome> list = configManager.getPlayerHomes(p);

        for (PlayerHome ph: list) {

        }
        ComponentBuilder t = new ComponentBuilder( "Hello " )
                .color( net.md_5.bungee.api.ChatColor.RED ).bold( true ).append( "world" )
                .color( net.md_5.bungee.api.ChatColor.DARK_RED ).append( "!" )
                .color( net.md_5.bungee.api.ChatColor.RED );
        ComponentBuilder b = new ComponentBuilder("=====================================").color(ChatColor.BOLD).color(ChatColor.GOLD);

        p.spigot().sendMessage(t.create());




        b.append("=====================================").color(ChatColor.BOLD).color(ChatColor.GOLD);


        p.sendMessage(list.get(0).location.toString());

        return true;
    }
}
