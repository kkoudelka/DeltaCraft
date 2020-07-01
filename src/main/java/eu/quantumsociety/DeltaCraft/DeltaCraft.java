package eu.quantumsociety.DeltaCraft;

import eu.quantumsociety.DeltaCraft.commands.home.HomeCommand;
import eu.quantumsociety.DeltaCraft.commands.spectate.SpectateCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class DeltaCraft extends JavaPlugin {
    @Override
    public void onEnable() {
        final Logger logger = getLogger();

        // Commands
        // this.getCommand("sethome").setExecutor(new SethomeCommand());
        this.getCommand("home").setExecutor(new HomeCommand());
        logger.info("Home loaded");
        this.getCommand("c").setExecutor(new SpectateCommand());
        logger.info("Spectate loaded");

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
