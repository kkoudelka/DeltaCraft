package eu.quantumsociety.DeltaCraft.classes;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerHome {
    private Player player;
    private String homeName;
    private Location location;

    public PlayerHome(Player player, String homeName, Location location) {
        this.player = player;
        this.homeName = homeName;
        this.location = location;
    }
}
