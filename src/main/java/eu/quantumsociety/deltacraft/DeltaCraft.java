package eu.quantumsociety.deltacraft;

import eu.quantumsociety.deltacraft.commands.home.DelHomeCommand;
import eu.quantumsociety.deltacraft.commands.home.HomeCommand;
import eu.quantumsociety.deltacraft.commands.home.HomesCommand;
import eu.quantumsociety.deltacraft.commands.home.SetHomeCommand;
import eu.quantumsociety.deltacraft.commands.kelp.KelpCommand;
import eu.quantumsociety.deltacraft.commands.spectate.SpectateCommand;
import eu.quantumsociety.deltacraft.listeners.MoveListener;
import eu.quantumsociety.deltacraft.listeners.SpectateMoveListener;
import eu.quantumsociety.deltacraft.managers.ConfigManager;
import eu.quantumsociety.deltacraft.managers.DeltaCraftManager;
import eu.quantumsociety.deltacraft.managers.HomesManager;
import eu.quantumsociety.deltacraft.utils.enums.Settings;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DeltaCraft extends JavaPlugin {
    private HomesManager homeConfigManager;
    private ConfigManager spectateConfigManager;
    private ConfigManager kelpConfigManager;
    private DeltaCraftManager manager;

    private boolean isDebug;

    @Override
    public void onEnable() {
        // Create managers
        this.manager = new DeltaCraftManager(this);
        this.homeConfigManager = new HomesManager(this);
        this.spectateConfigManager = new ConfigManager(this, "spectate.yml");
        this.kelpConfigManager = new ConfigManager(this, "kelp.yml");

        // Load config
        this.loadConfig();
        this.isDebug = getConfig().getBoolean(Settings.DEBUG.getPath());
        if (this.isDebug) {
            this.debugMsg("Debugging enabled");
        }

        // Home commands
        this.getCommand("sethome").setExecutor(new SetHomeCommand(homeConfigManager));
        debugMsg("SetHome loaded");
        this.getCommand("home").setExecutor(new HomeCommand(homeConfigManager));
        this.getCommand("homes").setExecutor(new HomesCommand(homeConfigManager));
        this.getCommand("delhome").setExecutor(new DelHomeCommand(homeConfigManager));
        debugMsg("Home submodule loaded");
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
