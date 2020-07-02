package eu.quantumsociety.DeltaCraft;

import eu.quantumsociety.DeltaCraft.commands.home.HomeCommand;
import eu.quantumsociety.DeltaCraft.commands.home.SetHomeCommand;
import eu.quantumsociety.DeltaCraft.commands.spectate.SpectateCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class DeltaCraft extends JavaPlugin {
    public DataManager spectateManager;
    public DataManager homeManager;

    @Override
    public void onEnable() {
        final Logger logger = getLogger();

        this.spectateManager = new DataManager(this, "spectate.yml");
        this.homeManager = new DataManager(this, "home.yml");

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
