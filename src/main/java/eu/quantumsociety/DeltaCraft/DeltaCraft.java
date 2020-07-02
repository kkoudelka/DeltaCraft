package eu.quantumsociety.DeltaCraft;

import eu.quantumsociety.DeltaCraft.commands.home.HomeCommand;
import eu.quantumsociety.DeltaCraft.commands.home.SetHomeCommand;
import eu.quantumsociety.DeltaCraft.commands.spectate.SpectateCommand;
import eu.quantumsociety.DeltaCraft.listeners.SpectateMoveListener;
import eu.quantumsociety.DeltaCraft.managers.ConfigManager;
import eu.quantumsociety.DeltaCraft.managers.DeltaCraftManager;
import eu.quantumsociety.DeltaCraft.utils.enums.Settings;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class DeltaCraft extends JavaPlugin {
    private ConfigManager spectateConfigManager;
    private ConfigManager homeConfigManager;
    private DeltaCraftManager manager;

    private boolean isDebug;

    @Override
    public void onEnable() {
        final Logger logger = getLogger();

        this.manager = new DeltaCraftManager(this);
        this.spectateConfigManager = new ConfigManager(this, "spectate.yml");
        this.homeConfigManager = new ConfigManager(this, "home.yml");

        // Load config
        this.loadConfig();
        this.isDebug = getConfig().getBoolean(Settings.DEBUG.getPath());
        if (this.isDebug) {
            this.debugMsg("Debugging enabled");
        }

        // Commands
        this.getCommand("sethome").setExecutor(new SetHomeCommand(homeConfigManager));
        logger.info("SetHome loaded");
        this.getCommand("home").setExecutor(new HomeCommand(homeConfigManager));
        logger.info("Home loaded");
        this.getCommand("c").setExecutor(new SpectateCommand(spectateConfigManager, this));
        logger.info("Spectate loaded");

        // Events
        PluginManager plm = this.getServer().getPluginManager();
        plm.registerEvents(new SpectateMoveListener(this), this);

//        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.saveConfig();

        super.onDisable();
    }

    private void loadConfig() {
        saveDefaultConfig();

        getConfig().options().copyDefaults(true);

        getConfig().options().header(getHeader());
        saveConfig();
    }

    private String getHeader() {
        String sep = System.getProperty("line.separator");

        return "###################" + sep
                + "DeltaCraft v." + this.getDescription().getVersion() + sep
                + "###################";

    }

    public DeltaCraftManager getManager() {
        return manager;
    }

    public boolean setDebug(boolean debug) {
        this.isDebug = debug;

        getConfig().set(Settings.DEBUG.getPath(), this.isDebug);
        saveConfig();

        return this.isDebug;
    }

    public boolean isInDebug() {
        return this.isDebug;
    }

    public void debugMsg(String message) {
        if (isInDebug()) {
            getLogger().info("[Debug]: " + message);
        }
    }

}
