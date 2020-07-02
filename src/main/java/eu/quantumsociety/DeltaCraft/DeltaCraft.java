package eu.quantumsociety.DeltaCraft;

import eu.quantumsociety.DeltaCraft.commands.home.HomeCommand;
import eu.quantumsociety.DeltaCraft.commands.home.SetHomeCommand;
import eu.quantumsociety.DeltaCraft.commands.spectate.SpectateCommand;
import eu.quantumsociety.DeltaCraft.managers.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class DeltaCraft extends JavaPlugin {
    public ConfigManager spectateManager;
    public ConfigManager homeManager;

    @Override
    public void onEnable() {
        final Logger logger = getLogger();

        this.spectateManager = new ConfigManager(this, "spectate.yml");
        this.homeManager = new ConfigManager(this, "home.yml");

        // Commands
        this.getCommand("sethome").setExecutor(new SetHomeCommand(homeManager));
        logger.info("SetHome loaded");
        this.getCommand("home").setExecutor(new HomeCommand(homeManager));
        logger.info("Home loaded");
        this.getCommand("c").setExecutor(new SpectateCommand(spectateManager));
        logger.info("Spectate loaded");

        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.saveConfig();

        super.onDisable();
    }

}
