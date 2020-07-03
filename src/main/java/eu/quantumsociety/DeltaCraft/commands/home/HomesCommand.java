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
        String divider = "====================================";
        ComponentBuilder text = new ComponentBuilder(divider).color(ChatColor.DARK_GRAY).append("\n").bold(true);

        for (PlayerHome ph : list) {


            text
                    .append(" - ")
                    .append("[").color(ChatColor.DARK_AQUA).bold(true)
                    .append(ph.homeName).color(ChatColor.GOLD).bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ("/home " + ph.homeName)))
                    .append("]").color(ChatColor.DARK_AQUA).bold(true)
                    .append("   ")
                    .append("[").color(ChatColor.DARK_GRAY).bold(false)
                    .append("✗").color(ChatColor.DARK_RED)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ("/delhome " + ph.homeName)))
                    .append("]").color(ChatColor.DARK_GRAY)
                    .append("\n").reset().bold(true);

        }
        text.append(divider).color(ChatColor.DARK_GRAY);

        p.spigot().sendMessage(text.create());

        return true;
    }
}
