package com.outcast.rpgcore.util.math.geometry;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class VectorUtil {

    /**
     * Rotates a vector around the X axis at an angle
     *
     * @param v Starting vector
     * @param angle How much to rotate
     * @return The starting vector rotated
     */
    public static Vector rotateAroundAxisX(Vector v, double angle) {
        double y, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        y = v.getY() * cos - v.getZ() * sin;
        z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    /**
     * Rotates a vector around the Y axis at an angle
     *
     * @param v Starting vector
     * @param angle How much to rotate
     * @return The starting vector rotated
     */
    public static Vector rotateAroundAxisY(Vector v, double angle) {
        double x, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    /**
     * Rotates a vector around the Z axis at an angle
     *
     * @param v Starting vector
     * @param angle How much to rotate
     * @return The starting vector rotated
     */
    public static Vector rotateAroundAxisZ(Vector v, double angle) {
        double x, y, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos - v.getY() * sin;
        y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }

    /**
     * Rotates a vector around the X, Y, and Z axes
     *
     * @param v The starting vector
     * @param angleX The change angle on X
     * @param angleY The change angle on Y
     * @param angleZ The change angle on Z
     * @return The starting vector rotated
     */
    public static Vector rotateVector(Vector v, double angleX, double angleY, double angleZ) {
        rotateAroundAxisX(v, angleX);
        rotateAroundAxisY(v, angleY);
        rotateAroundAxisZ(v, angleZ);
        return v;
    }

    /**
     * Rotate a vector about a location using that location's direction
     *
     * @param v The starting vector
     * @param location The location to rotate around
     * @return The starting vector rotated
     */
    public static Vector rotateVector(Vector v, Location location) {
        return rotateVector(v, location.getYaw(), location.getPitch());
    }

    /**
     * This handles non-unit vectors, with yaw and pitch instead of X,Y,Z angles.
     *
     * Thanks to SexyToad!
     *
     * @param v The starting vector
     * @param yawDegrees The yaw offset in degrees
     * @param pitchDegrees The pitch offset in degrees
     * @return The starting vector rotated
     */
    public static Vector rotateVector(Vector v, float yawDegrees, float pitchDegrees) {
        double yaw = Math.toRadians(-1 * (yawDegrees + 90));
        double pitch = Math.toRadians(-pitchDegrees);

        double cosYaw = Math.cos(yaw);
        double cosPitch = Math.cos(pitch);
        double sinYaw = Math.sin(yaw);
        double sinPitch = Math.sin(pitch);

        double initialX, initialY, initialZ;
        double x, y, z;

        // Z_Axis rotation (Pitch)
        initialX = v.getX();
        initialY = v.getY();
        x = initialX * cosPitch - initialY * sinPitch;
        y = initialX * sinPitch + initialY * cosPitch;

        // Y_Axis rotation (Yaw)
        initialZ = v.getZ();
        initialX = x;
        z = initialZ * cosYaw - initialX * sinYaw;
        x = initialZ * sinYaw + initialX * cosYaw;

        return new Vector(x, y, z);
    }

    /**
     * Determines if the source location can "see" the target location. Does not take line of sight into account.
     * An example use case would be having a "guard" NPC detect players up to 10 meters and 60 degrees in front of them,
     * allowing players to sneak behind the NPC without attracting its attention.
     * @param visionRadius The angle, in degrees, that the source's vision covers. A vision radius of 90 degrees
     *                     can see up to 45 degrees left or right, covering a total of 90 degrees of vision.
     * @param visionRange The maximum distance, in meters (blocks), that the source can "see".
     */
    public static boolean canSee(Location source, Location target, float visionRadius, double visionRange) {
        // if the source location is blind
        if (visionRadius == 0f || visionRange == 0f)
            return false;

        // source's direction
        Vector dir = source.getDirection().normalize();
        // difference between source and target locations
        Vector diff = target.toVector().subtract(source.toVector());
        double angle = Math.toDegrees(dir.angle(diff));

        // Since we're only checking the angle in one direction rather than two, halve the vision radius
        if (angle <= visionRadius / 2)
            // true if within radius and within vision range
            // square roots are demanding and this might be used many times
            // so I just compare squares
            return source.distanceSquared(target) <= Math.pow(visionRange, 2);
        else
            // false if not in vision radius
            return false;
    }

    /**
     * Gets the angle toward the X axis
     *
     * @param vector The vector to check
     * @return The angle toward the X axis
     */
    public static double angleToXAxis(Vector vector) {
        return Math.atan2(vector.getX(), vector.getY());
    }

    public static Vector direction(Location from, Entity to) {
        return direction(from, to.getLocation());
    }

    public static Vector direction(Entity from, Location to) {
        return direction(from.getLocation(), to);
    }

    public static Vector direction(Entity from, Entity to) {
        return direction(from.getLocation(), to.getLocation());
    }

    public static Vector direction(Location from, Location to) {
        return direction(from.toVector(), to.toVector());
    }

    public static Vector direction(Vector from, Vector to) {
        return from.clone().subtract(to).normalize();
    }

}