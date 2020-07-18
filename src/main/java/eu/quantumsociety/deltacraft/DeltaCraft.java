package eu.quantumsociety.deltacraft;

import eu.quantumsociety.deltacraft.commands.home.DelHomeCommand;
import eu.quantumsociety.deltacraft.commands.home.HomeCommand;
import eu.quantumsociety.deltacraft.commands.home.HomesCommand;
import eu.quantumsociety.deltacraft.commands.home.SetHomeCommand;
import eu.quantumsociety.deltacraft.commands.itemframe.InvCommand;
import eu.quantumsociety.deltacraft.commands.kelp.KelpCommand;
import eu.quantumsociety.deltacraft.commands.other.KahyCommand;
import eu.quantumsociety.deltacraft.commands.spectate.SpectateCommand;
import eu.quantumsociety.deltacraft.listeners.*;
import eu.quantumsociety.deltacraft.managers.*;
import eu.quantumsociety.deltacraft.recipes.QuartzRecipe;
import eu.quantumsociety.deltacraft.utils.enums.Settings;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DeltaCraft extends JavaPlugin {
    private DeltaCraftManager manager;

    private HomesManager homeConfigManager;
    private SpectateManager spectateConfigManager;
    private KelpManager kelpConfigManager;

    private boolean isDebug;
    private String newestVersion;

    @Override
    public void onEnable() {
        // Load config
        this.loadConfig();
        this.isDebug = getConfig().getBoolean(Settings.DEBUG.getPath());
        if (this.isDebug) {
            this.debugMsg("Debugging enabled");
        }

        // Check version
        this.newestVersion = this.getRemoteVersion();

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
        this.getCommand("kahy").setExecutor(new KahyCommand(this));
        debugMsg("Kahy command loaded");
        this.getCommand("inv").setExecutor(new InvCommand(this));
        debugMsg("ItemFrame command loaded");

        // Events
        PluginManager plm = this.getServer().getPluginManager();
        plm.registerEvents(new SpectateListener(this), this);
        debugMsg("Spectate listener loaded");
        plm.registerEvents(new KelpGrowListener(this), this);
        debugMsg("Kelp listener loaded");
        plm.registerEvents(new CampfireListener(this), this);
        debugMsg("Campfire listener loaded");
        plm.registerEvents(new ComposterListener(this), this);
        debugMsg("Composter listener loaded");
        plm.registerEvents(new SpawnerDestroyListener(), this);
        debugMsg("Spawner destroy listener loaded");
        plm.registerEvents(new ShulkerKillListener(), this);
        debugMsg("Shulker kill listener loaded");
        plm.registerEvents(new EndTeleportListener(this), this);
        debugMsg("End restriction listener loaded");
        plm.registerEvents(new KahyProtectionListener(this), this);
        debugMsg("Kahy protection listener loaded");
        plm.registerEvents(new UpdateCheckListener(this), this);
        debugMsg("UpdateCheck listener loaded");
        plm.registerEvents(new ItemFrameListener(this), this);
        debugMsg("ItemFrame listener loaded");

        debugMsg("Loaded " + manager.getKelpCacheManager().getCount() + " kelp regions");

        // Recipes
        QuartzRecipe.registerRecipe(this);
        debugMsg("Quartz recipe registered");
    }

    @Override
    public void onDisable() {
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

    private void debugMsg(String message) {
        if (isInDebug()) {
            getLogger().info("[Debug]: " + message);
        }
    }

    private String getRemoteVersion() {
        StringBuilder builder = new StringBuilder();
        HttpURLConnection connection = null;
        try {
            // TODO: Public repository
            URL url = new URL("https://raw.githubusercontent.com/kkoudelka/DeltaCraft/master/VERSION");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            // Buffer
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
            in.close();
        } catch (MalformedURLException ex) {
            this.debugMsg(ex.toString());
            return "Url is not formatted correctly";
        } catch (FileNotFoundException ex) {
            return "Version file not found";
        } catch (IOException ex) {
            this.debugMsg(ex.toString());
            return "Could not check version";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return builder.toString();
    }

    public String getNewestVersion() {
        return newestVersion;
    }
}
