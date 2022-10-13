package com.outcast.rpgcore.util.math.geometry;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public final class ShapeUtil {

    /**
     * Generates a list of Locations that form a line from start to end.
     *
     * @param start  Start point of the line.
     * @param end    Endpoint of the line.
     * @param points Number of Locations in the resulting line.
     * @return A list of Locations.
     **/
    public static List<Location> line(Location start, Location end, double points) {
        List<Location> locations = new ArrayList<>();
        // subtracting 1 ensures that one location will always be at the endpoint
        // since the final iteration of the for loop below will multiply increment by points - 1.
        Vector increment = end.toVector().subtract(start.toVector()).divide(new Vector(points - 1,
                points - 1, points - 1));
        for (int i = 0; i < points; i++)
            locations.add(start.clone().add(increment.clone().multiply(i)));
        return locations;
    }

    /**
     * Returns a bezier curve
     *
     * @param start The start location of the curve.
     * @param cp    The control point where the curve starts to arc.
     * @param end   The end location of the curve.
     */
    public static List<Location> curve(Location start, Location cp, Location end, int point) {
        List<Location> locations = new ArrayList<>();
        for (int i = 1; i < point; i++) {
            float t = i / (float) point;
            locations.add(point(start, cp, end, t));
        }
        return locations;
    }

    public static Location point(Location start, Location cp, Location end, float t) {
        float a = (1 - t) * (1 - t);
        float b = 2 * (1 - t) * t;
        float c = t * t;

        double x = (start.getX() * a) + (cp.getX() * b) + (end.getX() * c);
        double y = (start.getY() * a) + (cp.getY() * b) + (end.getY() * c);
        double z = (start.getZ() * a) + (cp.getZ() * b) + (end.getZ() * c);

        return new Location(start.getWorld(), x, y, z);
    }

    /**
     * Generates a horizontal circle of Locations with the given radius.
     *
     * @param center The center of the circle.
     * @param radius The radius of the circle, in blocks/meters.
     * @return A list of Locations.
     */
    public static List<Location> filledCircle(Location center, double radius, int points) {
        List<Location> locations = new ArrayList<>();
        double angle = 360.0 / points;
        for (int r = 0; r < radius; r++) {
            for (int i = 0; i < points; i++) {
                locations.add(center.clone().add(r * Math.cos(i * angle),
                        0, r * Math.sin(i * angle)));
            }
        }
        return locations;
    }

    /**
     * Generates a horizontal circle of Locations with the given radius.
     *
     * @param center The center of the circle.
     * @param radius The radius of the circle, in blocks/meters.
     * @param points The number of points in the circle.
     * @return A list of Locations.
     */
    public static List<Location> circle(Location center, double radius, int points, double rotateAngle, boolean rotateVector) {
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            double angle = ((2 * Math.PI) / points) * i;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Vector v;
            if (rotateVector) {
                v = VectorUtil.rotateAroundAxisY(new Vector(x, 0.0, z), rotateAngle);
                locations.add(center.clone().add(v.getX(), 0, v.getZ()));
            } else {
                locations.add(center.clone().add(x, 0, z));
            }
        }
        return locations;
    }

    /**
     * Generates a horizontal cone of Locations with the given radius.
     *
     * @param locations The locations returned from a circle check.
     * @param startPos  The starting location of the cone its origin point.
     * @param radius    The radius that you want to capture of the circle.
     * @param degrees   The degree that you want to capture of the circle.
     * @param direction The direction you are facing.
     * @return A list of Locations.
     */
    public static List<Location> cone(List<Location> locations, Vector startPos, float radius, float degrees, Vector direction) {
        List<Location> newLocations = new ArrayList<>(); // Returned list
        float squaredRadius = radius * radius;
        for (Location loc : locations) {
            Vector relativePosition = loc.toVector();
            // Position of the location relative to the cone origin
            relativePosition.subtract(startPos);
            if (relativePosition.lengthSquared() > squaredRadius) {
                continue;
            } // First check : distance
            if (Math.abs((float) Math.toDegrees(direction.angle(relativePosition))) > degrees) {
                continue;
            } // Second check : angle

            newLocations.add(loc); // The entity e is in the cone
        }
        return newLocations;
    }

    /**
     * Generates a list of locations that form a horizontal quadrilateral.
     *
     * @param center       Center of the quadrilateral.
     * @param pointsWidth  Number of points along the "width" side.
     * @param pointsLength Number of points along the "length" side.
     */
    public static List<Location> quad(Location center, double width, double length,
                                      int pointsWidth, int pointsLength) {
        List<Location> locations = new ArrayList<>();
        Location offset = center.clone().subtract(width / 2, 0, length / 2);
        double widthIncrement = width / pointsWidth,
                lengthIncrement = length / pointsLength;
        for (int x = 0; x < pointsWidth; x++) {
            for (int z = 0; z < pointsLength; z++) {
                locations.add(offset.clone().add(x * widthIncrement, 0, z * lengthIncrement));
            }
        }
        return locations;
    }

    /**
     * Generates a cube of Locations. See {@link #quad(Location, double, double, int, int)} for arguments.
     */
    public static List<Location> cube(Location center, double width, double length, double height, int pointsWidth,
                                      int pointsLength, int pointsHeight) {
        List<Location> locations = new ArrayList<>();
        double heightIncrement = height / pointsHeight;
        for (int y = 0; y < pointsHeight; y++) {
            locations.addAll(quad(center, width, length, pointsWidth, pointsLength));
            center.add(0, heightIncrement, 0);
        }
        return locations;
    }

    /**
     * Generates a Fibonacci sphere around the provided location with the given radius.
     *
     * @param points Number of points in the sphere.
     */
    public static List<Location> sphere(Location center, double radius, int points) {
        List<Location> locations = new ArrayList<>();
        double phi = Math.PI * (3.0 - Math.sqrt(5.0));
        for (int i = 0; i < points; i++) {
            // constrain y to [-1.0, 1.0]
            double y = 1.0 - ((double) i / ((double) points - 1.0)) * 2.0;
            // angle at i
            double theta = phi * i;
            double r = radius * Math.sqrt(1.0 - y * y);
            double x = r * Math.cos(theta);
            double z = r * Math.sin(theta);
            locations.add(center.clone().add(x, y, z));
        }
        return locations;
    }

    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    //  Custom locations for different animations.
    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public static List<Location> wings(Location center) {
        List<Location> locations = new ArrayList<>();
        double phi = Math.PI * (3.0 - Math.sqrt(5.0));
        for (double i = 0; i < Math.PI * 2; i += Math.PI / 48) {
            double offset = (Math.pow(Math.E, Math.cos(i)) - 2 * Math.cos(i * 4) - Math.pow(Math.sin(i / 12), 5)) / 2;
            double x = Math.sin(i) * offset;
            double y = Math.cos(i) * offset;
            Vector v = VectorUtil.rotateAroundAxisY(new Vector(x, y, -0.3), -Math.toRadians(center.getYaw()));
            locations.add(center.clone().add(v.getX(), v.getY(), v.getZ()));
        }
        return locations;
    }

    public static List<Location> orbit(Location location, double radius, int orbs, int points) {
        int step = 0;

        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < orbs; i++) {
            double dx = -(Math.cos((step / (double) points) * (Math.PI * 2) + (((Math.PI * 2) / orbs) * i))) * radius;
            double dz = -(Math.sin((step / (double) points) * (Math.PI * 2) + (((Math.PI * 2) / orbs) * i))) * radius;
            locations.add(location.clone().add(dx, 0, dz));
        }
        return locations;
    }

    public static List<Location> helix(Location center, double height, double radius, double particleInterval) {
        List<Location> locations = new ArrayList<>();
        double y;
        for (y = 0.0; y <= height; y += particleInterval) {
            double x = center.getX() + radius * Math.cos(y);
            double z = center.getZ() + radius * Math.sin(y);
            locations.add(new Location(center.getWorld(), x, center.getY() + y, z));
        }

        return locations;
    }

}
