package eu.quantumsociety.deltacraft.utils;

import org.bukkit.Location;

public class MathHelper {

    public static double calcDistance(Location one, Location two) throws Exception {
        if (one.getWorld().getName() != two.getWorld().getName()) {
            throw new Exception("Different world");
        }

        // d = sqrt((x2-x1)^2 + (y2-y1)^2 + (z2-z1)^2)

        double xDiff = two.getX() - one.getX();
        double yDiff = two.getY() - one.getY();
        double zDiff = two.getZ() - one.getZ();

        double x = xDiff * xDiff;
        double y = yDiff * yDiff;
        double z = zDiff * zDiff;

        return Math.sqrt(x + y + z);
    }
}
