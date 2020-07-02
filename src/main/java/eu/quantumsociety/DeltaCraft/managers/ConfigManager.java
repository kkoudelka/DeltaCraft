package eu.quantumsociety.DeltaCraft.managers;

import eu.quantumsociety.DeltaCraft.DeltaCraft;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;

public class ConfigManager {
    private final DeltaCraft plugin;
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
}
