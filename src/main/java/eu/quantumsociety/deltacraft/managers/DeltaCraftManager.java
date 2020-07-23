package eu.quantumsociety.deltacraft.managers;

import eu.quantumsociety.deltacraft.DeltaCraft;
import eu.quantumsociety.deltacraft.managers.cache.*;
import eu.quantumsociety.deltacraft.utils.enums.Settings;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DeltaCraftManager {
    private final DeltaCraft plugin;

    private boolean endAccess;
    private String newestVersion;

    private final KelpCacheManager kelpCacheManager;
    private final SpectateCacheManager spectateCacheManager;
    private final AfkCacheManager afkCacheManager;
    private final ItemFrameCacheManager itemFrameCacheManager;
    private final FakePlayerManager fakePlayerHelper;

    public DeltaCraftManager(DeltaCraft plugin) {
        this.plugin = plugin;

        this.endAccess = plugin.getConfig().getBoolean(Settings.END.getPath());

        this.kelpCacheManager = new KelpCacheManager(plugin);
        this.spectateCacheManager = new SpectateCacheManager(plugin);
        this.afkCacheManager = new AfkCacheManager(plugin);
        this.fakePlayerHelper = new FakePlayerManager(plugin);
        this.itemFrameCacheManager = new ItemFrameCacheManager(plugin);
    }

    public KelpCacheManager getKelpCacheManager() {
        return kelpCacheManager;
    }

    public SpectateCacheManager getSpectateCacheManager() {
        return spectateCacheManager;
    }

    public AfkCacheManager getAfkCacheManager() {
        return afkCacheManager;
    }

    public FakePlayerManager getFakePlayerManager() {
        return fakePlayerHelper;
    }

    public ItemFrameCacheManager getItemFrameCacheManager() {
        return itemFrameCacheManager;
    }

    /**
     * @return If player can enter end dimension
     */
    public boolean getEndAccess() {
        return endAccess;
    }

    public void setEndAccess(boolean endAccess) {
        this.endAccess = endAccess;

        this.plugin.getConfig().set(Settings.END.getPath(), this.endAccess);
        this.plugin.saveConfig();
    }

    /**
     * @return The newest (cached) version on github
     */
    public String getNewestVersion() {
        return newestVersion;
    }

    public String checkNewestVersion() {
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
            this.plugin.getLogger().warning(ex.toString());
            return "Url is not formatted correctly";
        } catch (FileNotFoundException ex) {
            return "Version file not found";
        } catch (IOException ex) {
            this.plugin.getLogger().warning(ex.toString());
            return "Could not check version";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        String version = builder.toString();
        this.newestVersion = version;
        return version;
    }

}
