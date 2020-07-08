package eu.quantumsociety.deltacraft;

import eu.quantumsociety.deltacraft.commands.home.DelHomeCommand;
import eu.quantumsociety.deltacraft.commands.home.HomeCommand;
import eu.quantumsociety.deltacraft.commands.home.HomesCommand;
import eu.quantumsociety.deltacraft.commands.home.SetHomeCommand;
import eu.quantumsociety.deltacraft.commands.kelp.KelpCommand;
import eu.quantumsociety.deltacraft.commands.spectate.SpectateCommand;
import eu.quantumsociety.deltacraft.listeners.ComposterListener;
import eu.quantumsociety.deltacraft.listeners.KelpGrowListener;
import eu.quantumsociety.deltacraft.listeners.SpectateMoveListener;
import eu.quantumsociety.deltacraft.managers.*;
import eu.quantumsociety.deltacraft.utils.enums.Settings;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DeltaCraft extends JavaPlugin {
    private DeltaCraftManager manager;

    private HomesManager homeConfigManager;
    private SpectateManager spectateConfigManager;
    private KelpManager kelpConfigManager;

    private boolean isDebug;

    @Override
    public void onEnable() {
        // Load config
        this.loadConfig();
        this.isDebug = getConfig().getBoolean(Settings.DEBUG.getPath());
        if (this.isDebug) {
            this.debugMsg("Debugging enabled");
        }

        // Create managers
        this.manager = new DeltaCraftManager(this);
        this.homeConfigManager = new HomesManager(this);
        this.spectateConfigManager = new SpectateManager(this, this.manager.getSpectateCacheManager());
        this.kelpConfigManager = new KelpManager(this, this.manager.getKelpCacheManager());

        // Home commands
        this.getCommand("sethome").setExecutor(new SetHomeCommand(homeConfigManager, this));
        debugMsg("SetHome loaded");
        this.getCommand("home").setExecutor(new HomeCommand(homeConfigManager));
        debugMsg("Home loaded");
        this.getCommand("homes").setExecutor(new HomesCommand(homeConfigManager));
        debugMsg("Homes loaded");
        this.getCommand("delhome").setExecutor(new DelHomeCommand(homeConfigManager));
        debugMsg("DelHome loaded");
        this.getCommand("c").setExecutor(new SpectateCommand(spectateConfigManager, this, this.manager.getFakePlayerManager()));
        debugMsg("Spectate loaded");
        this.getCommand("kelp").setExecutor(new KelpCommand(kelpConfigManager, this));
        debugMsg("Kelp farms loaded");

        // Events
        PluginManager plm = this.getServer().getPluginManager();
        plm.registerEvents(new SpectateMoveListener(this), this);
        debugMsg("Spectate listener loaded");
        plm.registerEvents(new KelpGrowListener(this), this);
        debugMsg("Kelp listener loaded");
        plm.registerEvents(new ComposterListener(this), this);
        debugMsg("Composter listener loaded");

        debugMsg("Loaded " + manager.getKelpCacheManager().getCount() + " kelp regions");

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
