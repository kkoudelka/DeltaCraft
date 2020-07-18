package eu.quantumsociety.deltacraft.commands.main;

import eu.quantumsociety.deltacraft.DeltaCraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final DeltaCraft plugin;

    public MainCommand(DeltaCraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1 || args[0].isEmpty()) {

            return true;
        }

        String cmd = args[0];
        if (cmd.equalsIgnoreCase("reload")) {

            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this commands");
            return false;
        }
        Player p = (Player) sender;

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (!(sender instanceof Player)) {
            return list;
        }

        String[] cmds = {"show", "reload", "change"};

        String typedIn = "";
        if (args.length == 1) {
            typedIn = args[0].toLowerCase();
        }

        for (String cmd : cmds) {
            if (cmd.startsWith(typedIn)) {
                list.add(cmd);
            }
        }
        return list;
    }
}
