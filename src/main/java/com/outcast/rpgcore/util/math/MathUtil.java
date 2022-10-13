package com.outcast.rpgcore.util.math;

import com.outcast.rpgcore.RPGCore;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MathUtil {

    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    //  Clamping methods.
    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public static <T extends Number & Comparable> T clamp(T val, T min, T max) {
        try {
            return compareTo(val, min) > 0 ? (compareTo(val, min) < 0 ? val : max) : min;
        } catch (ClassCastException e) {
            RPGCore.getInstance().getLogger().warning("MathUtil::clamp(): attempted clamp " +
                    "involving a non-number.");
            return val;
        }
    }

    public static double clampOne(double val) {
        return clamp(val, 0.0, 1.0);
    }

    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    //  Comparison
    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public static <T extends Comparable> int compareTo(T val1, T val2) {
        try {
            return val1.compareTo(val2);
        } catch (ClassCastException e) {
            RPGCore.getInstance().getLogger().warning("MathUtil::compareTo(): " +
                    "attempted comparison of non-comparable types.");
            return 0;
        }
    }


    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    //  Checking X direction.
    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public static boolean isXDirection(Player player) {
        org.bukkit.util.Vector u = player.getLocation().getDirection();
        u = (new org.bukkit.util.Vector(u.getX(), 0.0D, u.getZ())).normalize();
        org.bukkit.util.Vector v = new Vector(0, 0, -1);
        double magU = Math.sqrt(Math.pow(u.getX(), 2.0D) + Math.pow(u.getZ(), 2.0D));
        double magV = Math.sqrt(Math.pow(v.getX(), 2.0D) + Math.pow(v.getZ(), 2.0D));
        double angle = Math.acos(u.dot(v) / magU * magV);
        angle = angle * 180.0D / Math.PI;
        angle = Math.abs(angle - 180.0D);
        return (angle <= 45.0D || angle > 135.0D);
    }


    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    //  Calculation for distance on a three dimensional plane
    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public static double distance(Location start, Location end) {
        return Math.sqrt(Math.pow((start.getX()-end.getX()), 2) +
                Math.pow((start.getY()-end.getY()), 2) +
                Math.pow((start.getZ()-end.getZ()), 2));
    }

}
