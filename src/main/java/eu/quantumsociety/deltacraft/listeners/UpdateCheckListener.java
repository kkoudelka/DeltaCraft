package eu.quantumsociety.deltacraft.listeners;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.utils.TextHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateCheckListener implements Listener {

    private final String localVersion;
    private final String newestVersion;

    public UpdateCheckListener(DeltaCraft plugin) {
        this.localVersion = plugin.getDescription().getVersion();
        this.newestVersion = plugin.getManager().getNewestVersion();
    }

    @EventHandler(ignoreCancelled = true)
    public void onOpJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.isOp()) {
            return;
        }
        if (!localVersion.equals(newestVersion)) {
            p.spigot().sendMessage(TextHelper.attentionText("[DELTACRAFT] There is a new updated version available!"));
            p.spigot().sendMessage(TextHelper.infoText("Current version: §a" + localVersion, ChatColor.WHITE));
            p.spigot().sendMessage(TextHelper.infoText("Newest version: §a" + newestVersion, ChatColor.WHITE));

            TextComponent t = new TextComponent("https://github.com/kkoudelka/DeltaCraft/releases");
            t.setColor(ChatColor.DARK_AQUA);
            t.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/kkoudelka/DeltaCraft/releases"));
            ComponentBuilder builder = new ComponentBuilder("Click to download: ")
                    .append(t);

            p.spigot().sendMessage(builder.create());
            p.sendMessage("");
        }
    }


}
