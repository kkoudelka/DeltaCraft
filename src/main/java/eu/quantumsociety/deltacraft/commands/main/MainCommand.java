package eu.quantumsociety.deltacraft.commands.main;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.utils.TextHelper;
import eu.quantumsociety.deltacraft.utils.enums.Permissions;
import eu.quantumsociety.deltacraft.utils.enums.Settings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final DeltaCraft plugin;

    public MainCommand(DeltaCraft plugin) {
        this.plugin = plugin;
    }

    private Settings[] getAllSettings() {
        return Settings.values();
    }

    private List<String> getAllSettingsKeys() {
        return Arrays.stream(this.getAllSettings()).map(Settings::getPath).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1 || args[0].isEmpty()) {
            sender.sendMessage(ChatColor.GREEN + "Use " + ChatColor.YELLOW + "/deltacraft ? " + ChatColor.GREEN + "for help");
            return true;
        }

        String cmd = args[0];
        if (cmd.equalsIgnoreCase("reload")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission(Permissions.CONFIGRELOAD.getPath())) {
                    p.spigot().sendMessage(TextHelper.insufficientPermissions(Permissions.CONFIGRELOAD));
                    return true;
                }
            }

            this.reloadConfig(sender);
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this commands");
            return true;
        }
        Player p = (Player) sender;
        if (cmd.equalsIgnoreCase("version")) {
            if (!p.hasPermission(Permissions.SHOWVERSION.getPath())) {
                p.spigot().sendMessage(TextHelper.insufficientPermissions(Permissions.SHOWVERSION));
                return true;
            }
            this.versionCommand(p);
            return true;
        }
        if (cmd.equalsIgnoreCase("help") || cmd.equalsIgnoreCase("?")) {
            this.sendHelp(p);
            return true;
        }
        if (cmd.equalsIgnoreCase("change")) {
            if (!p.hasPermission(Permissions.CONFIGCHANGE.getPath())) {
                p.spigot().sendMessage(TextHelper.insufficientPermissions(Permissions.CONFIGCHANGE));
                return true;
            }
            if (args.length < 2 || args[1].isEmpty()) {
                p.spigot().sendMessage(TextHelper.attentionText("Key is empty"));
                return true;
            }
            if (args.length < 3 || args[2].isEmpty()) {
                p.spigot().sendMessage(TextHelper.attentionText("Value is empty"));
                return true;
            }
            String key = args[1];
            String newVal = args[2];
            this.changeConfig(p, key, newVal);
            return true;
        }
        if (cmd.equalsIgnoreCase("show")) {
            if (!p.hasPermission(Permissions.CONFIGSHOW.getPath())) {
                p.spigot().sendMessage(TextHelper.insufficientPermissions(Permissions.CONFIGSHOW));
                return true;
            }
            this.showCurrentSettings(p);
            return true;
        }


        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (!(sender instanceof Player)) {
            return list;
        }

        String[] cmds = {"show", "reload", "change", "version"};

        String typedIn;
        if (args.length == 1) {
            typedIn = args[0].toLowerCase();
        } else {
            return list;
        }

        for (String cmd : cmds) {
            if (cmd.startsWith(typedIn)) {
                list.add(cmd);
            }
        }
        return list;
    }

    private void versionCommand(Player p) {
        String localVersion = this.plugin.getDescription().getVersion();
        p.spigot().sendMessage(TextHelper.infoText("Current version: §a" + localVersion, net.md_5.bungee.api.ChatColor.WHITE));

        this.plugin.getManager().checkNewestVersion();
        String newestVersion = this.plugin.getManager().getNewestVersion();
        if (!localVersion.equals(newestVersion)) {
            p.spigot().sendMessage(TextHelper.attentionText("[DELTACRAFT] There is a new updated version available!"));
        }
        p.spigot().sendMessage(TextHelper.infoText("Newest version: §a" + newestVersion, net.md_5.bungee.api.ChatColor.WHITE));
    }

    private void sendHelp(Player p) {
        String text = "DeltaCraft main commands =========================\n";
        if (p.hasPermission(Permissions.SHOWVERSION.getPath())) {
            text += ChatColor.YELLOW + "/deltacraft version " + ChatColor.GREEN + " Show current version of the plugin \n";
        }
        if (p.hasPermission(Permissions.CONFIGSHOW.getPath())) {
            text += ChatColor.YELLOW + "/deltacraft show " + ChatColor.GREEN + " Show settings in config \n";
        }
        if (p.hasPermission(Permissions.CONFIGCHANGE.getPath())) {
            text += ChatColor.YELLOW + "/deltacraft change <key> <value>" + ChatColor.GREEN + " Change setting in config \n";
        }
        if (p.hasPermission(Permissions.CONFIGRELOAD.getPath())) {
            text += ChatColor.YELLOW + "/deltacraft reload" + ChatColor.GREEN + " Reload plugin settings \n";
        }
        text += ChatColor.WHITE + "==================================================";
        p.sendMessage(text);
    }

    private void reloadConfig(CommandSender sender) {
        // TODO: Reload config
    }

    private void changeConfig(Player p, String key, String value) {
        // TODO: Check key and change value of settings
    }

    private void showCurrentSettings(Player p) {
        p.spigot().sendMessage(TextHelper.getDivider());

        FileConfiguration config = this.plugin.getConfig();
        for (String key : getAllSettingsKeys()) {
            String value = config.getString(key);
            if (value == null || value.isEmpty()) {
                value = "null";
            }
            String newVal = "";
            if (value.equalsIgnoreCase("true")) {
                newVal = "false";
            }
            if (value.equalsIgnoreCase("false")) {
                newVal = "true";
            }

            BaseComponent[] toSend = new ComponentBuilder()
                    .append(
                            TextHelper.createActionButton(
                                    new ComponentBuilder(value)
                                            .event(
                                                    new ClickEvent(
                                                            ClickEvent.Action.SUGGEST_COMMAND, "/deltacraft change " + key + " " + newVal
                                                    )
                                            )
                                            .event(
                                                    new HoverEvent(
                                                            HoverEvent.Action.SHOW_TEXT, new Text("Change value")
                                                    )
                                            )
                                            .create(),
                                    ChatColor.GREEN
                            )
                    )
                    .append("   ")
                    .reset()
                    .append(key.replace("settings.", ""))
                    .create();
            p.spigot().sendMessage(toSend);
        }

        p.spigot().sendMessage(TextHelper.getDivider());
    }
}
