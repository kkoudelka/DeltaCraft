package eu.quantumsociety.deltacraft.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class Extensions {
    public static final String idiotKey = "isIdiot";
    public static final String kahyProtectionKey = "kahyProtection";


    public static boolean isIdiot(Player p) {
        return p.hasMetadata(idiotKey);
    }

    public static boolean hasProtection(Player p) {
        return p.hasMetadata(kahyProtectionKey);
    }

    public static FixedMetadataValue getFakeMetadata(JavaPlugin plugin) {
        return new FixedMetadataValue(plugin, true);
    }

    public static boolean isKahy(Player p) {
        return isKahy(p.getDisplayName());
    }

    public static boolean isKahy(String name) {
        return name.toLowerCase().contains("kahy");
    }


    /**
     * @param one First location
     * @param two Second location
     * @return if both location are same (only yaw and pitch changed)
     */
    public static boolean areSame(Location one, Location two) {
        if (one.equals(two)) {
            return true;
        }
        if (one.distance(two) > 0.1) {
            return false;
        }
        return true;
    }

}
