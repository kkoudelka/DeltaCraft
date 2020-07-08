package eu.quantumsociety.deltacraft.managers.templates;

import eu.quantumsociety.deltacraft.DeltaCraft;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;

public  class ConfigManager {
    protected final DeltaCraft plugin;
    private final String fileName;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public ConfigManager(DeltaCraft plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;

        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), fileName);
        }
        dataConfig = YamlConfiguration.loadConfiguration(configFile);

        // Look for defaults in the jar
        InputStream defaultStream = this.plugin.getResource(fileName);
        if (defaultStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            dataConfig.setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (dataConfig == null) {
            reloadConfig();
        }
        return dataConfig;
    }

    public void saveConfig() {
        if (dataConfig == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(this.plugin.getDataFolder(), fileName);
        }
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
    }

    public void setLocation(String path, Location l) {
        FileConfiguration config = this.getConfig();

        config.set(path, l);
    }

    public Location getLocation(String path) {
        FileConfiguration config = this.getConfig();
        if (!config.contains(path)) {
            return null;
        }
        return (Location) config.get(path);
    }
}
