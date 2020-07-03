package eu.quantumsociety.DeltaCraft;

import eu.quantumsociety.DeltaCraft.commands.home.HomeCommand;
import eu.quantumsociety.DeltaCraft.commands.home.SetHomeCommand;
import eu.quantumsociety.DeltaCraft.commands.kelp.KelpCommand;
import eu.quantumsociety.DeltaCraft.commands.spectate.SpectateCommand;
import eu.quantumsociety.DeltaCraft.listeners.MoveListener;
import eu.quantumsociety.DeltaCraft.listeners.SpectateMoveListener;
import eu.quantumsociety.DeltaCraft.managers.ConfigManager;
import eu.quantumsociety.DeltaCraft.managers.DeltaCraftManager;
import eu.quantumsociety.DeltaCraft.utils.enums.Settings;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class DeltaCraft extends JavaPlugin {
    private ConfigManager homeConfigManager;
    private ConfigManager spectateConfigManager;
    private ConfigManager kelpConfigManager;
    private DeltaCraftManager manager;

    private boolean isDebug;

    @Override
    public void onEnable() {
        // Create managers
        this.manager = new DeltaCraftManager(this);
        this.homeConfigManager = new ConfigManager(this, "home.yml");
        this.spectateConfigManager = new ConfigManager(this, "spectate.yml");
        this.kelpConfigManager = new ConfigManager(this, "kelp.yml");

        // Load config
        this.loadConfig();
        this.isDebug = getConfig().getBoolean(Settings.DEBUG.getPath());
        if (this.isDebug) {
            this.debugMsg("Debugging enabled");
        }

        // Commands
        this.getCommand("sethome").setExecutor(new SetHomeCommand(homeConfigManager));
        debugMsg("SetHome loaded");
        this.getCommand("home").setExecutor(new HomeCommand(homeConfigManager));
        debugMsg("Home loaded");
        this.getCommand("c").setExecutor(new SpectateCommand(spectateConfigManager, this));
        debugMsg("Spectate loaded");
        this.getCommand("kelp").setExecutor(new KelpCommand(kelpConfigManager, this));
        debugMsg("Kelp farms loaded");

        // Events
        PluginManager plm = this.getServer().getPluginManager();
        plm.registerEvents(new SpectateMoveListener(this), this);
        plm.registerEvents(new MoveListener(this), this);
        debugMsg("2. listener loaded");

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
